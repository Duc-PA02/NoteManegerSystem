package com.example.notemanegersystem.service.user;

import com.example.notemanegersystem.dtos.*;
import com.example.notemanegersystem.entity.User;
import com.example.notemanegersystem.exceptions.DataNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.net.http.HttpRequest;

public interface IUserService {
    String login(LoginRequest loginRequest) throws Exception;
    void saveUser(RegisterRequest registerRequest);
    String register(SendEmail sendEmail) throws Exception;
    UserDTO getInforUser(Integer userId) throws DataNotFoundException;
    User updateUser(Integer userId, UserDTO userDTO) throws DataNotFoundException;
}
