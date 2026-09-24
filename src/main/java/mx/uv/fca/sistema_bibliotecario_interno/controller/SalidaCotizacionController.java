package mx.uv.fca.sistema_bibliotecario_interno.controller;

import mx.uv.fca.sistema_bibliotecario_interno.dto.CotizacionSalidaDTO;
import mx.uv.fca.sistema_bibliotecario_interno.service.SalidaCotizacionService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cotizaciones")
public class SalidaCotizacionController {

    private final SalidaCotizacionService service;

    public SalidaCotizacionController(
            SalidaCotizacionService service) {

        this.service = service;
    }

    @GetMapping("/{id}/salida")
    public CotizacionSalidaDTO obtenerSalida(
            @PathVariable Integer id) {

        return service.obtenerSalida(id);
    }
}