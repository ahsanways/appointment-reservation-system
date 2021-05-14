package edu.miu.cs.cs544.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.miu.cs.cs544.domain.Appointment;
import edu.miu.cs.cs544.service.EmailService;

@Aspect
@Component
public class SendEmailAdvice { 	

	@Autowired
	private EmailService emailService;

//	@Pointcut("execution(* edu.miu.cs.cs544.service.AppointmentService.createAppointment(..))")
//	public void anyStatusChange() {
//	}

	@AfterReturning(pointcut = "execution(* edu.miu.cs.cs544.service.AppointmentService.createAppointment(..))"
			+ "|| execution(* edu.miu.cs.cs544.service.AppointmentService.requestAppointment(..))"
			+ "|| execution(* edu.miu.cs.cs544.service.AppointmentService.approveAppointment(..))"
			+ "|| execution(* edu.miu.cs.cs544.service.AppointmentService.cancelAppointment(..))"
			+ "|| execution(* edu.miu.cs.cs544.service.AppointmentService.deleteAppointment(..))"
			,returning = "appointment")
	public void sendEmail(JoinPoint joinPoint, Appointment appointment) {
		
		emailService.sendEmail(appointment);

	}

}