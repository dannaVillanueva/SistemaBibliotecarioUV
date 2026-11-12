package mx.uv.fca.sistema_bibliotecario_interno.controller;

import mx.uv.fca.sistema_bibliotecario_interno.dto.DonacionRequest;
import mx.uv.fca.sistema_bibliotecario_interno.model.Donacion;
import mx.uv.fca.sistema_bibliotecario_interno.service.DonacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/donaciones")
public class DonacionController {
    private final DonacionService service;
    public DonacionController(DonacionService service) { this.service = service; }

    @GetMapping
    public List<Donacion> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public ResponseEntity<Donacion> buscar(@PathVariable Integer id) {
        return service.buscarPorId(id).map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    public List<Donacion> buscar(
        @RequestParam(required = false) String nombreDonante,
        @RequestParam(required = false) LocalDate fechaDonacion,
        @RequestParam(required = false) Integer idRecursoDocumental,
        @RequestParam(required = false) String titulo,
        @RequestParam(required = false) String isbn,
        @RequestParam(required = false) BigDecimal precioMinimo,
        @RequestParam(required = false) BigDecimal precioMaximo) {
        return service.buscar(nombreDonante, fechaDonacion, idRecursoDocumental,
            titulo, isbn, precioMinimo, precioMaximo);
    }

    @PostMapping
    public Donacion crear(@RequestBody DonacionRequest request) {
        return service.crear(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Donacion> actualizar(@PathVariable Integer id,
                                               @RequestBody DonacionRequest request) {
        return service.buscarPorId(id).isEmpty() ? ResponseEntity.notFound().build()
            : ResponseEntity.ok(service.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (service.buscarPorId(id).isEmpty()) return ResponseEntity.notFound().build();
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
