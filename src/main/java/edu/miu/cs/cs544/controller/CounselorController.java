package edu.miu.cs.cs544.controller;

import edu.miu.cs.cs544.domain.Appointment;
import edu.miu.cs.cs544.domain.Session;
import edu.miu.cs.cs544.dto.AppointmentResponse;
import edu.miu.cs.cs544.dto.SessionRequest;
import edu.miu.cs.cs544.dto.SessionResponse;
import edu.miu.cs.cs544.service.IAppointmentService;
import edu.miu.cs.cs544.service.IMapService;
import edu.miu.cs.cs544.service.ISessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/counselors")
public class CounselorController {
    @Autowired
    private ISessionService sessionService;

    @Autowired
    private IAppointmentService appointmentService;

    @Autowired
    private IMapService mapService;

    @GetMapping("/sessions")
    public ResponseEntity<?> getSessions(){
        List<Session> sessionList = sessionService.getCounselorSessions();
        List<SessionResponse> sessionResponseList = sessionList.stream().map(mapService::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(sessionResponseList);
    }

    @PostMapping("/sessions")
    public ResponseEntity<?> createSession(@Validated @RequestBody SessionRequest sessionRequest) {
        Session session = mapService.convertToEntity(sessionRequest);
        session = sessionService.createSession(session);
        SessionResponse sessionResponse = mapService.convertToDTO(session);
        return ResponseEntity.ok(sessionResponse);
    }

    @DeleteMapping("/sessions/{session_id}")
    public ResponseEntity<?> deleteSession(@PathVariable int session_id){
        String result = sessionService.deleteSession(session_id);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/sessions/appointments/{appointment_id}")
    public ResponseEntity<?> approveAppointment(@PathVariable int appointment_id){
        Appointment appointment = appointmentService.approveAppointment(appointment_id);
        AppointmentResponse appointmentResponse = mapService.convertToDTO(appointment);
        return ResponseEntity.ok(appointmentResponse);
    }

    @PutMapping("/sessions/{session_id}")
    public ResponseEntity<?> updateSession(@Validated @RequestBody SessionRequest sessionRequest, @PathVariable int session_id){
        Session session = mapService.convertToEntity(sessionRequest);
        session = sessionService.updateSession(session, session_id);
        SessionResponse sessionResponse = mapService.convertToDTO(session);
        return ResponseEntity.ok(sessionResponse);
    }
}
