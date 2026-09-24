package mx.uv.fca.sistema_bibliotecario_interno.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import mx.uv.fca.sistema_bibliotecario_interno.model.Usuario;

import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

      private static final String SECRET_KEY =
            "SistemaBibliotecarioFCA2026ClaveJWTMuySegura123456789";

    private static final long EXPIRACION =
            1000L * 60 * 60 * 8; // 8 horas

    private SecretKey getKey() {

        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String generarToken(Usuario usuario) {

        Date ahora = new Date();

        Date expiracion =
                new Date(ahora.getTime() + EXPIRACION);

        return Jwts.builder()
                .subject(usuario.getCorreo())
                .claim(
                        "idUsuario",
                        usuario.getIdUsuario()
                )
                .claim(
                        "nombre",
                        usuario.getNombre()
                )
                .claim(
                        "rol",
                        usuario.getRol().name()
                )
                .issuedAt(ahora)
                .expiration(expiracion)
                .signWith(getKey())
                .compact();
    }

    public String obtenerCorreo(String token) {

        return obtenerClaims(token)
                .getSubject();
    }

    public String obtenerRol(String token) {

        return obtenerClaims(token)
                .get("rol", String.class);
    }

    public boolean tokenValido(String token) {

        try {

            Claims claims = obtenerClaims(token);

            return claims.getExpiration()
                    .after(new Date());

        } catch (Exception e) {

            return false;
        }
    }

    private Claims obtenerClaims(String token) {

        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}