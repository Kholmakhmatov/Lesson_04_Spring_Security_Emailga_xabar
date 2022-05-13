package uz.kholmakhmatov.lesson_04_spring_security_emailga_xabar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.kholmakhmatov.lesson_04_spring_security_emailga_xabar.dto.ApiResponse;
import uz.kholmakhmatov.lesson_04_spring_security_emailga_xabar.dto.LoginDto;
import uz.kholmakhmatov.lesson_04_spring_security_emailga_xabar.dto.RegisterDto;
import uz.kholmakhmatov.lesson_04_spring_security_emailga_xabar.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public HttpEntity<?> registerUser(@RequestBody RegisterDto dto) {
        ApiResponse apiResponse = authService.registerUser(dto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);

    }

    @GetMapping("/verifyEmail")
    public HttpEntity<?> verifyEmail(@RequestParam String emailCode, @RequestParam String email) {
        ApiResponse apiResponse = authService.verifyEmail(emailCode, email);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PostMapping("/login")
    public HttpEntity<?> login(@RequestBody LoginDto loginDto) {
        ApiResponse apiResponse = authService.login(loginDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 401).body(apiResponse);

    }
}
