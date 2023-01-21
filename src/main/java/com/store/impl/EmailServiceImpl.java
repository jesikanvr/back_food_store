package com.store.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.store.dto.ChangePasswordDTO;
import com.store.dto.EmailValuesDTO;
import com.store.entitys.User;
import com.store.exceptions.ResourceNotFoundException;
import com.store.repository.UsersRepository;
import com.store.service.EmailService;
import com.store.utils.ConstantsApp;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine engine;

    @Value("${mail.urlFront}")
    private String urlFront;

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Value("${mail.subject}")
    private String mailSubject;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public void sendMail(EmailValuesDTO dto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(ConstantsApp.MY_EMAIL);
        message.setTo(dto.getEmailTo());
        message.setSubject(dto.getSubject());
        message.setText(dto.getContent());
        javaMailSender.send(message);
    }

    @Override
    public void sendEmailTemplate(EmailValuesDTO dto) {
        User user = usersRepository.findByUserOrEmail(dto.getEmailTo(), dto.getEmailTo())
                .orElseThrow(() -> new ResourceNotFoundException("User", "name", null));
        dto.setEmailFrom(emailFrom);
        dto.setEmailTo(user.getEmail());
        dto.setSubject(mailSubject);
        dto.setUserName(user.getName() + " " + user.getLast_name());
        UUID uuid = UUID.randomUUID();
        String token = uuid.toString();
        dto.setTokenPassword(token);
        user.setToken_pass(token);
        usersRepository.save(user);
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            Context context = new Context();
            Map<String, Object> model = new HashMap<>();
            model.put("userName", dto.getUserName());
            model.put("url", urlFront + dto.getTokenPassword());
            context.setVariables(model);
            String htmlText = engine.process("email-template", context);
            helper.setFrom(dto.getEmailFrom());
            helper.setTo(dto.getEmailTo());
            helper.setSubject(dto.getSubject());
            helper.setText(htmlText, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            System.out.println(e);
        }
    }

    @Override
    public String changePassword(ChangePasswordDTO dto) {
        Optional<User> userOptional = usersRepository.findByToken(dto.getTokenPassword());
        User user = userOptional.get();
        String newPassword = encoder.encode(dto.getPassword());
        user.setPass(newPassword);
        user.setToken_pass(null);
        usersRepository.save(user);
        return "Changed password";
    }

}
