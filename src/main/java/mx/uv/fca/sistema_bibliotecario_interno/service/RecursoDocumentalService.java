package mx.uv.fca.sistema_bibliotecario_interno.service;

import mx.uv.fca.sistema_bibliotecario_interno.dto.RecursoDocumentalRequestDTO;
import mx.uv.fca.sistema_bibliotecario_interno.dto.VinculacionAcademicaRequestDTO;

import mx.uv.fca.sistema_bibliotecario_interno.model.Academia;
import mx.uv.fca.sistema_bibliotecario_interno.model.Editorial;
import mx.uv.fca.sistema_bibliotecario_interno.model.ExperienciaEducativa;
import mx.uv.fca.sistema_bibliotecario_interno.model.ProgramaEducativo;
import mx.uv.fca.sistema_bibliotecario_interno.model.RecursoDocumental;
import mx.uv.fca.sistema_bibliotecario_interno.model.VinculacionAcademicaRecurso;

import mx.uv.fca.sistema_bibliotecario_interno.repository.AcademiaRepository;
import mx.uv.fca.sistema_bibliotecario_interno.repository.EditorialRepository;
import mx.uv.fca.sistema_bibliotecario_interno.repository.ExperienciaEducativaRepository;
import mx.uv.fca.sistema_bibliotecario_interno.repository.ProgramaEducativoRepository;
import mx.uv.fca.sistema_bibliotecario_interno.repository.RecursoDocumentalRepository;

import mx.uv.fca.sistema_bibliotecario_interno.specification.RecursoDocumentalSpecification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RecursoDocumentalService {

    private final RecursoDocumentalRepository repository;
    private final EditorialRepository editorialRepository;
    private final ProgramaEducativoRepository programaRepository;
    private final AcademiaRepository academiaRepository;
    private final ExperienciaEducativaRepository experienciaRepository;

    public RecursoDocumentalService(
            RecursoDocumentalRepository repository,
            EditorialRepository editorialRepository,
            ProgramaEducativoRepository programaRepository,
            AcademiaRepository academiaRepository,
            ExperienciaEducativaRepository experienciaRepository) {

        this.repository = repository;
        this.editorialRepository = editorialRepository;
        this.programaRepository = programaRepository;
        this.academiaRepository = academiaRepository;
        this.experienciaRepository = experienciaRepository;
    }

    @Transactional(readOnly = true)
    public List<RecursoDocumental> listar() {
        List<RecursoDocumental> recursos = repository.findAll();
        inicializarVinculaciones(recursos);
        return recursos;
    }

    @Transactional(readOnly = true)
    public Optional<RecursoDocumental> buscarPorId(Integer id) {
        Optional<RecursoDocumental> resultado =
                repository.findById(id);

        resultado.ifPresent(this::inicializarVinculaciones);
        return resultado;
    }

    // Método original, conservado para compatibilidad.
    @Transactional
    public RecursoDocumental guardar(RecursoDocumental recurso) {
        return repository.save(recurso);
    }

    @Transactional
    public void eliminar(Integer id) {
        repository.deleteById(id);
    }

    // Nuevo método de registro avanzado.
    @Transactional
    public RecursoDocumental registrarConVinculaciones(
            RecursoDocumentalRequestDTO request) {

        validarDatosGenerales(request);

        RecursoDocumental recurso = new RecursoDocumental();

        recurso.setTitulo(request.getTitulo().trim());
        recurso.setAutor(request.getAutor());
        recurso.setEdicion(request.getEdicion());
        recurso.setAnio(request.getAnio());
        recurso.setIsbn(request.getIsbn());
        recurso.setCantidad(request.getCantidad());
        recurso.setMovimiento(request.getMovimiento());
        recurso.setEnCatalogoBiblioFca(
                Boolean.TRUE.equals(request.getEnCatalogoBiblioFca())
        );
        recurso.setObservaciones(request.getObservaciones());
        recurso.setTipo(request.getTipo().trim());
        recurso.setFormato(request.getFormato());
        recurso.setUrlRecurso("ELECTRONICO".equals(request.getFormato()) ? request.getUrlRecurso().trim() : null);

        Editorial editorial = resolverEditorial(request);
        recurso.setEditorial(editorial);

        Set<String> combinacionesRegistradas = new HashSet<>();

        if (request.getVinculacionesAcademicas() != null) {

            for (VinculacionAcademicaRequestDTO vinculacionRequest
                    : request.getVinculacionesAcademicas()) {

                if (vinculacionRequest == null) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Existe una vinculación académica vacía."
                    );
                }

                VinculacionAcademicaRecurso vinculacion =
                        construirVinculacion(
                                recurso,
                                vinculacionRequest
                        );

                String clave =
                        vinculacionRequest.getIdExperienciaEducativa()
                                + "-"
                                + vinculacionRequest.getIdAcademia()
                                + "-"
                                + vinculacionRequest.getIdProgramaEducativo();

                if (!combinacionesRegistradas.add(clave)) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Hay vinculaciones académicas duplicadas."
                    );
                }

                recurso.getVinculacionesAcademicas()
                        .add(vinculacion);
            }
        }

        RecursoDocumental guardado = repository.save(recurso);

        inicializarVinculaciones(guardado);

        return guardado;
    }

    private void validarDatosGenerales(
            RecursoDocumentalRequestDTO request) {

        if (request == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Los datos del recurso son obligatorios."
            );
        }

        if (request.getTitulo() == null
                || request.getTitulo().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El título es obligatorio."
            );
        }

        if (request.getTipo() == null
                || request.getTipo().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El tipo de recurso es obligatorio."
            );
        }

        if (request.getFormato() == null ||
                !(request.getFormato().equals("FISICO") || request.getFormato().equals("ELECTRONICO"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El formato debe ser FISICO o ELECTRONICO.");
        }
        if ("ELECTRONICO".equals(request.getFormato())) {
            String url = request.getUrlRecurso();
            if (url == null || url.isBlank() || url.length() > 2048 ||
                    !(url.startsWith("https://") || url.startsWith("http://"))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El recurso electrónico requiere una URL http o https válida.");
            }
            try {
                java.net.URI uri = java.net.URI.create(url);
                if (uri.getHost() == null || uri.getHost().isBlank()) throw new IllegalArgumentException();
            } catch (IllegalArgumentException ex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "URL inválida.");
            }
        }
        if (request.getCantidad() == null
                || request.getCantidad() < 1) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La cantidad debe ser mayor a cero."
            );
        }
    }

    @Transactional
    public Optional<RecursoDocumental> actualizarConVinculaciones(Integer id, RecursoDocumentalRequestDTO request) {
        validarDatosGenerales(request);
        return repository.findById(id).map(recurso -> {
            recurso.setTitulo(request.getTitulo().trim());
            recurso.setAutor(request.getAutor());
            recurso.setEdicion(request.getEdicion());
            recurso.setAnio(request.getAnio());
            recurso.setIsbn(request.getIsbn());
            recurso.setCantidad(request.getCantidad());
            recurso.setMovimiento(request.getMovimiento());
            recurso.setEnCatalogoBiblioFca(Boolean.TRUE.equals(request.getEnCatalogoBiblioFca()));
            recurso.setObservaciones(request.getObservaciones());
            recurso.setTipo(request.getTipo().trim());
            recurso.setFormato(request.getFormato());
            recurso.setUrlRecurso("ELECTRONICO".equals(request.getFormato()) ? request.getUrlRecurso().trim() : null);
            recurso.setEditorial(resolverEditorial(request));

            Set<String> combinaciones = new HashSet<>();
            Set<VinculacionAcademicaRecurso> nuevas = new HashSet<>();
            if (request.getVinculacionesAcademicas() != null) {
                for (VinculacionAcademicaRequestDTO v : request.getVinculacionesAcademicas()) {
                    if (v == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vinculación vacía.");
                    String clave = v.getIdProgramaEducativo() + "-" + v.getIdAcademia() + "-" + v.getIdExperienciaEducativa();
                    if (!combinaciones.add(clave)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vinculación duplicada.");
                    nuevas.add(construirVinculacion(recurso, v));
                }
            }
            recurso.getVinculacionesAcademicas().clear();
            repository.flush();
            recurso.getVinculacionesAcademicas().addAll(nuevas);
            RecursoDocumental actualizado = repository.save(recurso);
            inicializarVinculaciones(actualizado);
            return actualizado;
        });
    }

    private Editorial resolverEditorial(
            RecursoDocumentalRequestDTO request) {

        Integer idEditorial = request.getIdEditorial();

        String nuevaEditorial = request.getNuevaEditorial();

        boolean tieneNuevaEditorial =
                nuevaEditorial != null
                        && !nuevaEditorial.isBlank();

        if (idEditorial != null && tieneNuevaEditorial) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Selecciona una editorial existente o escribe una nueva, no ambas."
            );
        }

        if (idEditorial != null) {
            return editorialRepository.findById(idEditorial)
                    .orElseThrow(() ->
                            new ResponseStatusException(
                                    HttpStatus.BAD_REQUEST,
                                    "La editorial seleccionada no existe."
                            )
                    );
        }

        if (tieneNuevaEditorial) {

            String nombre = nuevaEditorial.trim();

            return editorialRepository
                    .findByNombreIgnoreCase(nombre)
                    .orElseGet(() -> {

                        Editorial editorial = new Editorial();
                        editorial.setNombre(nombre);

                        return editorialRepository.save(editorial);
                    });
        }

        return null;
    }

    private VinculacionAcademicaRecurso construirVinculacion(
            RecursoDocumental recurso,
            VinculacionAcademicaRequestDTO request) {

        if (request.getIdProgramaEducativo() == null
                || request.getIdAcademia() == null
                || request.getIdExperienciaEducativa() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cada vinculación debe incluir programa, academia y experiencia educativa."
            );
        }

        ProgramaEducativo programa =
                programaRepository.findById(
                        request.getIdProgramaEducativo()
                ).orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "El programa educativo no existe."
                        )
                );

        Academia academia =
                academiaRepository.findById(
                        request.getIdAcademia()
                ).orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "La academia no existe."
                        )
                );

        ExperienciaEducativa experiencia =
                experienciaRepository.findById(
                        request.getIdExperienciaEducativa()
                ).orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "La experiencia educativa no existe."
                        )
                );

        boolean academiaPerteneceAlPrograma =
                academia.getProgramasEducativos()
                        .stream()
                        .anyMatch(p ->
                                p.getIdProgramaEducativo()
                                        .equals(
                                                programa.getIdProgramaEducativo()
                                        )
                        );

        if (!academiaPerteneceAlPrograma) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La academia no está vinculada al programa educativo seleccionado."
            );
        }

        boolean experienciaPerteneceAcademia =
                experiencia.getAcademias()
                        .stream()
                        .anyMatch(a ->
                                a.getIdAcademia()
                                        .equals(
                                                academia.getIdAcademia()
                                        )
                        );

        if (!experienciaPerteneceAcademia) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La experiencia educativa no pertenece a la academia seleccionada."
            );
        }

        VinculacionAcademicaRecurso vinculacion =
                new VinculacionAcademicaRecurso();

        vinculacion.setRecursoDocumental(recurso);
        vinculacion.setProgramaEducativo(programa);
        vinculacion.setAcademia(academia);
        vinculacion.setExperienciaEducativa(experiencia);

        return vinculacion;
    }

    private void inicializarVinculaciones(
            RecursoDocumental recurso) {

        recurso.getVinculacionesAcademicas().size();

        for (VinculacionAcademicaRecurso vinculacion
                : recurso.getVinculacionesAcademicas()) {

            vinculacion.getProgramaEducativo().getNombre();
            vinculacion.getAcademia().getNombre();
            vinculacion.getExperienciaEducativa().getNombre();

            // Inicializamos también las relaciones anidadas
            // que aparecen en las respuestas JSON.
            vinculacion.getAcademia()
                    .getProgramasEducativos().size();

            vinculacion.getExperienciaEducativa()
                    .getAcademias().size();
        }
    }

    private void inicializarVinculaciones(
            List<RecursoDocumental> recursos) {

        for (RecursoDocumental recurso : recursos) {
            inicializarVinculaciones(recurso);
        }
    }

    @Transactional(readOnly = true)
    public List<RecursoDocumental> buscar(
            String titulo,
            String autor,
            String isbn,
            String tipo,
            String anio,
            Integer idEditorial,
            Boolean enCatalogo,
            Integer idExperienciaEducativa,
            Integer idAcademia,
            Integer idProgramaEducativo) {

        Specification<RecursoDocumental> spec =
                Specification
                        .where(
                                RecursoDocumentalSpecification
                                        .tituloContiene(titulo)
                        )
                        .and(
                                RecursoDocumentalSpecification
                                        .vinculacionAcademicaIgual(
                                                idExperienciaEducativa,
                                                idAcademia,
                                                idProgramaEducativo
                                        )
                        );
        List<RecursoDocumental> recursos =
                repository.findAll(spec);

        inicializarVinculaciones(recursos);

        return recursos;
    }
}