package mx.uv.fca.sistema_bibliotecario_interno.controller;

import mx.uv.fca.sistema_bibliotecario_interno.model.ExperienciaEducativa;
import mx.uv.fca.sistema_bibliotecario_interno.service.ExperienciaEducativaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/experiencias")
public class ExperienciaEducativaController {

    private final ExperienciaEducativaService service;

    public ExperienciaEducativaController(
            ExperienciaEducativaService service) {
        this.service = service;
    }

    @GetMapping
    public List<ExperienciaEducativa> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExperienciaEducativa> buscar(
            @PathVariable Integer id) {

        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ExperienciaEducativa guardar(
            @RequestBody ExperienciaEducativa experiencia) {

        return service.guardar(experiencia);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExperienciaEducativa> actualizar(
            @PathVariable Integer id,
            @RequestBody ExperienciaEducativa experiencia) {

        return service.buscarPorId(id)
                .map(existente -> {

                    existente.setNombre(experiencia.getNombre());
                    existente.setDescripcion(experiencia.getDescripcion());
                    existente.setEstado(experiencia.getEstado());
                    existente.setAcademias(experiencia.getAcademias());

                    return ResponseEntity.ok(
                            service.guardar(existente)
                    );
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Integer id) {

        if (service.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        service.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}