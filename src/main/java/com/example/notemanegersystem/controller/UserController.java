package com.example.notemanegersystem.controller;

import com.example.notemanegersystem.dtos.LoginRequest;
import com.example.notemanegersystem.dtos.RegisterRequest;
import com.example.notemanegersystem.entity.ConfirmEmail;
import com.example.notemanegersystem.exceptions.DataNotFoundException;
import com.example.notemanegersystem.repository.ConfirmEmailRepository;
import com.example.notemanegersystem.service.email.ConfirmEmailService;
import com.example.notemanegersystem.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {
    private final UserService userService;
    private final ConfirmEmailService confirmEmailService;
    private final ConfirmEmailRepository confirmEmailRepository;
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest){
        try {
            String msg = userService.register(registerRequest);
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
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request){
        try {
             userService.login(loginRequest, request);
            return ResponseEntity.ok().body(loginRequest);
        } catch (DataNotFoundException e) {
            // Email không tồn tại
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email không tồn tại");
        } catch (AuthenticationException e) {
            // Sai mật khẩu hoặc thông tin đăng nhập không hợp lệ
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai mật khẩu hoặc thông tin đăng nhập không hợp lệ");
        } catch (Exception e) {
            //lỗi khác do serve
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            String msg = userService.logout(request, response);
            return ResponseEntity.ok(msg);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
