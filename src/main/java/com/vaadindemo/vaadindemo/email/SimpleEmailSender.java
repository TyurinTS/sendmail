package com.vaadindemo.vaadindemo.email;

import com.vaadindemo.vaadindemo.entities.PersonData;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class SimpleEmailSender {

    private JavaMailSender mailSender;


    public SimpleEmailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void SendSimpleEmail(List<PersonData> senderList) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        senderList.stream().forEach(s -> {
            mailMessage.setTo("timt@mail.ru");
            mailMessage.setSubject("Пример");
            mailMessage.setText("Hello");
            mailSender.send(mailMessage);
        });
    }
}
