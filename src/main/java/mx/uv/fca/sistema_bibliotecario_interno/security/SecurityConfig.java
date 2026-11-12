package mx.uv.fca.sistema_bibliotecario_interno.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // Inicio de sesión público
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/auth/login"
                        ).permitAll()

                        // Solo el responsable puede administrar usuarios
                        .requestMatchers("/api/usuarios", "/api/usuarios/**")
                        .hasRole("RESPONSABLE_BIBLIOTECA")

                        // Consulta de catálogos
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/editoriales",
                                "/api/editoriales/**",
                                "/api/programas-educativos",
                                "/api/programas-educativos/**",
                                "/api/academias",
                                "/api/academias/**",
                                "/api/experiencias",
                                "/api/experiencias/**"
                        ).hasAnyRole(
                                "RESPONSABLE_BIBLIOTECA",
                                "PERSONAL_BIBLIOTECA"
                        )

                        // Resto de la API
                        .requestMatchers("/api/**")
                        .hasAnyRole(
                                "RESPONSABLE_BIBLIOTECA",
                                "PERSONAL_BIBLIOTECA"
                        )

                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}