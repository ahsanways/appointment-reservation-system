package edu.miu.cs.cs544.service;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.miu.cs.cs544.common.MyEntityAlreadyExistException;
import edu.miu.cs.cs544.common.MyEntityNotFoundException;
import edu.miu.cs.cs544.domain.Appointment;
import edu.miu.cs.cs544.domain.AppointmentStatus;
import edu.miu.cs.cs544.domain.Person;
import edu.miu.cs.cs544.domain.Role;
import edu.miu.cs.cs544.domain.RoleType;
import edu.miu.cs.cs544.domain.Session;
import edu.miu.cs.cs544.repository.AppointmentRepository;
import edu.miu.cs.cs544.repository.PersonRepository;
import edu.miu.cs.cs544.repository.SessionRepository;

@Service
public class AppointmentService implements IAppointmentService{
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private PersonDetailsService personDetailService;


    @Override
    public Appointment createAppointment(Appointment appointment) {
    	appointmentRepository.save(appointment);
    	sessionRepository.save(appointment.getSession());
    	return appointment;
    }

    public Appointment requestAppointment(int sessionId) {
        Session session = sessionRepository.findById(sessionId).get();
        Person customer = personDetailService.getCurrentPerson();

        Appointment appointment = new Appointment(AppointmentStatus.PENDING, session, customer);
        return appointmentRepository.save(appointment);}

    @Override
    public Appointment getAppointment(int appointmentId) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        if(! appointmentExist(appointment)) return null;

        return appointment.get();}

    @Override
    public List<Appointment> getAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public List<Appointment> getCustomerAppointments() {
        Person customer = personDetailService.getCurrentPerson();
        List<Appointment> appointments = appointmentRepository.findByCustomerId(customer.getId());
        return appointments;}

    @Override
    public List<Appointment> getSessionAppointments(int sessionId) {
        return appointmentRepository.findBySessionId(sessionId);}

    @Override
    public List<Appointment> getCounselorAppointments() {
        return null;
    }

    @Override
    public Appointment approveAppointment(int appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).get();
        int session_id = appointment.getSession().getId();
        List<Appointment> optionalAppointment = appointmentRepository.findByStatusAndSessionId(AppointmentStatus.APPROVED,session_id);
        if(optionalAppointment.isEmpty()) {
            appointment.setStatus(AppointmentStatus.APPROVED);
            return appointmentRepository.save(appointment);
        }
        if(optionalAppointment.get(0).getId() == appointmentId) {
            throw new RuntimeException("This appointment is already approved!");
        }
        throw new RuntimeException("Another Appointment is already approved!");
    }

    // Utils
    private boolean appointmentExist(Optional<Appointment> appointment) {
        if(! appointment.isPresent())
            throw new RuntimeException("Appointment ID is not found");

        return true;}

    private Appointment cancelUtil(Appointment appointment) {
        // Cancel if appointment's status is NOT CANCELLED ONLY
        if( appointment.getStatus().equals(AppointmentStatus.CANCELLED) )
            throw new RuntimeException(appointment.getStatus()+" Appointment Can NOT be Cancelled!");

        // If an approved Appointment is being cancelled, replace it with the earliest PENDING AppointmentRequest
        if( appointment.getStatus().equals(AppointmentStatus.APPROVED) ) {
            // Get Session
            Session session = appointment.getSession();

            // Get Appointment Requests (PENDING Appointments) of Session
            List<Appointment> pendingAppointments = appointmentRepository.findByStatusAndSessionId(AppointmentStatus.PENDING, session.getId());

            // Get the earliest if there's any, forget operation replace if not
            if( pendingAppointments.size() > 0 ) {
                Appointment earliestPendingAppointment = pendingAppointments.get(0);

                // Change earliestPendingAppointment's Status to APPROVED
                earliestPendingAppointment.setStatus(AppointmentStatus.APPROVED);
                appointmentRepository.save(earliestPendingAppointment);
            }
        }
        
        // Cancel Appointment
        appointment.setStatus(AppointmentStatus.CANCELLED);
        return appointmentRepository.save(appointment);}

    private Appointment commenceUtil(Appointment appointment) {
        // Commence if appointment's status is CANCELLED ONLY
        if(! appointment.getStatus().equals(AppointmentStatus.CANCELLED) )
            throw new RuntimeException(appointment.getStatus()+" Appointment Can NOT be Commenced!");

        appointment.setStatus(AppointmentStatus.PENDING);
        return appointmentRepository.save(appointment);}

    private void deleteUtil(Appointment appointment) {
        Session session = appointment.getSession();
        session.removeAppointment(appointment);
        appointmentRepository.delete(appointment);;
        sessionRepository.save(session);}

    private boolean validAppointment4Admin(Optional<Appointment> appointment) {
        // Exception thrown if appointment does NOT exist
        if(! appointmentExist(appointment))
            throw new RuntimeException("Appointment does NOT exist!");

        return true;}

    private boolean validAppointment4Customer(Optional<Appointment> appointment) {
        // Exception thrown if appointment does NOT exist
        if(! appointmentExist(appointment))
            throw new RuntimeException("Appointment does NOT exist!");

        Appointment realAppointment = appointment.get();

        // Check if appointment is made by this CUSTOMER, if it's not do NOT cancel and return null
        Person customer = personDetailService.getCurrentPerson();
        if( realAppointment.getCustomer().getId() != customer.getId() )
            throw new RuntimeException("Appointment Can NOT be Accessed!");

        // Check if appointment is within 2 days (48 hours), if it's not do NOT cancel
        LocalDate sessionDate = realAppointment.getSession().getDate();
        if( sessionDate.compareTo( LocalDate.now() ) <= 2 )
            throw new RuntimeException("Appointment Can NOT Be Updated because it takes place within the next 48 hours!");

        return true;}
    // END Utils

    @Override
    public Appointment cancelAppointment(int appointmentId) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        if(! validAppointment4Admin(appointment)) return null;

        return cancelUtil(appointment.get());}

    @Override
    public Appointment cancelAppointmentCustomer(int appointmentId) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        if(! validAppointment4Customer(appointment)) return null;

        return cancelUtil(appointment.get());}

    @Override
    public Appointment commenceAppointment(int appointmentId) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        if(! validAppointment4Admin(appointment)) return null;

        return commenceUtil(appointment.get());}

    @Override
    public Appointment commenceAppointmentCustomer(int appointmentId) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        if(! validAppointment4Customer(appointment)) return appointment.get();

        return commenceUtil(appointment.get());}

    @Override
    public void deleteAppointment(int appointmentId) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        if(! validAppointment4Admin(appointment)) return;

        deleteUtil(appointment.get());}

    @Override
    public void deleteAppointmentCustomer(int appointmentId) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        if(! validAppointment4Customer(appointment)) return;

        deleteUtil(appointment.get());}

    @Override
    public Appointment updateAppointmentAdmin(int appointmentId) {
    	Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        if(! validAppointment4Admin(appointment)) return null;
        
        return commenceUtil(appointment.get());
    }

	@Override
	public Appointment approveAppointmentByAdmin(int appointmentId) {
		Appointment appointment = appointmentRepository.findById(appointmentId).get();
        appointment.setStatus(AppointmentStatus.APPROVED);
        return appointmentRepository.save(appointment);
	}
	
	@Override
	public Optional<Appointment> getAppointmentBySessionCustomer(int sessionId, int customerId){
		return appointmentRepository.findBySessionCustomer(sessionId, customerId);
	}

	@Override
	public Optional<Appointment> getAppointmentById(int appointmentId) {
		return appointmentRepository.findById(appointmentId);
	}
}
