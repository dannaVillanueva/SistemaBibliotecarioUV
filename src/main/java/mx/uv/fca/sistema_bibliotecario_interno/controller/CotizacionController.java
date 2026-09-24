package mx.uv.fca.sistema_bibliotecario_interno.controller;

import mx.uv.fca.sistema_bibliotecario_interno.model.Cotizacion;
import mx.uv.fca.sistema_bibliotecario_interno.service.CotizacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cotizaciones")
public class CotizacionController {

    private final CotizacionService service;

    public CotizacionController(CotizacionService service) {
        this.service = service;
    }


    @GetMapping
    public List<Cotizacion> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cotizacion> buscar(
            @PathVariable Integer id) {

        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Cotizacion guardar(
            @RequestBody Cotizacion cotizacion) {

        return service.guardar(cotizacion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cotizacion> actualizar(
            @PathVariable Integer id,
            @RequestBody Cotizacion cotizacion) {

        return service.buscarPorId(id)
                .map(existente -> {

                    existente.setFechaSolicitud(
                            cotizacion.getFechaSolicitud());

                    existente.setEstado(
                            cotizacion.getEstado());

                    existente.setObservaciones(
                            cotizacion.getObservaciones());

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