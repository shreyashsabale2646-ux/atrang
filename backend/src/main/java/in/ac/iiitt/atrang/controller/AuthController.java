package in.ac.iiitt.atrang.controller;

import in.ac.iiitt.atrang.dto.AdminUserDto;
import in.ac.iiitt.atrang.dto.ApiResponse;
import in.ac.iiitt.atrang.dto.LoginRequest;
import in.ac.iiitt.atrang.dto.LoginResponse;
import in.ac.iiitt.atrang.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        LoginResponse response = authService.login(request, httpRequest);
        return ResponseEntity.ok(ApiResponse.ok("Login successful", response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(HttpServletRequest httpRequest) {
        authService.logout(httpRequest);
        return ResponseEntity.ok(ApiResponse.ok("Logged out successfully", "Session invalidated"));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AdminUserDto>> me() {
        AdminUserDto user = authService.getCurrentUser();
        return ResponseEntity.ok(ApiResponse.ok(user));
    }
}
