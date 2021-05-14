package edu.miu.cs.cs544.service;


import java.util.List;
import java.util.Optional;

import edu.miu.cs.cs544.domain.Appointment;
import edu.miu.cs.cs544.domain.AppointmentStatus;

public interface IAppointmentService {
	Appointment createAppointment(Appointment appointment);
    Appointment requestAppointment(int sessionId);
    Appointment getAppointment(int appointmentId);
    Optional<Appointment> getAppointmentById(int appointmentId);
    List<Appointment> getAppointments();
    List<Appointment> getCustomerAppointments();
    List<Appointment> getSessionAppointments(int sessionId);
    List<Appointment> getCounselorAppointments();
    Appointment approveAppointment(int appointmentId);
    Appointment approveAppointmentByAdmin(int appointmentId);
    Appointment cancelAppointment(int appointmentId);
    Appointment cancelAppointmentCustomer(int appointmentId);
    Appointment commenceAppointment(int appointmentId);
    Appointment commenceAppointmentCustomer(int appointmentId);
    void deleteAppointment(int appointmentId);
    void deleteAppointmentCustomer(int appointmentId);
    Appointment updateAppointmentAdmin(int appointmentId);
    Optional<Appointment> getAppointmentBySessionCustomer(int sessionId, int customerId);
}
