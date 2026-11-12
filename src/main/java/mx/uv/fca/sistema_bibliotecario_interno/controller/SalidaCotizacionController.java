package mx.uv.fca.sistema_bibliotecario_interno.controller;

import mx.uv.fca.sistema_bibliotecario_interno.dto.CotizacionSalidaDTO;
import mx.uv.fca.sistema_bibliotecario_interno.service.SalidaCotizacionService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/cotizaciones")
public class SalidaCotizacionController {

    private final SalidaCotizacionService service;

    public SalidaCotizacionController(
            SalidaCotizacionService service) {
        this.service = service;
    }

    // Obtener la salida completa de una cotización por ID
    @GetMapping("/{id}/salida")
    public CotizacionSalidaDTO obtenerSalida(
            @PathVariable Integer id) {

        return service.obtenerSalida(id);
    }

    // Buscar cotizaciones con filtros
    @GetMapping("/buscar")
    public List<CotizacionSalidaDTO> buscar(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) LocalDate fecha,
            @RequestParam(required = false) Integer idRecursoDocumental,
            @RequestParam(required = false) Integer idProgramaEducativo,
            @RequestParam(required = false) Integer idAcademia,
            @RequestParam(required = false) String filu) {

        return service.buscarCotizaciones(
                estado,
                fecha,
                idRecursoDocumental,
                idProgramaEducativo,
                idAcademia,
                filu
        );
    }
}