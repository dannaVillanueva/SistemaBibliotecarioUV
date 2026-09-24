package mx.uv.fca.sistema_bibliotecario_interno.service;

import mx.uv.fca.sistema_bibliotecario_interno.dto.LoginRequest;
import mx.uv.fca.sistema_bibliotecario_interno.dto.LoginResponse;
import mx.uv.fca.sistema_bibliotecario_interno.model.Usuario;
import mx.uv.fca.sistema_bibliotecario_interno.repository.UsuarioRepository;
import mx.uv.fca.sistema_bibliotecario_interno.security.JwtService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public Optional<LoginResponse> login(
            LoginRequest request) {

        Optional<Usuario> usuarioOptional =
                usuarioRepository
                        .findByCorreo(request.getCorreo());

        if (usuarioOptional.isEmpty()) {
            return Optional.empty();
        }

        Usuario usuario =
                usuarioOptional.get();

        if (usuario.getActivo() == null
                || !usuario.getActivo()) {

            return Optional.empty();
        }

        boolean passwordCorrecto =
                passwordEncoder.matches(
                        request.getPassword(),
                        usuario.getPassword()
                );

        if (!passwordCorrecto) {
            return Optional.empty();
        }

        String token =
                jwtService.generarToken(usuario);

        LoginResponse response =
                new LoginResponse(
                        usuario.getIdUsuario(),
                        usuario.getNombre(),
                        usuario.getCorreo(),
                        usuario.getRol().name(),
                        "Inicio de sesión correcto",
                        token
                );

        return Optional.of(response);
    }
}