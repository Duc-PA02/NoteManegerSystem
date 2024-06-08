package com.example.notemanegersystem.controller;

import com.example.notemanegersystem.dtos.LoginRequest;
import com.example.notemanegersystem.dtos.LoginResponse;
import com.example.notemanegersystem.dtos.RegisterRequest;
import com.example.notemanegersystem.dtos.SendEmail;
import com.example.notemanegersystem.entity.ConfirmEmail;
import com.example.notemanegersystem.repository.ConfirmEmailRepository;
import com.example.notemanegersystem.service.email.ConfirmEmailService;
import com.example.notemanegersystem.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {
    private final UserService userService;
    private final ConfirmEmailService confirmEmailService;
    private final ConfirmEmailRepository confirmEmailRepository;
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody SendEmail sendEmail){
        try {
            String msg = userService.register(sendEmail);
            return ResponseEntity.ok().body(msg);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @PostMapping("/confirm-register")
    public ResponseEntity<?> confirmRegister(@RequestParam String confirmCode, @RequestBody RegisterRequest registerRequest){
        try {
            boolean isConfirm = confirmEmailService.confirmEmail(confirmCode);
            ConfirmEmail confirmEmail = confirmEmailRepository.findConfirmEmailByCode(confirmCode);
            if (isConfirm){
                userService.saveUser(registerRequest);
                confirmEmailRepository.delete(confirmEmail);
            }
            return ResponseEntity.ok().body("Đăng ký thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        try {
             String token = userService.login(loginRequest);
            return ResponseEntity.ok().body(LoginResponse.builder()
                            .token(token)
                    .build());
        } catch (Exception e){
            return ResponseEntity.badRequest().body(LoginResponse.builder()
                            .token(null)
                    .build());
        }
    }
}
