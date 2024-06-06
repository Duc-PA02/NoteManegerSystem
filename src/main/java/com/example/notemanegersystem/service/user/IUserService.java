package com.example.notemanegersystem.service.user;

import com.example.notemanegersystem.dtos.LoginRequest;
import com.example.notemanegersystem.dtos.LoginResponse;
import com.example.notemanegersystem.dtos.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.net.http.HttpRequest;

public interface IUserService {
    String login(LoginRequest loginRequest) throws Exception;
    void saveUser(RegisterRequest registerRequest);
    String register(RegisterRequest registerRequest) throws Exception;
}
