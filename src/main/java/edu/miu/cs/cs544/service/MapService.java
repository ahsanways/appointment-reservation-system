package edu.miu.cs.cs544.service;

import edu.miu.cs.cs544.domain.Appointment;
import edu.miu.cs.cs544.domain.Person;
import edu.miu.cs.cs544.domain.Session;
import edu.miu.cs.cs544.dto.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MapService implements IMapService{

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Person convertToEntity(PersonRequest personRequest) {
        return modelMapper.map(personRequest,Person.class);
    }

    @Override
    public PersonResponse convertToDTO(Person person) {
        PersonResponse personResponse = modelMapper.map(person,PersonResponse.class);
        personResponse.setRoles(person.getRoles());
        return personResponse;
    }

    @Override
    public Session convertToEntity(SessionRequest sessionRequest) {
        return modelMapper.map(sessionRequest,Session.class);
    }

    @Override
    public SessionResponse convertToDTO(Session session) {
        SessionResponse sessionResponse = modelMapper.map(session,SessionResponse.class);
        sessionResponse.setCounselor(convertToDTO(session.getCounselor()));
        return sessionResponse;
    }

    @Override
    public Appointment convertToEntity(AppointmentRequest appointmentRequest) {
        return null;
    }

    @Override
    public AppointmentResponse convertToDTO(Appointment appointment) {
        AppointmentResponse appointmentResponse = modelMapper.map(appointment,AppointmentResponse.class);
        appointmentResponse.setCustomer(convertToDTO(appointment.getCustomer()));
        appointmentResponse.setSession(convertToDTO(appointment.getSession()));
        return appointmentResponse;
    }
}
