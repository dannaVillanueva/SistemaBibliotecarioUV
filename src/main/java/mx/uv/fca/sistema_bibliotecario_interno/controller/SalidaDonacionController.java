package mx.uv.fca.sistema_bibliotecario_interno.controller;

import mx.uv.fca.sistema_bibliotecario_interno.dto.DonacionSalidaDTO;
import mx.uv.fca.sistema_bibliotecario_interno.service.SalidaDonacionService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/donaciones/salida")
public class SalidaDonacionController {

    private final SalidaDonacionService service;

    public SalidaDonacionController(
            SalidaDonacionService service) {

        this.service = service;
    }

    @GetMapping
    public List<DonacionSalidaDTO> listar() {

        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DonacionSalidaDTO> buscarPorId(
            @PathVariable Integer id) {

        try {

            DonacionSalidaDTO donacion =
                    service.buscarPorId(id);

            return ResponseEntity.ok(donacion);

        } catch (RuntimeException e) {

            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/buscar")
    public List<DonacionSalidaDTO> buscar(
            @RequestParam(required = false) String nombreDonante,
            @RequestParam(required = false) LocalDate fechaDonacion,
            @RequestParam(required = false) Integer idRecursoDocumental,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) BigDecimal precioMinimo,
            @RequestParam(required = false) BigDecimal precioMaximo) {

        return service.buscar(
                nombreDonante,
                fechaDonacion,
                idRecursoDocumental,
                titulo,
                isbn,
                precioMinimo,
                precioMaximo
        );
    }
}