package mx.uv.fca.sistema_bibliotecario_interno.controller;

import mx.uv.fca.sistema_bibliotecario_interno.dto.LoginRequest;
import mx.uv.fca.sistema_bibliotecario_interno.dto.LoginResponse;
import mx.uv.fca.sistema_bibliotecario_interno.service.AuthService;

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
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request) {

        return authService.login(request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity
                        .status(401)
                        .build());
    }
}