package com.store.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.store.dto.ChangePasswordDTO;
import com.store.dto.EmailValuesDTO;
import com.store.service.EmailService;

@RestController
@RequestMapping("/api/auth/email")
public class EmailController {

    @Autowired
    private EmailService service;

    @PostMapping("/send-email")
    public ResponseEntity<?> sendEmail_Template(@RequestBody EmailValuesDTO dto) {
        service.sendEmailTemplate(dto);
        return new ResponseEntity<>("Correo con plantilla enviado", HttpStatus.OK);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDTO dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            return new ResponseEntity<>("Las contraseñas no cohinciden", HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(service.changePassword(dto), HttpStatus.OK);
    }

}
