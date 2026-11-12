package mx.uv.fca.sistema_bibliotecario_interno.controller;

import mx.uv.fca.sistema_bibliotecario_interno.dto.CotizacionCompletaDTO;
import mx.uv.fca.sistema_bibliotecario_interno.model.Cotizacion;
import mx.uv.fca.sistema_bibliotecario_interno.model.DetalleCotizacion;
import mx.uv.fca.sistema_bibliotecario_interno.model.HistorialCotizacion;
import mx.uv.fca.sistema_bibliotecario_interno.service.CotizacionService;
import mx.uv.fca.sistema_bibliotecario_interno.service.HistorialCotizacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Locale;

@RestController
@RequestMapping("/api/cotizaciones")
public class CotizacionController {
    private final CotizacionService service;
    private final HistorialCotizacionService historial;

    public CotizacionController(CotizacionService service, HistorialCotizacionService historial) {
        this.service = service;
        this.historial = historial;
    }

    @GetMapping
    public List<Cotizacion> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public ResponseEntity<Cotizacion> buscar(@PathVariable Integer id) {
        return service.buscarPorId(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/detalles")
    public ResponseEntity<List<DetalleCotizacion>> listarDetalles(@PathVariable Integer id) {
        if (service.buscarPorId(id).isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(service.obtenerDetalles(id));
    }

    @GetMapping("/historial")
    public List<HistorialCotizacion> historialGeneral() { return historial.listar(); }

    @GetMapping("/{id}/historial")
    public List<HistorialCotizacion> historialPorCotizacion(@PathVariable Integer id) {
        // Funciona también si la cotización fue eliminada.
        return historial.listarPorCotizacion(id);
    }

    @PostMapping
    public ResponseEntity<Cotizacion> guardar(@RequestBody Cotizacion cotizacion) {
        cotizacion.setIdCotizacion(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(cotizacion));
    }

    @PostMapping("/completa")
    public ResponseEntity<Cotizacion> guardarCompleta(@RequestBody CotizacionCompletaDTO solicitud) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardarCotizacionCompleta(solicitud));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cotizacion> actualizar(@PathVariable Integer id, @RequestBody Cotizacion cotizacion) {
        return service.actualizarCabecera(id, cotizacion).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/completa")
    public ResponseEntity<Cotizacion> actualizarCompleta(@PathVariable Integer id,
                                                           @RequestBody CotizacionCompletaDTO solicitud) {
        return service.actualizarCompleta(id, solicitud).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Cotizacion> cambiarEstado(@PathVariable Integer id,
                                                     @RequestBody Map<String, String> datos) {
        String estado = datos == null ? null : datos.get("estado");
        if (estado == null) return ResponseEntity.badRequest().build();
        String normalizado = estado.trim().toLowerCase(Locale.ROOT);
        if (!List.of("aprobada", "rechazada").contains(normalizado))
            return ResponseEntity.badRequest().build();
        return service.cambiarEstado(id, normalizado).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (service.buscarPorId(id).isEmpty()) return ResponseEntity.notFound().build();
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
