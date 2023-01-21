package com.store.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.store.dto.EmailValuesDTO;

@SpringBootTest
public class TestEmail {

    @Autowired
    private EmailService emailService;

    @Test
    public void sendEmailTest(){
        EmailValuesDTO dto = new EmailValuesDTO(null, "leandrojdvr@estudiantes.uci.cu", "Prueba de envio de correo", "Este es un correo de prueba", "Leandro");
        emailService.sendMail(dto);
    }
    
}
