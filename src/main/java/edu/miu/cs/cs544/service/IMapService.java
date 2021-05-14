package edu.miu.cs.cs544.service;

import edu.miu.cs.cs544.domain.Appointment;
import edu.miu.cs.cs544.domain.Person;
import edu.miu.cs.cs544.domain.Session;
import edu.miu.cs.cs544.dto.*;

public interface IMapService {
    Person convertToEntity(PersonRequest personRequest);
    PersonResponse convertToDTO(Person person);

    Session convertToEntity(SessionRequest sessionRequest);
    SessionResponse convertToDTO(Session session);

    Appointment convertToEntity(AppointmentRequest appointmentRequest);
    AppointmentResponse convertToDTO(Appointment appointment);
}
