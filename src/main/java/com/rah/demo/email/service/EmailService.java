package com.rah.demo.email.service;

import java.io.File;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
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

	public void enviarCorreoHtml(String para, String asunto, String cuerpoHtml, File archivoAdjunto,
			byte[] qrBytes) {
		try {
			// 1. Crear el mensaje Mime base
			MimeMessage mensaje = mailSender.createMimeMessage();

			// 2. Usar el Helper con el flag 'true' para activar contenido multipart (HTML y
			// adjuntos)
			MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

			helper.setFrom(this.usernameEmail);
			helper.setTo(para);
			helper.setSubject(asunto);

			// 3. Definir el cuerpo. El segundo parámetro 'true' le dice a Spring que es
			// código HTML
			helper.setText(cuerpoHtml, true);

			// 4. Agregar el archivo adjunto si existe
			if (archivoAdjunto != null && archivoAdjunto.exists()) {
				FileSystemResource resource = new FileSystemResource(archivoAdjunto);
				// El primer parámetro es el nombre que verá el usuario al descargar el archivo
				helper.addAttachment(archivoAdjunto.getName(), resource);
			}

			// 5. Agregar imagen del QR usando su identificador CID
			if (qrBytes != null && qrBytes.length > 0) {
				ByteArrayResource qrResource = new ByteArrayResource(qrBytes);
				// El identificador "codigoQR" debe coincidir exactamente con el cid:codigoQR
				// del HTML
				helper.addInline("codigoQR", qrResource, "image/png");
			}

			// 6. Enviar el correo
			mailSender.send(mensaje);

		} catch (MessagingException exception) {
			exception.printStackTrace();
			throw new RuntimeException("Error al procesar el envío: " + exception.getMessage());
		}
	}
	

	public void enviarCorreoHtml(String para, String asunto, String cuerpoHtml, String qrImage) {
		try {
			// 1. Crear el mensaje Mime base
			MimeMessage mensaje = mailSender.createMimeMessage();

			// 2. Usar el Helper con el flag 'true' para activar contenido multipart (HTML y
			// adjuntos)
			MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

			helper.setFrom(this.usernameEmail);
			helper.setTo(para);
			helper.setSubject(asunto);

			// 3. Definir el cuerpo. El segundo parámetro 'true' le dice a Spring que es
			// código HTML
			helper.setText(cuerpoHtml, true);

			// 6. Agregar imagen del QR usando su identificador CID
			byte[] qrBytes = Base64.getDecoder().decode(qrImage);
			if (qrBytes != null && qrBytes.length > 0) {
				ByteArrayResource qrResource = new ByteArrayResource(qrBytes);
				// El identificador "codigoQR" debe coincidir exactamente con el cid:codigoQR
				// del HTML
				helper.addInline("codigoQR", qrResource, "image/png");
			}

			// 5. Enviar el correo
			mailSender.send(mensaje);

		} catch (MessagingException exception) {
			exception.printStackTrace();
			throw new RuntimeException("Error al procesar el envío: " + exception.getMessage());
		}
	}
}
