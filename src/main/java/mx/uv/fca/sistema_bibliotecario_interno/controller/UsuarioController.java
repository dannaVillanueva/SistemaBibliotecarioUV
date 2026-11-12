package mx.uv.fca.sistema_bibliotecario_interno.controller;

import mx.uv.fca.sistema_bibliotecario_interno.model.Rol;
import mx.uv.fca.sistema_bibliotecario_interno.model.Usuario;
import mx.uv.fca.sistema_bibliotecario_interno.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;

    public UsuarioController(UsuarioRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public record UsuarioDatos(Integer idUsuario, String nombre, String correo, Rol rol, Boolean activo) {}
    public record UsuarioEntrada(String nombre, String correo, String password, Rol rol) {}
    public record UsuarioEdicion(String nombre, String correo, String password, Rol rol) {}

    private UsuarioDatos salida(Usuario u) {
        return new UsuarioDatos(u.getIdUsuario(), u.getNombre(), u.getCorreo(), u.getRol(), u.getActivo());
    }

    private String obligatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, campo + " es obligatorio");
        return valor.trim();
    }

    private String correo(String valor) {
        String v = obligatorio(valor, "Correo").toLowerCase(java.util.Locale.ROOT);
        if (!v.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Correo inválido");
        return v;
    }

    private Rol rol(Rol valor) {
        if (valor == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Selecciona un rol");
        return valor;
    }

    private String password(String valor) {
        if (valor == null || valor.length() < 8)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña debe tener al menos 8 caracteres");
        return valor;
    }

    private Usuario buscar(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    private void validarResponsables(Usuario u, Rol nuevoRol, boolean nuevoActivo) {
        if (u.getRol() == Rol.RESPONSABLE_BIBLIOTECA && Boolean.TRUE.equals(u.getActivo())
                && (nuevoRol != Rol.RESPONSABLE_BIBLIOTECA || !nuevoActivo)) {
            long responsables = repository.findAll().stream()
                .filter(x -> x.getRol() == Rol.RESPONSABLE_BIBLIOTECA && Boolean.TRUE.equals(x.getActivo())).count();
            if (responsables <= 1) throw new ResponseStatusException(HttpStatus.CONFLICT, "Debe existir al menos un responsable activo");
        }
    }

    @GetMapping
    public List<UsuarioDatos> listar() {
        return repository.findAll().stream().map(this::salida).toList();
    }

    @PostMapping
    public ResponseEntity<UsuarioDatos> crear(@RequestBody UsuarioEntrada datos) {
        String email = correo(datos.correo());
        if (repository.existsByCorreo(email)) throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya existe");
        Usuario u = new Usuario();
        u.setNombre(obligatorio(datos.nombre(), "Nombre"));
        u.setCorreo(email);
        u.setPassword(encoder.encode(password(datos.password())));
        u.setRol(rol(datos.rol()));
        u.setActivo(true);
        return ResponseEntity.status(HttpStatus.CREATED).body(salida(repository.save(u)));
    }

    @PutMapping("/{id}")
    public UsuarioDatos editar(@PathVariable Integer id, @RequestBody UsuarioEdicion datos, Authentication auth) {
        Usuario u = buscar(id);
        String email = correo(datos.correo());
        if (!email.equalsIgnoreCase(u.getCorreo()) && repository.existsByCorreo(email))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya existe");
        Rol nuevoRol = rol(datos.rol());
        if (u.getCorreo().equalsIgnoreCase(auth.getName()) && nuevoRol != Rol.RESPONSABLE_BIBLIOTECA)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No puedes cambiar tu propio rol");
        validarResponsables(u, nuevoRol, Boolean.TRUE.equals(u.getActivo()));
        u.setNombre(obligatorio(datos.nombre(), "Nombre"));
        u.setCorreo(email);
        u.setRol(nuevoRol);
        if (datos.password() != null && !datos.password().isBlank()) u.setPassword(encoder.encode(password(datos.password())));
        return salida(repository.save(u));
    }

    @PatchMapping("/{id}/estado")
    public UsuarioDatos estado(@PathVariable Integer id, @RequestBody Map<String, Boolean> datos, Authentication auth) {
        Usuario u = buscar(id);
        Boolean activo = datos.get("activo");
        if (activo == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Falta el estado");
        if (!activo && u.getCorreo().equalsIgnoreCase(auth.getName()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No puedes desactivar tu propia cuenta");
        validarResponsables(u, u.getRol(), activo);
        u.setActivo(activo);
        return salida(repository.save(u));
    }
}
