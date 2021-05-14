package edu.miu.cs.cs544.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import edu.miu.cs.cs544.domain.Appointment;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender javaMailSender;

	private void sendEmail(String toCustomer, String toCounselor, String body, String topic) {

		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setTo(new String[] { toCustomer, toCounselor });
		simpleMailMessage.setSubject(topic);
		simpleMailMessage.setText(body);

		javaMailSender.send(simpleMailMessage);
	}

	public void sendEmail(Appointment appointment) {

		String customerEmail = appointment.getCustomer().getEmail();
		String counselorEmail = appointment.getSession().getCounselor().getEmail();

		this.sendEmail(customerEmail, counselorEmail, appointment.toString(),
				"Appointment has been " + appointment.getStatus().toString());
	}
 
}