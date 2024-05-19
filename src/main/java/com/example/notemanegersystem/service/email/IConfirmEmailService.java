package com.example.notemanegersystem.service.email;

public interface IConfirmEmailService {
    void sendConfirmEmail(String email);
    boolean confirmEmail(String confirmCode) throws Exception;
}
