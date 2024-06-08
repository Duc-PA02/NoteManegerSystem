package com.example.notemanegersystem.service.user;

import com.example.notemanegersystem.component.JwtToken;
import com.example.notemanegersystem.dtos.LoginRequest;
import com.example.notemanegersystem.dtos.RegisterRequest;
import com.example.notemanegersystem.dtos.SendEmail;
import com.example.notemanegersystem.entity.User;
import com.example.notemanegersystem.exceptions.DataNotFoundException;
import com.example.notemanegersystem.repository.UserRepository;
import com.example.notemanegersystem.service.email.ConfirmEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.http.HttpRequest;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ConfirmEmailService confirmEmailService;
    private final JwtToken jwtToken;

    @Override
    public String login(LoginRequest loginRequest) throws Exception {
        User existingUser = userRepository.findByEmail(loginRequest.getEmail()).orElse(null);
        if (existingUser == null) {
            throw new DataNotFoundException("Email không tồn tại");
        }
        if (!passwordEncoder.matches(loginRequest.getPassword(), existingUser.getPassword())) {
            throw new BadCredentialsException("Sai mật khẩu");
        }

        // Tạo AuthenticationToken với thông tin của người dùng
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword(),
                existingUser.getAuthorities()
        );

        // Xác thực người dùng
        authenticationManager.authenticate(authRequest);
        String token = jwtToken.generateToken(existingUser);
        return token;
    }


    @Override
    public void saveUser(RegisterRequest registerRequest){
        User user = User.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .fullName(registerRequest.getFullName())
                .build();
        userRepository.save(user);
    }

    @Override
    public String register(SendEmail sendEmail) throws Exception {
        String email = sendEmail.getEmail();
        if (userRepository.existsByEmail(email)){
            throw new DataIntegrityViolationException("email đã tồn tại");
        }
        confirmEmailService.sendConfirmEmail(email);
        return "Mã xác minh đã được gửi đến email của bạn";
    }

}
