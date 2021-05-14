package edu.miu.cs.cs544.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.miu.cs.cs544.domain.Appointment;
import edu.miu.cs.cs544.domain.Person;
import edu.miu.cs.cs544.domain.Session;
import edu.miu.cs.cs544.repository.PersonRepository;
import edu.miu.cs.cs544.repository.SessionRepository;

@Service
public class SessionService implements ISessionService {
	@Autowired
	private SessionRepository sessionRepository;
	@Autowired
	private AppointmentService appointmentService;
	@Autowired
	private PersonDetailsService personDetailsService;
	@Autowired
	private PersonRepository personRepository;
	
	@Override
	public Session createSession(Session session) {
		Person counselor = personDetailsService.getCurrentPerson();
		session.setCounselor(counselor);
		return sessionRepository.save(session);
	}

	@Override
	public Session updateSession(Session session, int session_id) {
		Optional<Session> session1 = sessionRepository.findById(session_id);
		if (session1.isPresent()) {
			Session _session = session1.get();
			_session.setDuration(session.getDuration());
			_session.setDate(session.getDate());
			_session.setStartTime(session.getStartTime());
			_session.setLocation(session.getLocation());
			return sessionRepository.save(_session);
		}
		return null;
	}

	@Override
	public Session getSession(int sessionId) {

		return sessionRepository.findById(sessionId).get();
	}

	@Override
	public List<Appointment> getAppointmentsByCustomerId() {
		List<Appointment> customerAppointments = appointmentService.getCustomerAppointments();
		return customerAppointments;
	}

	@Override
	public List<Session> getSessionsByCustomerId() {
		// Get All Appointments By Customer
		List<Appointment> customerAppointments = appointmentService.getCustomerAppointments();

		// Empty Sessions List to Add Future Sessions to
		List<Session> sessions = new ArrayList<>();

		// Get All Sessions of Each Appointments
		for (Appointment ap : customerAppointments)
			sessions.add(ap.getSession());

		return sessions;
	}

	@Override
	public List<Session> getFutureSessionsByCustomerId() {
		Person customer = personDetailsService.getCurrentPerson();

		// Get All Appointments By Customer
		List<Appointment> customerAppointments = appointmentService.getCustomerAppointments();

		// Empty Sessions List to Add Future Sessions to
		List<Session> futureSessions = new ArrayList<>();

		// Get All Sessions of Each Appointments
		for (Appointment ap : customerAppointments) {
			Session session = ap.getSession();
			// Check if Session is in the Future
			if (session.isFutureSession())
				futureSessions.add(session);
		}

		return futureSessions;
	}

	@Override
	public List<Session> getCounselorSessions() {
		Person counselor = personDetailsService.getCurrentPerson();
		List<Session> sessions = sessionRepository.findSessionsByCounselorId(counselor.getId());
		return sessions;
	}

	@Override
	public String deleteSession(int sessionId) {
		sessionRepository.deleteById(sessionId);
		return "success";
	}

	@Override
	public Session createSessionByAdmin(Session session) {
		Person counselor = personRepository.findById(session.getCounselor().getId()).get();
        session.setCounselor(counselor);
        return sessionRepository.save(session);		
	}

	@Override
	public List<Session> getAllSessionsForAdmin() {
		return sessionRepository.findAll();
	}

	@Override
	public String deleteSessionByAdmin(int sessionId) throws Exception{
		try {
        	sessionRepository.deleteById(sessionId);
        } catch ( Exception ex ) {
        	throw new Exception("Session Id not found");
        }
		return "Success";
	}

	@Override
	public void saveOrUpdate(Session session) {
		sessionRepository.save(session); 
	}

	public List<Session> getFutureSessions() {
		List<Session> allSessions = sessionRepository.findAll();
		List<Session> futureSessions = new ArrayList<>();
		for (Session s : allSessions) {
			if (s.getDate().isAfter(LocalDate.now())) {
				futureSessions.add(s);
			}
		}
		return futureSessions;
	}
	
    @Override
    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }
    
    @Override
    public Optional<Session> getSessionById(int sessionId) {
    	return sessionRepository.findById(sessionId);
    }
}