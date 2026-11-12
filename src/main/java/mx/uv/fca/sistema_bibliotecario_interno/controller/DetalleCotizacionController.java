package mx.uv.fca.sistema_bibliotecario_interno.controller;

import mx.uv.fca.sistema_bibliotecario_interno.model.DetalleCotizacion;
import mx.uv.fca.sistema_bibliotecario_interno.service.DetalleCotizacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.time.LocalDate;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/detalles-cotizacion")
public class DetalleCotizacionController {
    private final DetalleCotizacionService service;
    public DetalleCotizacionController(DetalleCotizacionService service) { this.service = service; }
    @GetMapping public List<DetalleCotizacion> listar() { return service.listar(); }
    @GetMapping("/{id}") public ResponseEntity<DetalleCotizacion> buscar(@PathVariable Integer id) {
        return service.buscarPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/buscar") public List<DetalleCotizacion> buscar(
        @RequestParam(required = false) String estado,
        @RequestParam(required = false) LocalDate fecha,
        @RequestParam(required = false) Integer idRecursoDocumental,
        @RequestParam(required = false) Integer idProgramaEducativo,
        @RequestParam(required = false) Integer idAcademia,
        @RequestParam(required = false) String filu) {
        return service.buscar(estado, fecha, idRecursoDocumental, idProgramaEducativo, idAcademia, filu);
    }
    private void validarYCalcular(DetalleCotizacion d) {
        boolean existente = d.getRecursoDocumental() != null &&
            d.getRecursoDocumental().getIdRecursoDocumental() != null;
        boolean externo = d.getTituloExterno() != null && !d.getTituloExterno().isBlank();
        if (existente == externo) throw new IllegalArgumentException(
            "Indica un recurso existente o un título externo, pero no ambos.");
        if (d.getCotizacion() == null || d.getCotizacion().getIdCotizacion() == null)
            throw new IllegalArgumentException("La cotización es obligatoria.");
        if (d.getCantidad() == null || d.getCantidad() <= 0 || d.getPrecioUnitarioEstimado() == null ||
            d.getPrecioUnitarioEstimado().signum() < 0)
            throw new IllegalArgumentException("Cantidad y precio deben ser válidos.");
        d.setSubtotal(d.getPrecioUnitarioEstimado().multiply(BigDecimal.valueOf(d.getCantidad())));
        if (existente) {
            d.setTituloExterno(null); d.setAutorExterno(null); d.setIsbnExterno(null);
            d.setEditorialExterna(null); d.setEditorial(null); d.setEdicionExterna(null); d.setAnioExterno(null);
        } else { d.setRecursoDocumental(null); }
    }
    @PostMapping public DetalleCotizacion guardar(@RequestBody DetalleCotizacion d) {
        validarYCalcular(d);
        return service.guardar(d);
    }
    @PutMapping("/{id}") public ResponseEntity<DetalleCotizacion> actualizar(
        @PathVariable Integer id, @RequestBody DetalleCotizacion d) {
        return service.buscarPorId(id).map(actual -> {
            validarYCalcular(d);
            actual.setCantidad(d.getCantidad());
            actual.setPrecioUnitarioEstimado(d.getPrecioUnitarioEstimado());
            actual.setSubtotal(d.getSubtotal());
            actual.setFilu(d.getFilu());
            actual.setCotizacion(d.getCotizacion());
            actual.setRecursoDocumental(d.getRecursoDocumental());
            actual.setProgramaEducativo(d.getProgramaEducativo());
            actual.setAcademia(d.getAcademia());
            actual.setTituloExterno(d.getTituloExterno());
            actual.setAutorExterno(d.getAutorExterno());
            actual.setIsbnExterno(d.getIsbnExterno());
            actual.setEditorialExterna(d.getEditorialExterna());
            actual.setEditorial(d.getEditorial());
            actual.setUrlCotizacion(d.getUrlCotizacion());
            actual.setEstadoAdquisicion(d.getEstadoAdquisicion());
            actual.setEdicionExterna(d.getEdicionExterna());
            actual.setAnioExterno(d.getAnioExterno());
            return ResponseEntity.ok(service.guardar(actual));
        }).orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (service.buscarPorId(id).isEmpty()) return ResponseEntity.notFound().build();
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}