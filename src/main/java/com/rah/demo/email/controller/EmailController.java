package com.rah.demo.email.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rah.demo.email.service.EmailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class EmailController {

	private final EmailService emailService;

	@GetMapping("/enviar-test")
	public String enviarTest(@RequestParam String destino) {
		emailService.enviarCorreoSimple(destino, "Prueba de Spring Boot 3.5",
				"¡Hola! Este es un correo de prueba usando Java 21 y Spring Boot.");
		return "Correo enviado con éxito a: " + destino;
	}
}
