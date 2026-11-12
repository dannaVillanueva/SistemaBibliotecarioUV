package mx.uv.fca.sistema_bibliotecario_interno.service;

import mx.uv.fca.sistema_bibliotecario_interno.dto.AlumnoPracticaResponseDTO;
import mx.uv.fca.sistema_bibliotecario_interno.dto.ResumenHorasDTO;

import mx.uv.fca.sistema_bibliotecario_interno.model.AlumnoPractica;
import mx.uv.fca.sistema_bibliotecario_interno.model.TipoPractica;

import mx.uv.fca.sistema_bibliotecario_interno.repository.AlumnoPracticaRepository;

import mx.uv.fca.sistema_bibliotecario_interno.specification.AlumnoPracticaSpecification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AlumnoPracticaService {

    private final AlumnoPracticaRepository repository;
    private final RegistroHorasService registroHorasService;

    public AlumnoPracticaService(
            AlumnoPracticaRepository repository,
            RegistroHorasService registroHorasService) {

        this.repository = repository;
        this.registroHorasService = registroHorasService;
    }

    // =====================================================
    // LISTAR TODOS
    // =====================================================

    public List<AlumnoPracticaResponseDTO> listar() {

        return repository
                .findAll()
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    // =====================================================
    // BUSCAR POR ID
    // =====================================================

    public AlumnoPracticaResponseDTO buscarPorId(
            Integer id) {

        AlumnoPractica alumno =
                buscarEntidadPorId(id);

        return convertirDTO(alumno);
    }

    // =====================================================
    // BUSCAR ENTIDAD POR ID
    // =====================================================

    public AlumnoPractica buscarEntidadPorId(
            Integer id) {

        return repository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Alumno no encontrado"
                        ));
    }

    // =====================================================
    // REGISTRAR ALUMNO
    // =====================================================

    public AlumnoPracticaResponseDTO guardar(
            AlumnoPractica alumno) {

        if (alumno.getMatricula() == null ||
                alumno.getMatricula().isBlank()) {

            throw new RuntimeException(
                    "La matrícula es obligatoria"
            );
        }

        if (alumno.getIdAlumno() == null &&
                repository.existsByMatricula(
                        alumno.getMatricula()
                )) {

            throw new RuntimeException(
                    "Ya existe un alumno con esa matrícula"
            );
        }

        if (alumno.getActivo() == null) {
            alumno.setActivo(true);
        }

        AlumnoPractica guardado =
                repository.save(alumno);

        return convertirDTO(guardado);
    }

    // =====================================================
    // ACTUALIZAR ALUMNO
    // =====================================================

    public AlumnoPracticaResponseDTO actualizar(
            Integer id,
            AlumnoPractica datos) {

        AlumnoPractica alumno =
                buscarEntidadPorId(id);

        // Verificar cambio de matrícula

        if (datos.getMatricula() != null &&
                !datos.getMatricula().isBlank() &&
                !datos.getMatricula()
                        .equals(alumno.getMatricula())) {

            if (repository.existsByMatricula(
                    datos.getMatricula())) {

                throw new RuntimeException(
                        "Ya existe otro alumno con esa matrícula"
                );
            }
        }

        alumno.setMatricula(
                datos.getMatricula()
        );

        alumno.setNombre(
                datos.getNombre()
        );

        alumno.setApellidos(
                datos.getApellidos()
        );

        alumno.setCorreo(
                datos.getCorreo()
        );

        alumno.setTelefono(
                datos.getTelefono()
        );

        alumno.setTipo(
                datos.getTipo()
        );

        alumno.setFechaInicio(
                datos.getFechaInicio()
        );

        alumno.setFechaFin(
                datos.getFechaFin()
        );

        alumno.setHorasRequeridas(
                datos.getHorasRequeridas()
        );

        alumno.setProgramaEducativo(
                datos.getProgramaEducativo()
        );

        if (datos.getActivo() != null) {

            alumno.setActivo(
                    datos.getActivo()
            );
        }

        AlumnoPractica actualizado =
                repository.save(alumno);

        return convertirDTO(actualizado);
    }

    // =====================================================
    // FILTROS
    // =====================================================

    public List<AlumnoPracticaResponseDTO> buscar(
            String matricula,
            String nombre,
            TipoPractica tipo,
            Integer idProgramaEducativo,
            Boolean activo) {

        Specification<AlumnoPractica> spec =
                AlumnoPracticaSpecification
                        .matriculaContiene(matricula)

                        .and(
                                AlumnoPracticaSpecification
                                        .nombreContiene(nombre)
                        )

                        .and(
                                AlumnoPracticaSpecification
                                        .tipoIgual(tipo)
                        )

                        .and(
                                AlumnoPracticaSpecification
                                        .programaEducativoIgual(
                                                idProgramaEducativo
                                        )
                        )

                        .and(
                                AlumnoPracticaSpecification
                                        .activoIgual(activo)
                        );

        return repository
                .findAll(spec)
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    // =====================================================
    // CAMBIAR ESTADO GENÉRICO
    // =====================================================

    public AlumnoPracticaResponseDTO cambiarEstado(
            Integer id,
            Boolean activo) {

        if (activo == null) {

            throw new RuntimeException(
                    "Debe indicar el estado del alumno"
            );
        }

        AlumnoPractica alumno =
                buscarEntidadPorId(id);

        alumno.setActivo(activo);

        AlumnoPractica actualizado =
                repository.save(alumno);

        return convertirDTO(actualizado);
    }

    // =====================================================
    // FINALIZAR PRÁCTICAS / SERVICIO SOCIAL
    // =====================================================

    public AlumnoPracticaResponseDTO finalizar(
            Integer id) {

        AlumnoPractica alumno =
                buscarEntidadPorId(id);

        if (!Boolean.TRUE.equals(
                alumno.getActivo())) {

            throw new RuntimeException(
                    "Las prácticas o servicio social del alumno ya están finalizados"
            );
        }

        alumno.setActivo(false);

        AlumnoPractica actualizado =
                repository.save(alumno);

        return convertirDTO(actualizado);
    }

    // =====================================================
    // REACTIVAR PRÁCTICAS / SERVICIO SOCIAL
    // =====================================================

    public AlumnoPracticaResponseDTO reactivar(
            Integer id) {

        AlumnoPractica alumno =
                buscarEntidadPorId(id);

        if (Boolean.TRUE.equals(
                alumno.getActivo())) {

            throw new RuntimeException(
                    "El alumno ya se encuentra activo"
            );
        }

        alumno.setActivo(true);

        AlumnoPractica actualizado =
                repository.save(alumno);

        return convertirDTO(actualizado);
    }

    // =====================================================
    // CONVERTIR ENTIDAD A DTO
    // =====================================================

    private AlumnoPracticaResponseDTO convertirDTO(
            AlumnoPractica alumno) {

        AlumnoPracticaResponseDTO dto =
                new AlumnoPracticaResponseDTO();

        // ------------------------------
        // DATOS PERSONALES
        // ------------------------------

        dto.setIdAlumno(
                alumno.getIdAlumno()
        );

        dto.setMatricula(
                alumno.getMatricula()
        );

        dto.setNombre(
                alumno.getNombre()
        );

        dto.setApellidos(
                alumno.getApellidos()
        );

        String nombreCompleto =
                ((alumno.getNombre() != null)
                        ? alumno.getNombre()
                        : "")
                        +
                        " "
                        +
                        ((alumno.getApellidos() != null)
                                ? alumno.getApellidos()
                                : "");

        dto.setNombreCompleto(
                nombreCompleto.trim()
        );

        dto.setCorreo(
                alumno.getCorreo()
        );

        dto.setTelefono(
                alumno.getTelefono()
        );

        // ------------------------------
        // TIPO
        // ------------------------------

        if (alumno.getTipo() != null) {

            dto.setTipo(
                    alumno.getTipo().name()
            );
        }

        // ------------------------------
        // FECHAS
        // ------------------------------

        dto.setFechaInicio(
                alumno.getFechaInicio()
        );

        dto.setFechaFin(
                alumno.getFechaFin()
        );

        // ------------------------------
        // HORAS REQUERIDAS
        // ------------------------------

        dto.setHorasRequeridas(
                alumno.getHorasRequeridas()
        );

        // ------------------------------
        // ACTIVO / ESTADO
        // ------------------------------

        dto.setActivo(
                alumno.getActivo()
        );

        if (Boolean.TRUE.equals(
                alumno.getActivo())) {

            dto.setEstado(
                    "EN_CURSO"
            );

        } else {

            dto.setEstado(
                    "FINALIZADO"
            );
        }

        // ------------------------------
        // PROGRAMA EDUCATIVO
        // ------------------------------

        if (alumno.getProgramaEducativo() != null) {

            dto.setIdProgramaEducativo(
                    alumno
                            .getProgramaEducativo()
                            .getIdProgramaEducativo()
            );

            dto.setProgramaEducativo(
                    alumno
                            .getProgramaEducativo()
                            .getNombre()
            );

            dto.setProgramaEducativoCorto(
                    alumno
                            .getProgramaEducativo()
                            .getNombreCorto()
            );
        }

        // ------------------------------
        // HORAS Y AVANCE
        // ------------------------------

        if (alumno.getIdAlumno() != null) {

            ResumenHorasDTO resumen =
                    registroHorasService.resumen(
                            alumno.getIdAlumno()
                    );

            dto.setHorasRealizadas(
                    resumen.getHorasRealizadas()
            );

            dto.setHorasRestantes(
                    resumen.getHorasRestantes()
            );

            dto.setPorcentajeAvance(
                    resumen.getPorcentajeAvance()
            );

        } else {

            dto.setHorasRealizadas(
                    BigDecimal.ZERO
            );

            BigDecimal requeridas =
                    alumno.getHorasRequeridas() != null
                            ? BigDecimal.valueOf(
                            alumno.getHorasRequeridas()
                    )
                            : BigDecimal.ZERO;

            dto.setHorasRestantes(
                    requeridas
            );

            dto.setPorcentajeAvance(
                    BigDecimal.ZERO
            );
        }

        return dto;
    }
}