package mx.uv.fca.sistema_bibliotecario_interno.controller;

import mx.uv.fca.sistema_bibliotecario_interno.model.Donacion;
import mx.uv.fca.sistema_bibliotecario_interno.service.DonacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donaciones")
public class DonacionController {

    private final DonacionService service;

    public DonacionController(DonacionService service) {
        this.service = service;
    }

    @GetMapping
    public List<Donacion> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Donacion> buscar(@PathVariable Integer id) {

        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Donacion guardar(@RequestBody Donacion donacion) {
        return service.guardar(donacion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Donacion> actualizar(
            @PathVariable Integer id,
            @RequestBody Donacion donacion) {

        return service.buscarPorId(id)
                .map(existente -> {

                    existente.setNombreDonante(
                            donacion.getNombreDonante());

                    existente.setFechaDonacion(
                            donacion.getFechaDonacion());

                    existente.setPrecioEstimado(
                            donacion.getPrecioEstimado());

                    existente.setObservaciones(
                            donacion.getObservaciones());

                    existente.setRecursoDocumental(
                            donacion.getRecursoDocumental());

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