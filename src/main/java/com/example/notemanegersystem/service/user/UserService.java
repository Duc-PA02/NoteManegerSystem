package com.example.notemanegersystem.service.user;

import com.example.notemanegersystem.dtos.LoginRequest;
import com.example.notemanegersystem.dtos.RegisterRequest;
import com.example.notemanegersystem.entity.User;
import com.example.notemanegersystem.exceptions.DataNotFoundException;
import com.example.notemanegersystem.repository.UserRepository;
import com.example.notemanegersystem.service.email.ConfirmEmailService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import java.net.http.HttpRequest;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ConfirmEmailService confirmEmailService;

    @Override
    public void login(LoginRequest loginRequest, HttpServletRequest request) throws Exception {
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
                loginRequest.getPassword()
        );

        // Xác thực người dùng
        Authentication authResult = authenticationManager.authenticate(authRequest);

        // Kiểm tra xem xác thực đã thành công và lưu vào SecurityContextHolder
        if (authResult.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authResult);
            // Lưu thông tin email vào session
            HttpSession session = request.getSession();
            session.setAttribute("userEmail", loginRequest.getEmail());
            System.out.println("Email đã được lưu vào session: " + session.getAttribute("userEmail"));
        } else {
            throw new Exception("Xác thực không thành công");
        }
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
    public String register(RegisterRequest registerRequest) throws Exception {
        String email = registerRequest.getEmail();
        if (userRepository.existsByEmail(email)){
            throw new DataIntegrityViolationException("email đã tồn tại");
        }
        confirmEmailService.sendConfirmEmail(email);
        return "Mã xác minh đã được gửi đến email của bạn";
    }

    @Override
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // Lấy session hiện tại của người dùng
        HttpSession session = request.getSession(false);

        // Kiểm tra xem session có tồn tại hay không
        if (session != null) {
            System.out.println("Email sẽ được xóa khỏi session: " + session.getAttribute("userEmail"));
            // Xóa thông tin đăng nhập khỏi session
            session.removeAttribute("userEmail");
        }

        // Lấy thông tin xác thực từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kiểm tra xem người dùng đã được xác thực hay chưa
        if (authentication == null || !authentication.isAuthenticated()) {
            return "Ban chua dang nhap";
        }

        // Khởi tạo SecurityContextLogoutHandler
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

        // Xử lý đăng xuất
        logoutHandler.logout(request, response, authentication);

        return "Dang xuat thanh cong";
    }
}