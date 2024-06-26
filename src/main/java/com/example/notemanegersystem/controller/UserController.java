package com.example.notemanegersystem.controller;

import com.example.notemanegersystem.dtos.*;
import com.example.notemanegersystem.entity.ConfirmEmail;
import com.example.notemanegersystem.exceptions.DataNotFoundException;
import com.example.notemanegersystem.repository.ConfirmEmailRepository;
import com.example.notemanegersystem.service.email.ConfirmEmailService;
import com.example.notemanegersystem.service.user.UserService;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {
    private final UserService userService;
    private final ConfirmEmailService confirmEmailService;
    private final ConfirmEmailRepository confirmEmailRepository;
    private final FreeMarkerConfigurer freemarkerConfig;
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
    @GetMapping("{userId}")
    public ResponseEntity<?> getInforUser(@PathVariable Integer userId) {
        try {
            UserDTO userDTO = userService.getInforUser(userId);

            // Tạo model dữ liệu
            Map<String, Object> model = new HashMap<>();
            model.put("user", userDTO);

            // Load template
            Template template = freemarkerConfig.getConfiguration().getTemplate("use-template.ftl");

            // Merge dữ liệu với template
            String jsonResponse = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

            return ResponseEntity.ok().body(jsonResponse);
        } catch (IOException | TemplateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (DataNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @PatchMapping("{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Integer userId, @RequestBody UserDTO userDTO){
        try {
            // Load JSON schema from resources
            InputStream schemaStream = new ClassPathResource("json/user-scheme.json").getInputStream();
            JSONObject jsonSchema = new JSONObject(new JSONTokener(schemaStream));
            Schema schema = SchemaLoader.load(jsonSchema);

            // Convert UserDTO to JSONObject
            JSONObject requestBody = new JSONObject(userDTO);

            // Validate the request body against the schema
            schema.validate(requestBody);

            // If valid, proceed to update user information
            userService.updateUser(userId, userDTO);
            return ResponseEntity.ok().body("User updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
