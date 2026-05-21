package com.rah.demo.email.controller;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

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

	@GetMapping("/enviar-html")
	public String enviarHtml(@RequestParam String destino, String asunto) {

		// 1. Crear un diseño HTML sencillo usando estilos en línea (Inline CSS)
		String cuerpoHtml = """
				<html>
				    <body style="font-family: Arial, sans-serif; color: #333;">
				        <div style="background-color: #f8f9fa; padding: 20px; border-radius: 5px;">
				            <h2 style="color: #007bff;">¡Hola desde Spring Boot 3.5!</h2>
				               <!-- Aquí se renderiza el arreglo de bytes -->
				               <img src="cid:codigoQR" alt="Código QR de Acceso" style="width: 200px; height: 200px; border: 1px solid #ccc;"/>
				               <br><br>
				            <p>Este es un correo electrónico con formato <strong>HTML</strong> dinámico.</p>
				            <hr style="border: 0; border-top: 1px solid #ccc;">
				            <p style="font-size: 12px; color: #6c757d;">Este correo fue generado automáticamente por Java 21.</p>
				        </div>
				    </body>
				</html>
				""";

		// 2. Crear un archivo temporal simulado para adjuntar (.txt o .pdf simulado)
		File archivoTemp = this.createFile();

		// 3. Enviar el correo usando el nuevo método
		emailService.enviarCorreoHtml(destino, asunto, cuerpoHtml, archivoTemp, this.createImage());

		this.deleteFile(archivoTemp);

		return "Correo HTML con adjunto enviado con éxito a: " + destino;
	}

	private byte[] createImage() {
		BufferedImage bufferedImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.setColor(Color.BLUE);
		g2d.fillRect(20, 20, 160, 160); // Simula el patrón de un QR básico
		g2d.dispose();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(bufferedImage, "png", baos);

//			return baos.toByteArray();

		} catch (IOException exception) {
			exception.printStackTrace();
			throw new RuntimeException("Error al generar la imagen: " + exception.getMessage());
		}

		return null;
	}

	private File createFile() {
		File archivoTemp = null;
		try {
			archivoTemp = File.createTempFile("Reporte_Demo_", ".txt");
			try (FileWriter writer = new FileWriter(archivoTemp)) {
				writer.write("Contenido confidencial del reporte generado por el sistema.");
			}

//			return archivoTemp;

		} catch (IOException exception) {
			exception.printStackTrace();
			throw new RuntimeException("Error al generar el archivo: " + exception.getMessage());
		}

		return null;
	}

	private void deleteFile(File archivoTemp) {
		if (archivoTemp != null && archivoTemp.exists()) {
			archivoTemp.delete();
		}
	}
}
