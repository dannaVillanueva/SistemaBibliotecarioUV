package mx.uv.fca.sistema_bibliotecario_interno.controller;

import mx.uv.fca.sistema_bibliotecario_interno.model.Editorial;
import mx.uv.fca.sistema_bibliotecario_interno.service.EditorialService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/editoriales")
public class EditorialController {

    private final EditorialService service;

    public EditorialController(EditorialService service) {
        this.service = service;
    }

    @GetMapping
    public List<Editorial> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Editorial> buscar(@PathVariable Integer id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Editorial guardar(@RequestBody Editorial editorial) {
        return service.guardar(editorial);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Editorial> actualizar(
            @PathVariable Integer id,
            @RequestBody Editorial editorial) {

        return service.buscarPorId(id)
                .map(existente -> {

                    existente.setNombre(editorial.getNombre());
                    existente.setPaginaWeb(editorial.getPaginaWeb());

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