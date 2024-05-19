package com.example.notemanegersystem.service.email;

import com.example.notemanegersystem.entity.ConfirmEmail;
import com.example.notemanegersystem.exceptions.ConfirmEmailExpired;
import com.example.notemanegersystem.exceptions.DataNotFoundException;
import com.example.notemanegersystem.repository.ConfirmEmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ConfirmEmailService implements IConfirmEmailService{
    private final ConfirmEmailRepository confirmEmailRepository;
    private final JavaMailSender javaMailSender;

    @Override
    public void sendConfirmEmail(String email) {
        ConfirmEmail confirmEmail = ConfirmEmail.builder()
                .user(null)
                .code(generateConfirmCode())
                .requiredTime(LocalDateTime.now())
                .expiredTime(LocalDateTime.now().plusSeconds(120))
                .isConfirm(false)
                .build();
        confirmEmailRepository.save(confirmEmail);

        String subject = "Note System";
        String content = "Mã xác nhận của bạn là: " + confirmEmail.getCode();
        sendEmail(email, subject, content);
    }

    @Override
    public boolean confirmEmail(String confirmCode) throws Exception {
        ConfirmEmail confirmEmail = confirmEmailRepository.findConfirmEmailByCode(confirmCode);

        if (confirmEmail == null){
            throw new DataNotFoundException("mã xác minh không chính xác");
        }
        if (isExpired(confirmEmail)){
            throw new ConfirmEmailExpired("mã xác minh đã hết hạn");
        }
        confirmEmail.setIsConfirm(true);
        confirmEmailRepository.save(confirmEmail);
        return true;
    }
    private String generateConfirmCode() {
        Random random = new Random();
        int randomNumber = random.nextInt(900000) + 100000;
        return String.valueOf(randomNumber);
    }

    public void sendEmail(String to, String subject, String content){
        SimpleMailMessage msg =new SimpleMailMessage();
        msg.setFrom("ducpa2002@gmail.com");
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(content);
        javaMailSender.send(msg);
    }

    public boolean isExpired(ConfirmEmail confirmEmail) {
        return LocalDateTime.now().isAfter(confirmEmail.getExpiredTime());
    }
}
