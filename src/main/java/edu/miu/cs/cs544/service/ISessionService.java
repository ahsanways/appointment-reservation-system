package edu.miu.cs.cs544.service;


import java.util.List;
import java.util.Optional;

import edu.miu.cs.cs544.domain.Appointment;
import edu.miu.cs.cs544.domain.Session;

public interface ISessionService {
    List<Appointment> getAppointmentsByCustomerId();
    Session createSession(Session session);
    Session createSessionByAdmin(Session session);
    Session getSession(int sessionId);
    Optional<Session> getSessionById(int sessionId);
    void saveOrUpdate(Session session);
    List<Session> getAllSessions();
    List<Session> getAllSessionsForAdmin();

    List<Session> getCounselorSessions();
    List<Session> getSessionsByCustomerId();
    List<Session> getFutureSessionsByCustomerId();
    String  deleteSession(int sessionId);
    String deleteSessionByAdmin(int sessionId) throws Exception;
    Session updateSession(Session session,int sessionId);
    List<Session> getFutureSessions();
}
