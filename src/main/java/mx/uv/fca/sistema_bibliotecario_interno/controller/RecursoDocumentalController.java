package mx.uv.fca.sistema_bibliotecario_interno.controller;

import mx.uv.fca.sistema_bibliotecario_interno.dto.RecursoDocumentalRequestDTO;
import mx.uv.fca.sistema_bibliotecario_interno.model.RecursoDocumental;
import mx.uv.fca.sistema_bibliotecario_interno.service.RecursoDocumentalService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recursos")
public class RecursoDocumentalController {

    private final RecursoDocumentalService service;

    public RecursoDocumentalController(
            RecursoDocumentalService service) {

        this.service = service;
    }

    @GetMapping
    public List<RecursoDocumental> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecursoDocumental> buscar(
            @PathVariable Integer id) {

        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    public List<RecursoDocumental> buscar(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String autor,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String anio,
            @RequestParam(required = false) Integer idEditorial,
            @RequestParam(required = false) Boolean enCatalogo,
            @RequestParam(required = false) Integer idExperienciaEducativa,
            @RequestParam(required = false) Integer idAcademia,
            @RequestParam(required = false) Integer idProgramaEducativo) {

        return service.buscar(
                titulo,
                autor,
                isbn,
                tipo,
                anio,
                idEditorial,
                enCatalogo,
                idExperienciaEducativa,
                idAcademia,
                idProgramaEducativo
        );
    }

    // Registro original: se conserva temporalmente
    // para no afectar el formulario Angular actual.
    @PostMapping
    public RecursoDocumental guardar(
            @RequestBody RecursoDocumental recurso) {

        return service.guardar(recurso);
    }

    // Nuevo registro con editorial y vinculaciones
    // académicas específicas.
    @PostMapping("/registro")
    public ResponseEntity<RecursoDocumental> registrar(
            @RequestBody RecursoDocumentalRequestDTO request) {

        RecursoDocumental guardado =
                service.registrarConVinculaciones(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(guardado);
    }

    @PutMapping("/{id}/edicion")
    public ResponseEntity<RecursoDocumental> editarCompleto(
            @PathVariable Integer id,
            @RequestBody RecursoDocumentalRequestDTO request) {
        return service.actualizarConVinculaciones(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecursoDocumental> actualizar(
            @PathVariable Integer id,
            @RequestBody RecursoDocumental recurso) {

        return service.buscarPorId(id)
                .map(existente -> {

                    existente.setTitulo(recurso.getTitulo());
                    existente.setAutor(recurso.getAutor());
                    existente.setEdicion(recurso.getEdicion());
                    existente.setAnio(recurso.getAnio());
                    existente.setIsbn(recurso.getIsbn());
                    existente.setCantidad(recurso.getCantidad());
                    existente.setMovimiento(recurso.getMovimiento());

                    existente.setEnCatalogoBiblioFca(
                            recurso.getEnCatalogoBiblioFca()
                    );

                    existente.setObservaciones(
                            recurso.getObservaciones()
                    );

                    existente.setTipo(recurso.getTipo());
                    existente.setFormato(recurso.getFormato());
                    existente.setUrlRecurso("ELECTRONICO".equals(recurso.getFormato()) ? recurso.getUrlRecurso() : null);
                    existente.setEditorial(recurso.getEditorial());

                    existente.setExperienciasEducativas(
                            recurso.getExperienciasEducativas()
                    );

                    // Las nuevas vinculaciones académicas
                    // se conservan sin cambios en este endpoint.

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