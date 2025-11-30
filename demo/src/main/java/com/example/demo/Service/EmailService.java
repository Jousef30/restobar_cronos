package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleEmail(String toEmail,
                                String subject,
                                String body
    ) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("pepespepe038@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);
try {
        emailSender.send(message);  // Intentamos enviar el correo
        System.out.println("Correo enviado a: " + toEmail);  // Confirmación en consola
    } catch (MailException e) {
        System.out.println("Error al enviar correo: " + e.getMessage());  // Imprimir el error
        e.printStackTrace();  // Mostrar detalles adicionales del error
    }


    }
    
    
}
