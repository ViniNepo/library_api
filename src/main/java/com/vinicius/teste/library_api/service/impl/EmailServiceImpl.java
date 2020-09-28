package com.vinicius.teste.library_api.service.impl;

import com.vinicius.teste.library_api.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${application.mail.defeaut-remetente}")
    private String rementente;
    private JavaMailSender javaMailSender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendMails(String message, List<String> list) {
        String[] mails = list.toArray(new String[list.size()]);

        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setFrom(rementente);
        emailMessage.setSubject("livro atrasado");
        emailMessage.setText(message);
        emailMessage.setTo(mails);

        javaMailSender.send(emailMessage);
    }
}
