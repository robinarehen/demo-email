package com.rah.demo.email.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

	private final JavaMailSender mailSender;
	
	@Value("${spring.mail.username}")
	private String usernameEmail;

	public void enviarCorreoSimple(String para, String asunto, String cuerpo) {
		SimpleMailMessage mensaje = new SimpleMailMessage();
		mensaje.setFrom(this.usernameEmail);
		mensaje.setTo(para);
		mensaje.setSubject(asunto);
		mensaje.setText(cuerpo);

		mailSender.send(mensaje);
	}
}
