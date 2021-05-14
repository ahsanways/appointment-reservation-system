package edu.miu.cs.cs544.controller;

import edu.miu.cs.cs544.domain.Appointment;
import edu.miu.cs.cs544.domain.Person;

import edu.miu.cs.cs544.domain.Session;
import edu.miu.cs.cs544.dto.AppointmentResponse;
import edu.miu.cs.cs544.dto.PersonResponse;
import edu.miu.cs.cs544.dto.SessionResponse;
import edu.miu.cs.cs544.service.IMapService;
import edu.miu.cs.cs544.service.IPersonService;
import edu.miu.cs.cs544.service.IAppointmentService;
import edu.miu.cs.cs544.service.ISessionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private IPersonService personService;
    @Autowired
    private IAppointmentService appointmentService;
    @Autowired
    private ISessionService sessionService;
    @Autowired
    private IMapService mapService;

    @GetMapping
    public ResponseEntity<?> getCustomer(){
        Person person = personService.getPerson();
        PersonResponse personResponse = mapService.convertToDTO(person);
        return ResponseEntity.ok(personResponse);
    }

    // Session
    @GetMapping("/sessions")
    public ResponseEntity<?> getSessions(){
        List<Session> sessionList = sessionService.getSessionsByCustomerId();
        List<SessionResponse> sessionResponseList = sessionList.stream().map(mapService::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(sessionResponseList);
    }

    @GetMapping("/sessions/future")
    public ResponseEntity<?> getFutureSessions(){
        List<Session> sessionList = sessionService.getFutureSessionsByCustomerId();
        List<SessionResponse> sessionResponseList = sessionList.stream().map(mapService::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(sessionResponseList);
    }
    // END Session

    // Appointment
    @GetMapping("/appointments")
    public ResponseEntity<?> getAppointments(){
        List<Appointment> appointmentList = appointmentService.getCustomerAppointments();
        List<AppointmentResponse> appointmentResponseList = appointmentList.stream().map(mapService::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(appointmentResponseList);
    }

    @GetMapping("/appointments/{appointmentId}")
    public ResponseEntity<?> getAppointment(@PathVariable int appointmentId){
        Appointment appointment = appointmentService.getAppointment(appointmentId);
        AppointmentResponse appointmentResponse = mapService.convertToDTO(appointment);
        return ResponseEntity.ok(appointmentResponse);
    }

    @PatchMapping("/appointments/cancel/{appointmentId}")
    public ResponseEntity<?> cancelAppointment(@PathVariable("appointmentId") int appointmentId){
        Appointment appointment = appointmentService.cancelAppointmentCustomer(appointmentId);
        AppointmentResponse appointmentResponse = mapService.convertToDTO(appointment);
        return ResponseEntity.ok(appointmentResponse);
    }

    @PatchMapping("/appointments/commence/{appointmentId}")
    public ResponseEntity<?> commenceAppointment(@PathVariable("appointmentId") int appointmentId){
        Appointment appointment = appointmentService.commenceAppointmentCustomer(appointmentId);
        AppointmentResponse appointmentResponse = mapService.convertToDTO(appointment);
        return ResponseEntity.ok(appointmentResponse);
    }

    @DeleteMapping("/appointments/{appointment_id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable int appointment_id){
        appointmentService.deleteAppointment(appointment_id);
        return ResponseEntity.ok("Delete Successful.");
    }

    @GetMapping("/sessions/{sessionId}/appointments/request")
    public ResponseEntity<?> requestAppointment(@PathVariable int sessionId){
        Appointment appointment = appointmentService.requestAppointment(sessionId);
        AppointmentResponse appointmentResponse = mapService.convertToDTO(appointment);
        return ResponseEntity.ok(appointmentResponse);
    }
    // END Appointment

}
