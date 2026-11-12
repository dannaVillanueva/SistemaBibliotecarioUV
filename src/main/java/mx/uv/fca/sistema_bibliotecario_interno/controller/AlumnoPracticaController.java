package mx.uv.fca.sistema_bibliotecario_interno.controller;

import mx.uv.fca.sistema_bibliotecario_interno.dto.AlumnoPracticaResponseDTO;

import mx.uv.fca.sistema_bibliotecario_interno.model.AlumnoPractica;
import mx.uv.fca.sistema_bibliotecario_interno.model.TipoPractica;

import mx.uv.fca.sistema_bibliotecario_interno.service.AlumnoPracticaService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alumnos-practicas")
public class AlumnoPracticaController {

    private final AlumnoPracticaService service;

    public AlumnoPracticaController(
            AlumnoPracticaService service) {

        this.service = service;
    }

    @GetMapping
    public List<AlumnoPracticaResponseDTO> listar() {

        return service.listar();
    }

    @GetMapping("/buscar")
    public List<AlumnoPracticaResponseDTO> buscar(
            @RequestParam(required = false)
            String matricula,

            @RequestParam(required = false)
            String nombre,

            @RequestParam(required = false)
            TipoPractica tipo,

            @RequestParam(required = false)
            Integer idProgramaEducativo,

            @RequestParam(required = false)
            Boolean activo) {

        return service.buscar(
                matricula,
                nombre,
                tipo,
                idProgramaEducativo,
                activo
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlumnoPracticaResponseDTO> buscarPorId(
            @PathVariable Integer id) {

        try {

            return ResponseEntity.ok(
                    service.buscarPorId(id)
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .notFound()
                    .build();
        }
    }

    @PostMapping
    public AlumnoPracticaResponseDTO guardar(
            @RequestBody AlumnoPractica alumno) {

        return service.guardar(
                alumno
        );
    }

    @PutMapping("/{id}")
    public AlumnoPracticaResponseDTO actualizar(
            @PathVariable Integer id,
            @RequestBody AlumnoPractica alumno) {

        return service.actualizar(
                id,
                alumno
        );
    }

    @PatchMapping("/{id}/estado")
    public AlumnoPracticaResponseDTO cambiarEstado(
            @PathVariable Integer id,
            @RequestParam Boolean activo) {

        return service.cambiarEstado(
                id,
                activo
        );
    }

    @PatchMapping("/{id}/finalizar")
    public AlumnoPracticaResponseDTO finalizar(
            @PathVariable Integer id) {

        return service.finalizar(
                id
        );
    }

    @PatchMapping("/{id}/reactivar")
    public AlumnoPracticaResponseDTO reactivar(
            @PathVariable Integer id) {

        return service.reactivar(
                id
        );
    }
}