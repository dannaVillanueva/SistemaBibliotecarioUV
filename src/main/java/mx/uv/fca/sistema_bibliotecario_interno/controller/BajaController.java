package mx.uv.fca.sistema_bibliotecario_interno.controller;

import mx.uv.fca.sistema_bibliotecario_interno.dto.BajaRequestDTO;
import mx.uv.fca.sistema_bibliotecario_interno.dto.BajaResponseDTO;
import mx.uv.fca.sistema_bibliotecario_interno.service.BajaService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bajas")
public class BajaController {

    private final BajaService service;

    public BajaController(BajaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<BajaResponseDTO> registrar(
            @RequestBody BajaRequestDTO datos) {

        BajaResponseDTO baja = service.registrar(datos);

        return ResponseEntity.ok(baja);
    }

    @GetMapping
    public List<BajaResponseDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BajaResponseDTO> buscarPorId(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                service.buscarPorId(id)
        );
    }

    @GetMapping("/recurso/{idRecurso}")
    public List<BajaResponseDTO> buscarPorRecurso(
            @PathVariable Integer idRecurso) {

        return service.buscarPorRecurso(idRecurso);
    }
}