package mx.uv.fca.sistema_bibliotecario_interno.Controller;

import mx.uv.fca.sistema_bibliotecario_interno.model.ProgramaEducativo;
import mx.uv.fca.sistema_bibliotecario_interno.service.ProgramaEducativoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/programas")

public class ProgramaEducativoController {
    private final ProgramaEducativoService service;

    public ProgramaEducativoController(ProgramaEducativoService service) {
            this.service = service;
        }

        @GetMapping
        public List<ProgramaEducativo> listar() {
            return service.listar();
        }

        @GetMapping("/{id}")
        public ResponseEntity<ProgramaEducativo> buscar(@PathVariable Integer id) {
            return service.buscarPorId(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }

        @PostMapping
        public ProgramaEducativo guardar(
                @RequestBody ProgramaEducativo programa) {

            return service.guardar(programa);
        }

        @PutMapping("/{id}")
        public ResponseEntity<ProgramaEducativo> actualizar(
                @PathVariable Integer id,
                @RequestBody ProgramaEducativo programa) {

            return service.buscarPorId(id)
                    .map(programaExistente -> {
                        programaExistente.setNombre(programa.getNombre());
                        programaExistente.setAnio(programa.getAnio());
                        programaExistente.setNombreCorto(programa.getNombreCorto());

                        return ResponseEntity.ok(
                                service.guardar(programaExistente)
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