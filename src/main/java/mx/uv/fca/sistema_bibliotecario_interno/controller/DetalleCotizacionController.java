package mx.uv.fca.sistema_bibliotecario_interno.controller;

import mx.uv.fca.sistema_bibliotecario_interno.model.DetalleCotizacion;
import mx.uv.fca.sistema_bibliotecario_interno.service.DetalleCotizacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalles-cotizacion")
public class DetalleCotizacionController {

    private final DetalleCotizacionService service;

    public DetalleCotizacionController(
            DetalleCotizacionService service) {
        this.service = service;
    }

    @GetMapping
    public List<DetalleCotizacion> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleCotizacion> buscar(
            @PathVariable Integer id) {

        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public DetalleCotizacion guardar(
            @RequestBody DetalleCotizacion detalle) {

        return service.guardar(detalle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetalleCotizacion> actualizar(
            @PathVariable Integer id,
            @RequestBody DetalleCotizacion detalle) {

        return service.buscarPorId(id)
                .map(existente -> {

                    existente.setCantidad(
                            detalle.getCantidad());

                    existente.setPrecioUnitarioEstimado(
                            detalle.getPrecioUnitarioEstimado());

                    existente.setFilu(
                            detalle.getFilu());

                    existente.setCotizacion(
                            detalle.getCotizacion());

                    existente.setRecursoDocumental(
                            detalle.getRecursoDocumental());

                    existente.setProgramaEducativo(
                            detalle.getProgramaEducativo());

                    existente.setAcademia(
                            detalle.getAcademia());

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