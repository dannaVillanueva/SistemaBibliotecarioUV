package mx.uv.fca.sistema_bibliotecario_interno.controller;

import mx.uv.fca.sistema_bibliotecario_interno.model.Academia;
import mx.uv.fca.sistema_bibliotecario_interno.service.AcademiaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/academias")
public class AcademiaController {

    private final AcademiaService service;

    public AcademiaController(AcademiaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Academia> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Academia> buscar(@PathVariable Integer id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Academia guardar(@RequestBody Academia academia) {
        return service.guardar(academia);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Academia> actualizar(
            @PathVariable Integer id,
            @RequestBody Academia academia) {

        return service.buscarPorId(id)
                .map(existente -> {

                    existente.setProgramasEducativos(
                            academia.getProgramasEducativos()
                    );

                    return ResponseEntity.ok(
                            service.guardar(existente)
                    );
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {

        if (service.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        service.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}