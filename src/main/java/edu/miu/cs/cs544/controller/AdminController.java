package edu.miu.cs.cs544.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import edu.miu.cs.cs544.common.MyEntityAlreadyExistException;
import edu.miu.cs.cs544.common.MyEntityNotFoundException;
import edu.miu.cs.cs544.domain.Appointment;
import edu.miu.cs.cs544.domain.AppointmentStatus;
import edu.miu.cs.cs544.domain.Person;
import edu.miu.cs.cs544.domain.Role;
import edu.miu.cs.cs544.domain.RoleType;
import edu.miu.cs.cs544.domain.Session;
import edu.miu.cs.cs544.dto.AppointmentResponse;
import edu.miu.cs.cs544.dto.PersonRequest;
import edu.miu.cs.cs544.dto.PersonResponse;
import edu.miu.cs.cs544.dto.SessionRequest;
import edu.miu.cs.cs544.dto.SessionResponse;
import edu.miu.cs.cs544.service.IAppointmentService;
import edu.miu.cs.cs544.service.IMapService;
import edu.miu.cs.cs544.service.IPersonService;
import edu.miu.cs.cs544.service.ISessionService;


@RestController
@RequestMapping("/admins")
public class AdminController {
  
    @Autowired
    private IPersonService personService;
    
    @Autowired
    private IAppointmentService appointmentService;
    
    @Autowired
    private ISessionService sessionService;
    
    @Autowired
    private IMapService mapService;

    @PostMapping
    public ResponseEntity<?> createCounselor(@RequestBody PersonRequest personRequest) {
    	Person counselor = mapService.convertToEntity(personRequest);
        PersonResponse respCounselor = mapService.convertToDTO(personService.createCounselor(counselor));
		return ResponseEntity.ok(respCounselor);
    }
    
    // UC 3.11: view list of user
    @GetMapping("/users")
    public ResponseEntity<?> ListAllUsers(){
    	List<Person> personList = personService.getAll();
    	List<PersonResponse> respPersonList =
    			personList.stream().map(mapService::convertToDTO).collect(Collectors.toList());
    	return ResponseEntity.ok(respPersonList);
    }
    
    // UC 3.11: view user by ID
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") int id){
    	Optional<Person> person = personService.getPersonById(id);
    	if(!person.isPresent()) {
    		throw new MyEntityNotFoundException("User ID not found");
    	}
    	PersonResponse respPerson = mapService.convertToDTO(person.get());
    	return ResponseEntity.ok(respPerson);
    }
  
    // UC 3.11: create user profile
    @PostMapping("/users")
    public ResponseEntity<?> createUser(@Validated @RequestBody Person person) {
    	if(personService.getPersonByUsername(person.getUsername()).isPresent()) {    		
    		throw new MyEntityAlreadyExistException("username already exists");
    	}
    	else if (personService.getPersonByEmail(person.getEmail()).isPresent()) {
    		throw new MyEntityAlreadyExistException("email already exists");
    	}
    	else {
    		PersonResponse respPerson = mapService.convertToDTO(personService.createPerson(person));
    		return ResponseEntity.ok(respPerson);
    	}
    }
    
    // UC 3.11: update user profile
    @PutMapping("/users")
    public ResponseEntity<?> updateUser(@Validated @RequestBody Person person) {
    	Optional<Person> personDB = personService.getPersonById(person.getId());
    	if(!personDB.isPresent()) {
    		throw new MyEntityNotFoundException("User ID not found");
    	}    	
    	person.setPassword(new BCryptPasswordEncoder().encode(person.getPassword()));        
    	PersonResponse respPerson = mapService.convertToDTO(personService.updatePerson(person));
    	return ResponseEntity.ok(respPerson);
    }
    
    // UC 3.11: delete user profile
    @DeleteMapping("/users/{id}")
    public void removeUserById(@PathVariable("id") int id){
    	Optional<Person> person = personService.getPersonById(id);
    	if(!person.isPresent()) {
    		throw new MyEntityNotFoundException("User ID not found");
    	}    	
    	personService.deletePerson(id);
    }
    
    // UC 3.7: view list of appointment
    @GetMapping("/appointments")
    public ResponseEntity<?> ListAllAppointment() {
    	List<Appointment> appointmentList = appointmentService.getAppointments();
    	List<AppointmentResponse> respAppointmentList =
    			appointmentList.stream().map(mapService::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(respAppointmentList);
    }
    
    // UC 3.8: create new appointment
    @PostMapping("/appointments/{sessionId}/{customerId}")
    public ResponseEntity<?> createAppointment(@PathVariable("sessionId") int sessionId,
    		                             @PathVariable("customerId") int customerId) {
       	Optional<Session> sessionDB = sessionService.getSessionById(sessionId);
    	Optional<Person> customerDB = personService.getPersonById(customerId);
    	if(!sessionDB.isPresent() ) {
    		throw new MyEntityNotFoundException("Session ID not found");
    	}
    	else if(!customerDB.isPresent() ) {
    		throw new MyEntityNotFoundException("Customer ID not found");
    	}	
    	else {
    		boolean isCustomerRole = false;
    		Person customer = customerDB.get();
    		for(Role r : customer.getRoles()) {
    			if(r.getRoleType() == RoleType.CUSTOMER) {
    				isCustomerRole = true;
    				break;
    			}
    		}    		
    		if(!isCustomerRole) {
    			throw new RuntimeException("Role is not as Customer for appointment request");  
    		}
    		else {
        	    Optional<Appointment> appointmentDB = 
        	    		appointmentService.getAppointmentBySessionCustomer(sessionId, customerId);
        	    if(appointmentDB.isPresent()) {
        	    	throw new MyEntityAlreadyExistException("Customer already made the appointment");
        	    }
        	    else {
        	    	Session saveSession = sessionDB.get();
        	    	Appointment appointment = new Appointment();
        	    	appointment.setCustomer(customer);        	    	
        	    	appointment.setStatus(AppointmentStatus.PENDING);        	    	        	    	
        	    	saveSession.addAppointment(appointment);
        	    	appointmentService.createAppointment(appointment);
        	    	AppointmentResponse respAppointment = mapService.convertToDTO(appointment);
        	    	return ResponseEntity.ok(respAppointment);
        	    }
        	}
    	}
    }   

    // UC 3.19: update an appointment
    @PutMapping("/appointments/update/{id}")
    public ResponseEntity<?> updateAppointment(@PathVariable("id") int id) {
 	   Appointment appointment = appointmentService.updateAppointmentAdmin(id);
 	   AppointmentResponse respAppointment = mapService.convertToDTO(appointment);
  	   return ResponseEntity.ok(respAppointment);
    }
    
    // UC 3.10: cancel appointment
    @PutMapping("/appointments/cancel/{id}")
    public void cancelAppointment(@PathVariable("id") int id) {
        appointmentService.cancelAppointment(id);
    }
    
    // UC 3.10: delete appointment
    @DeleteMapping("/appointments/{id}")
    public void removeAppointment(@PathVariable("id") int id) {
        appointmentService.deleteAppointment(id);
    }
  
  	@GetMapping("/sessions/future")
	public ResponseEntity<?> getFutureSessions() {
		List<Session> ls = sessionService.getAllSessions();
		List<Session> ls2 = new ArrayList<>();
		for (Session s : ls) {
			if (s.getDate().isAfter(LocalDate.now())) {
				ls2.add(s);
			}
		}
		List<SessionResponse> respSessionList =
				ls2.stream().map(mapService::convertToDTO).collect(Collectors.toList());;
		return ResponseEntity.ok(respSessionList);
	}
  	
    @GetMapping("/sessions")
    public ResponseEntity<?> getAllSessionsForAdmin(){
        List<Session> sessionList = sessionService.getAllSessionsForAdmin();
        List<SessionResponse> respSessionList =
        		sessionList.stream().map(mapService::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(respSessionList);
    }
    
    @GetMapping("/sessions/{session_id}")  
    private ResponseEntity<?> getSessions(@PathVariable("session_id") int session_id){
    	Session retSession = sessionService.getSession(session_id);
    	SessionResponse respSession = mapService.convertToDTO(retSession);
		return ResponseEntity.ok(respSession);
    }  
    
    @PutMapping("/sessions") 
    private ResponseEntity<?> update(@Validated @RequestBody Session session) {    	
    	sessionService.saveOrUpdate(session);  
    	SessionResponse respSession = mapService.convertToDTO(session);
		return ResponseEntity.ok(respSession);
    }  

    @PostMapping("/sessions")
    public ResponseEntity<?> createSession(@Validated @RequestBody Session session) {
    	Session retSession = sessionService.createSessionByAdmin(session);
    	SessionResponse respSession = mapService.convertToDTO(retSession);
		return ResponseEntity.ok(respSession);
    }

    @DeleteMapping("/sessions/{session_id}")
    public String getSessionAppointments(@PathVariable int session_id){
    	try {
    		return sessionService.deleteSessionByAdmin(session_id);
    	}
    	catch(Exception ex) {
    		return "Session ID not found";
    	}
    }

    @GetMapping("/sessions/appointments/{appointment_id}")
    public ResponseEntity<?> approveAppointmentByAdmin(@PathVariable int appointment_id){
    	Appointment appointment = appointmentService.approveAppointmentByAdmin(appointment_id);
    	AppointmentResponse respAppointment = mapService.convertToDTO(appointment);
    	return ResponseEntity.ok(respAppointment);
    }

    //CREATE ADMIN
    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdmin(@Validated @RequestBody PersonRequest personRequest) {
    	Person person = mapService.convertToEntity(personRequest);
        Person admin = personService.createAdmin(person);
        PersonResponse respAdmin = mapService.convertToDTO(admin);
        return ResponseEntity.ok(respAdmin);
    }

    //Grant A PERSON COUNSELOR PRIVILEGE
    @PatchMapping("/grant-counselor/{customer_id}")
    public ResponseEntity<?> grantCounselorPrivilege(@PathVariable("customer_id") int customer_id){
        Person counselor = personService.grantCounselorPrivilege(customer_id);
        PersonResponse respCounselor = mapService.convertToDTO(counselor);
        return ResponseEntity.ok(respCounselor);
    }

    //REVOKE A PERSON A COUNSELOR PRIVILEGE
    @PatchMapping("/revoke-counselor/{customer_id}")
    public ResponseEntity<?> revokeCounselorPrivilege(@PathVariable("customer_id") int customer_id){
         Person counselor = personService.revokeCounselorPrivilege(customer_id);
         PersonResponse respCounselor = mapService.convertToDTO(counselor);
         return ResponseEntity.ok(respCounselor);
    }
}
