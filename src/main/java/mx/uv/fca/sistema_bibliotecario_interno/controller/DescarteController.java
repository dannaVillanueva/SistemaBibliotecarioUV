package mx.uv.fca.sistema_bibliotecario_interno.controller;

import mx.uv.fca.sistema_bibliotecario_interno.dto.DescarteRequestDTO;
import mx.uv.fca.sistema_bibliotecario_interno.dto.DescarteResponseDTO;
import mx.uv.fca.sistema_bibliotecario_interno.service.DescarteService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/descartes")
public class DescarteController {

    private final DescarteService service;

    public DescarteController(
            DescarteService service) {

        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DescarteResponseDTO> registrar(
            @RequestBody DescarteRequestDTO datos) {

        DescarteResponseDTO descarte =
                service.registrar(datos);

        return ResponseEntity.ok(descarte);
    }

    @GetMapping
    public List<DescarteResponseDTO> listar() {

        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DescarteResponseDTO> buscarPorId(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                service.buscarPorId(id)
        );
    }

    @GetMapping("/recurso/{idRecurso}")
    public List<DescarteResponseDTO> buscarPorRecurso(
            @PathVariable Integer idRecurso) {

        return service.buscarPorRecurso(
                idRecurso
        );
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<DescarteResponseDTO> buscarPorUsuario(
            @PathVariable Integer idUsuario) {

        return service.buscarPorUsuario(
                idUsuario
        );
    }
}