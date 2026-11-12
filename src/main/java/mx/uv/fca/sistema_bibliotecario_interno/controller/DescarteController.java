package mx.uv.fca.sistema_bibliotecario_interno.controller;

import mx.uv.fca.sistema_bibliotecario_interno.dto.DescarteRequestDTO;
import mx.uv.fca.sistema_bibliotecario_interno.dto.DescarteResponseDTO;
import mx.uv.fca.sistema_bibliotecario_interno.dto.DescarteResolucionDTO;
import mx.uv.fca.sistema_bibliotecario_interno.service.DescarteService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/descartes")
public class DescarteController {

    private final DescarteService service;

    public DescarteController(DescarteService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DescarteResponseDTO> registrar(
            @RequestBody DescarteRequestDTO datos) {
        return ResponseEntity.ok(service.registrar(datos));
    }

    @GetMapping
    public List<DescarteResponseDTO> listar() {
        return service.listar();
    }

    @GetMapping("/pendientes")
    public List<DescarteResponseDTO> pendientes() {
        return service.pendientes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DescarteResponseDTO> buscarPorId(
            @PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/recurso/{idRecurso}")
    public List<DescarteResponseDTO> buscarPorRecurso(
            @PathVariable Integer idRecurso) {
        return service.buscarPorRecurso(idRecurso);
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<DescarteResponseDTO> buscarPorUsuario(
            @PathVariable Integer idUsuario) {
        return service.buscarPorUsuario(idUsuario);
    }

    @PatchMapping("/{id}/aprobar")
    public ResponseEntity<DescarteResponseDTO> aprobar(
            @PathVariable Integer id,
            @RequestBody(required = false)
            DescarteResolucionDTO datos) {

        return ResponseEntity.ok(service.aprobar(id, datos));
    }

    @PatchMapping("/{id}/rechazar")
    public ResponseEntity<DescarteResponseDTO> rechazar(
            @PathVariable Integer id,
            @RequestBody(required = false)
            DescarteResolucionDTO datos) {

        return ResponseEntity.ok(service.rechazar(id, datos));
    }
}