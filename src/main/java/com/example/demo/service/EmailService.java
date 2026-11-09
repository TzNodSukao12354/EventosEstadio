package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarConfirmacionCompra(String correoDestino, String nombreUsuario, String nombreEvento, int idEntrada) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("raznakrotherrol@gmail.com");
        message.setTo(correoDestino);
        message.setSubject("✅ Confirmación de Compra - " + nombreEvento);

        String cuerpoCorreo = "Hola " + nombreUsuario + ",\n\n" +
                "¡Tu compra fue exitosa!\n" +
                "Evento: " + nombreEvento + "\n" +
                "ID de tu Entrada (E-TICKET): " + idEntrada + "\n\n" +
                "IMPORTANTE: Presenta este ID o código QR en la entrada.\n" +
                "Recuerda revisar el reglamento del estadio (no se permite reingreso, mochilas grandes, etc.).\n\n" +
                "¡Gracias por tu compra!";

        message.setText(cuerpoCorreo);
        mailSender.send(message);
    }
}