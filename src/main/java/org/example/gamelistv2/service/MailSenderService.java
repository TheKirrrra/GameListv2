package org.example.gamelistv2.service;

public interface MailSenderService {

    void send(String emailTo, String subject, String message);
}
