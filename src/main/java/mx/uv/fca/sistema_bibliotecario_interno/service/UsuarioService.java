
package mx.uv.fca.sistema_bibliotecario_interno.service;

import mx.uv.fca.sistema_bibliotecario_interno.dto.UsuarioResponseDTO;
import mx.uv.fca.sistema_bibliotecario_interno.dto.UsuarioUpdateDTO;
import mx.uv.fca.sistema_bibliotecario_interno.model.Usuario;
import mx.uv.fca.sistema_bibliotecario_interno.repository.UsuarioRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository repository,
            PasswordEncoder passwordEncoder) {

        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UsuarioResponseDTO> listar() {

        return repository.findAll()
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    public Optional<UsuarioResponseDTO> buscarPorId(Integer id) {

        return repository.findById(id)
                .map(this::convertirDTO);
    }

    public UsuarioResponseDTO guardar(Usuario usuario) {

        if (repository.existsByCorreo(usuario.getCorreo())) {
            throw new RuntimeException(
                    "Ya existe un usuario con ese correo"
            );
        }

        usuario.setPassword(
                passwordEncoder.encode(
                        usuario.getPassword()
                )
        );

        if (usuario.getActivo() == null) {
            usuario.setActivo(true);
        }

        Usuario guardado = repository.save(usuario);

        return convertirDTO(guardado);
    }

    public Optional<UsuarioResponseDTO> actualizar(
            Integer id,
            UsuarioUpdateDTO datos) {

        Optional<Usuario> usuarioOptional =
                repository.findById(id);

        if (usuarioOptional.isEmpty()) {
            return Optional.empty();
        }

        Usuario usuario = usuarioOptional.get();

        usuario.setNombre(datos.getNombre());
        usuario.setCorreo(datos.getCorreo());
        usuario.setRol(datos.getRol());
        usuario.setActivo(datos.getActivo());

        Usuario actualizado = repository.save(usuario);

        return Optional.of(
                convertirDTO(actualizado)
        );
    }

    public Optional<UsuarioResponseDTO> cambiarEstado(
            Integer id,
            Boolean activo) {

        Optional<Usuario> usuarioOptional =
                repository.findById(id);

        if (usuarioOptional.isEmpty()) {
            return Optional.empty();
        }

        Usuario usuario = usuarioOptional.get();

        usuario.setActivo(activo);

        Usuario actualizado = repository.save(usuario);

        return Optional.of(
                convertirDTO(actualizado)
        );
    }

    private UsuarioResponseDTO convertirDTO(
            Usuario usuario) {

        return new UsuarioResponseDTO(usuario);
    }
}