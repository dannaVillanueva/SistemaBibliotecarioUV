package mx.uv.fca.sistema_bibliotecario_interno.controller;

import mx.uv.fca.sistema_bibliotecario_interno.dto.UsuarioResponseDTO;
import mx.uv.fca.sistema_bibliotecario_interno.dto.UsuarioUpdateDTO;
import mx.uv.fca.sistema_bibliotecario_interno.model.Usuario;
import mx.uv.fca.sistema_bibliotecario_interno.service.UsuarioService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(
            UsuarioService service) {

        this.service = service;
    }

    @GetMapping
    public List<UsuarioResponseDTO> listar() {

        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscar(
            @PathVariable Integer id) {

        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(
                        ResponseEntity
                                .notFound()
                                .build()
                );
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> guardar(
            @RequestBody Usuario usuario) {

        UsuarioResponseDTO guardado =
                service.guardar(usuario);

        return ResponseEntity.ok(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizar(
            @PathVariable Integer id,
            @RequestBody UsuarioUpdateDTO datos) {

        return service.actualizar(id, datos)
                .map(ResponseEntity::ok)
                .orElse(
                        ResponseEntity
                                .notFound()
                                .build()
                );
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<UsuarioResponseDTO> cambiarEstado(
            @PathVariable Integer id,
            @RequestBody Map<String, Boolean> body) {

        Boolean activo =
                body.get("activo");

        if (activo == null) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        return service.cambiarEstado(id, activo)
                .map(ResponseEntity::ok)
                .orElse(
                        ResponseEntity
                                .notFound()
                                .build()
                );
    }
}