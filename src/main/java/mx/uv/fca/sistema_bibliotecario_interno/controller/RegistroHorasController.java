package mx.uv.fca.sistema_bibliotecario_interno.controller;

import mx.uv.fca.sistema_bibliotecario_interno.dto.RegistroHorasRequestDTO;
import mx.uv.fca.sistema_bibliotecario_interno.dto.RegistroHorasResponseDTO;
import mx.uv.fca.sistema_bibliotecario_interno.dto.ResumenHorasDTO;

import mx.uv.fca.sistema_bibliotecario_interno.service.RegistroHorasService;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/registros-horas")
public class RegistroHorasController {

    private final RegistroHorasService service;

    public RegistroHorasController(
            RegistroHorasService service) {
        this.service = service;
    }

    // ==========================================
    // REGISTRAR NUEVO TURNO
    // ==========================================

    @PostMapping
    public RegistroHorasResponseDTO registrar(
            @RequestBody RegistroHorasRequestDTO request) {

        return service.registrar(request);
    }

    // ==========================================
    // ACTUALIZAR TURNO
    // ==========================================

    @PutMapping("/{id}")
    public RegistroHorasResponseDTO actualizar(
            @PathVariable Integer id,
            @RequestBody RegistroHorasRequestDTO request) {

        return service.actualizar(id, request);
    }

    // ==========================================
    // HISTORIAL DEL ALUMNO
    // ==========================================

    @GetMapping("/alumno/{idAlumno}")
    public List<RegistroHorasResponseDTO> historial(
            @PathVariable Integer idAlumno) {

        return service.historial(idAlumno);
    }

    // ==========================================
    // CONSULTAR POR RANGO
    // ==========================================

    @GetMapping("/alumno/{idAlumno}/rango")
    public List<RegistroHorasResponseDTO> buscarPorRango(
            @PathVariable Integer idAlumno,
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fin) {

        return service.buscarPorRango(
                idAlumno,
                inicio,
                fin
        );
    }

    // ==========================================
    // CONSULTAR POR MES
    // ==========================================

    @GetMapping("/alumno/{idAlumno}/mes")
    public List<RegistroHorasResponseDTO> buscarPorMes(
            @PathVariable Integer idAlumno,
            @RequestParam Integer anio,
            @RequestParam Integer mes) {

        return service.buscarPorMes(
                idAlumno,
                anio,
                mes
        );
    }

    // ==========================================
    // RESUMEN DE HORAS
    // ==========================================

    @GetMapping("/alumno/{idAlumno}/resumen")
    public ResumenHorasDTO resumen(
            @PathVariable Integer idAlumno) {

        return service.resumen(idAlumno);
    }
}