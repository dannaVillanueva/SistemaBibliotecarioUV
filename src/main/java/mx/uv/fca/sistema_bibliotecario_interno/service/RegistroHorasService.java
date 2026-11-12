package mx.uv.fca.sistema_bibliotecario_interno.service;

import mx.uv.fca.sistema_bibliotecario_interno.dto.RegistroHorasRequestDTO;
import mx.uv.fca.sistema_bibliotecario_interno.dto.RegistroHorasResponseDTO;
import mx.uv.fca.sistema_bibliotecario_interno.dto.ResumenHorasDTO;

import mx.uv.fca.sistema_bibliotecario_interno.model.AlumnoPractica;
import mx.uv.fca.sistema_bibliotecario_interno.model.RegistroHoras;
import mx.uv.fca.sistema_bibliotecario_interno.model.Usuario;

import mx.uv.fca.sistema_bibliotecario_interno.repository.AlumnoPracticaRepository;
import mx.uv.fca.sistema_bibliotecario_interno.repository.RegistroHorasRepository;
import mx.uv.fca.sistema_bibliotecario_interno.repository.UsuarioRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;

@Service
public class RegistroHorasService {

    private final RegistroHorasRepository repository;
    private final AlumnoPracticaRepository alumnoRepository;
    private final UsuarioRepository usuarioRepository;

    public RegistroHorasService(
            RegistroHorasRepository repository,
            AlumnoPracticaRepository alumnoRepository,
            UsuarioRepository usuarioRepository) {

        this.repository = repository;
        this.alumnoRepository = alumnoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // ==========================================
    // REGISTRAR HORAS
    // ==========================================

    @Transactional
    public RegistroHorasResponseDTO registrar(
            RegistroHorasRequestDTO request) {

        if (request == null || request.getIdAlumno() == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar un alumno"
            );
        }

        AlumnoPractica alumno =
                alumnoRepository.buscarParaActualizar(
                        request.getIdAlumno()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Alumno no encontrado"
                        )
                );

        if (!Boolean.TRUE.equals(alumno.getActivo())) {
            throw new IllegalArgumentException(
                    "El alumno no se encuentra activo"
            );
        }

        validarHorario(
                request.getHoraEntrada(),
                request.getHoraSalida()
        );

        LocalDate fecha =
                request.getFecha() != null
                        ? request.getFecha()
                        : LocalDate.now();

        validarPeriodo(alumno, fecha);

        // Se permiten varios turnos en un día,
        // siempre que sus horarios no se superpongan.

        if (repository.existeSolapamiento(
                alumno.getIdAlumno(),
                fecha,
                request.getHoraEntrada(),
                request.getHoraSalida()
        )) {
            throw new IllegalArgumentException(
                    "El horario se superpone con otro turno "
                            + "registrado para este estudiante"
            );
        }

        Usuario usuario = obtenerUsuarioAutenticado();

        RegistroHoras registro = new RegistroHoras();

        registro.setAlumno(alumno);
        registro.setUsuario(usuario);
        registro.setFecha(fecha);

        registro.setHoraEntrada(
                request.getHoraEntrada()
        );

        registro.setHoraSalida(
                request.getHoraSalida()
        );

        registro.setHorasRealizadas(
                calcularHoras(
                        request.getHoraEntrada(),
                        request.getHoraSalida()
                )
        );

        registro.setObservaciones(
                request.getObservaciones()
        );

        RegistroHoras guardado =
                repository.save(registro);

        return convertirDTO(guardado);
    }

    // ==========================================
    // ACTUALIZAR REGISTRO
    // ==========================================

    @Transactional
    public RegistroHorasResponseDTO actualizar(
            Integer idRegistro,
            RegistroHorasRequestDTO request) {

        if (idRegistro == null) {
            throw new IllegalArgumentException(
                    "Debe indicar el registro"
            );
        }

        if (request == null) {
            throw new IllegalArgumentException(
                    "Debe proporcionar los datos del registro"
            );
        }

        RegistroHoras registro =
                repository.findById(idRegistro)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Registro de horas no encontrado"
                                )
                        );

        // Bloquear el estudiante para serializar
        // modificaciones de sus horarios.

        AlumnoPractica alumno =
                alumnoRepository.buscarParaActualizar(
                        registro.getAlumno().getIdAlumno()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Alumno no encontrado"
                        )
                );

        // No se permite cambiar de estudiante
        // mediante la edición de una asistencia.

        if (request.getIdAlumno() != null &&
                !request.getIdAlumno().equals(
                        alumno.getIdAlumno()
                )) {
            throw new IllegalArgumentException(
                    "No se puede cambiar el alumno "
                            + "de un registro existente"
            );
        }

        LocalDate fecha =
                request.getFecha() != null
                        ? request.getFecha()
                        : registro.getFecha();

        LocalTime horaEntrada =
                request.getHoraEntrada() != null
                        ? request.getHoraEntrada()
                        : registro.getHoraEntrada();

        LocalTime horaSalida =
                request.getHoraSalida() != null
                        ? request.getHoraSalida()
                        : registro.getHoraSalida();

        validarHorario(horaEntrada, horaSalida);
        validarPeriodo(alumno, fecha);

        // Ignorar el registro actual al comprobar
        // si existe superposición.

        if (repository.existeSolapamientoExcluyendo(
                alumno.getIdAlumno(),
                fecha,
                horaEntrada,
                horaSalida,
                idRegistro
        )) {
            throw new IllegalArgumentException(
                    "No se puede actualizar el registro: "
                            + "el horario se superpone con otro turno"
            );
        }

        registro.setFecha(fecha);
        registro.setHoraEntrada(horaEntrada);
        registro.setHoraSalida(horaSalida);

        registro.setHorasRealizadas(
                calcularHoras(horaEntrada, horaSalida)
        );

        if (request.getObservaciones() != null) {
            registro.setObservaciones(
                    request.getObservaciones()
            );
        }

        RegistroHoras actualizado =
                repository.save(registro);

        return convertirDTO(actualizado);
    }

    // ==========================================
    // HISTORIAL COMPLETO
    // ==========================================

    @Transactional(readOnly = true)
    public List<RegistroHorasResponseDTO> historial(
            Integer idAlumno) {

        validarAlumnoExiste(idAlumno);

        return convertirLista(
                repository.findByAlumno_IdAlumno(idAlumno)
        );
    }

    // ==========================================
    // CONSULTAR POR RANGO DE FECHAS
    // ==========================================

    @Transactional(readOnly = true)
    public List<RegistroHorasResponseDTO> buscarPorRango(
            Integer idAlumno,
            LocalDate inicio,
            LocalDate fin) {

        validarAlumnoExiste(idAlumno);

        if (inicio == null || fin == null) {
            throw new IllegalArgumentException(
                    "La fecha inicial y final son obligatorias"
            );
        }

        if (fin.isBefore(inicio)) {
            throw new IllegalArgumentException(
                    "La fecha final no puede ser anterior "
                            + "a la fecha inicial"
            );
        }

        return convertirLista(
                repository.findByAlumno_IdAlumnoAndFechaBetween(
                        idAlumno,
                        inicio,
                        fin
                )
        );
    }

    // ==========================================
    // CONSULTAR POR MES
    // ==========================================

    @Transactional(readOnly = true)
    public List<RegistroHorasResponseDTO> buscarPorMes(
            Integer idAlumno,
            Integer anio,
            Integer mes) {

        validarAlumnoExiste(idAlumno);

        if (anio == null || mes == null) {
            throw new IllegalArgumentException(
                    "El año y el mes son obligatorios"
            );
        }

        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException(
                    "El mes debe estar entre 1 y 12"
            );
        }

        YearMonth periodo = YearMonth.of(anio, mes);

        return convertirLista(
                repository.findByAlumno_IdAlumnoAndFechaBetween(
                        idAlumno,
                        periodo.atDay(1),
                        periodo.atEndOfMonth()
                )
        );
    }

    // ==========================================
    // RESUMEN DE HORAS
    // ==========================================

    @Transactional(readOnly = true)
    public ResumenHorasDTO resumen(Integer idAlumno) {

        AlumnoPractica alumno =
                alumnoRepository.findById(idAlumno)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Alumno no encontrado"
                                )
                        );

        LocalDate hoy = LocalDate.now();

        LocalDate inicioSemana = hoy.with(
                TemporalAdjusters.previousOrSame(
                        DayOfWeek.MONDAY
                )
        );

        LocalDate finSemana = hoy.with(
                TemporalAdjusters.nextOrSame(
                        DayOfWeek.SUNDAY
                )
        );

        YearMonth mesActual = YearMonth.from(hoy);

        // Sumar todos los turnos del día.
        BigDecimal horasHoy = sumarHoras(
                repository.findByAlumno_IdAlumnoAndFecha(
                        idAlumno,
                        hoy
                )
        );

        // Sumar todos los turnos de la semana.
        BigDecimal horasSemana = sumarHoras(
                repository.findByAlumno_IdAlumnoAndFechaBetween(
                        idAlumno,
                        inicioSemana,
                        finSemana
                )
        );

        // Sumar todos los turnos del mes.
        BigDecimal horasMes = sumarHoras(
                repository.findByAlumno_IdAlumnoAndFechaBetween(
                        idAlumno,
                        mesActual.atDay(1),
                        mesActual.atEndOfMonth()
                )
        );

        // Total histórico.
        BigDecimal horasRealizadas = sumarHoras(
                repository.findByAlumno_IdAlumno(idAlumno)
        );

        BigDecimal horasRequeridas = BigDecimal.valueOf(
                alumno.getHorasRequeridas() != null
                        ? alumno.getHorasRequeridas()
                        : 0
        );

        BigDecimal horasRestantes =
                horasRequeridas.subtract(horasRealizadas);

        if (horasRestantes.signum() < 0) {
            horasRestantes = BigDecimal.ZERO;
        }

        BigDecimal porcentajeAvance = BigDecimal.ZERO;

        if (horasRequeridas.signum() > 0) {
            porcentajeAvance = horasRealizadas
                    .multiply(BigDecimal.valueOf(100))
                    .divide(
                            horasRequeridas,
                            2,
                            RoundingMode.HALF_UP
                    );
        }

        if (porcentajeAvance.compareTo(
                BigDecimal.valueOf(100)
        ) > 0) {
            porcentajeAvance = BigDecimal.valueOf(100);
        }

        ResumenHorasDTO dto = new ResumenHorasDTO();

        dto.setIdAlumno(alumno.getIdAlumno());

        dto.setNombreCompleto(
                alumno.getNombre() + " " + alumno.getApellidos()
        );

        dto.setMatricula(alumno.getMatricula());

        if (alumno.getTipo() != null) {
            dto.setTipo(alumno.getTipo().name());
        }

        dto.setHorasHoy(horasHoy);
        dto.setHorasSemana(horasSemana);
        dto.setHorasMes(horasMes);
        dto.setHorasRequeridas(horasRequeridas);
        dto.setHorasRealizadas(horasRealizadas);
        dto.setHorasRestantes(horasRestantes);
        dto.setPorcentajeAvance(porcentajeAvance);

        return dto;
    }

    // ==========================================
    // VALIDAR HORARIOS
    // ==========================================

    private void validarHorario(
            LocalTime entrada,
            LocalTime salida) {

        if (entrada == null || salida == null) {
            throw new IllegalArgumentException(
                    "La hora de entrada y salida son obligatorias"
            );
        }

        if (!salida.isAfter(entrada)) {
            throw new IllegalArgumentException(
                    "La hora de salida debe ser posterior "
                            + "a la hora de entrada"
            );
        }
    }

    // ==========================================
    // VALIDAR PERIODO DEL ALUMNO
    // ==========================================

    private void validarPeriodo(
            AlumnoPractica alumno,
            LocalDate fecha) {

        if (alumno.getFechaInicio() != null &&
                fecha.isBefore(alumno.getFechaInicio())) {
            throw new IllegalArgumentException(
                    "La fecha es anterior al inicio "
                            + "del servicio o las prácticas"
            );
        }

        if (alumno.getFechaFin() != null &&
                fecha.isAfter(alumno.getFechaFin())) {
            throw new IllegalArgumentException(
                    "La fecha es posterior al término "
                            + "del servicio o las prácticas"
            );
        }
    }

    // ==========================================
    // VALIDAR EXISTENCIA DEL ALUMNO
    // ==========================================

    private void validarAlumnoExiste(Integer idAlumno) {

        if (idAlumno == null ||
                !alumnoRepository.existsById(idAlumno)) {
            throw new RuntimeException(
                    "Alumno no encontrado"
            );
        }
    }

    // ==========================================
    // OBTENER USUARIO AUTENTICADO
    // ==========================================

    private Usuario obtenerUsuarioAutenticado() {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                "anonymousUser".equals(
                        authentication.getPrincipal()
                )) {
            throw new RuntimeException(
                    "No existe un usuario autenticado"
            );
        }

        String correo = authentication.getName();

        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuario no encontrado"
                        )
                );
    }

    // ==========================================
    // CALCULAR HORAS
    // ==========================================

    private BigDecimal calcularHoras(
            LocalTime entrada,
            LocalTime salida) {

        long minutos = Duration.between(
                entrada,
                salida
        ).toMinutes();

        return BigDecimal.valueOf(minutos)
                .divide(
                        BigDecimal.valueOf(60),
                        2,
                        RoundingMode.HALF_UP
                );
    }

    // ==========================================
    // SUMAR HORAS
    // ==========================================

    private BigDecimal sumarHoras(
            List<RegistroHoras> registros) {

        return registros.stream()
                .map(RegistroHoras::getHorasRealizadas)
                .filter(horas -> horas != null)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }

    // ==========================================
    // CONVERTIR LISTA A DTO
    // ==========================================

    private List<RegistroHorasResponseDTO> convertirLista(
            List<RegistroHoras> registros) {

        return registros.stream()
                .sorted(
                        Comparator.comparing(
                                RegistroHoras::getFecha
                        ).thenComparing(
                                RegistroHoras::getHoraEntrada
                        ).thenComparing(
                                RegistroHoras::getIdRegistro
                        )
                )
                .map(this::convertirDTO)
                .toList();
    }

    // ==========================================
    // CONVERTIR REGISTRO A DTO
    // ==========================================

    private RegistroHorasResponseDTO convertirDTO(
            RegistroHoras registro) {

        RegistroHorasResponseDTO dto =
                new RegistroHorasResponseDTO();

        dto.setIdRegistro(registro.getIdRegistro());
        dto.setFecha(registro.getFecha());
        dto.setHoraEntrada(registro.getHoraEntrada());
        dto.setHoraSalida(registro.getHoraSalida());

        dto.setHorasRealizadas(
                registro.getHorasRealizadas()
        );

        dto.setObservaciones(
                registro.getObservaciones()
        );

        if (registro.getAlumno() != null) {

            AlumnoPractica alumno = registro.getAlumno();

            dto.setIdAlumno(alumno.getIdAlumno());

            dto.setNombreAlumno(
                    alumno.getNombre() + " "
                            + alumno.getApellidos()
            );

            dto.setMatricula(alumno.getMatricula());
        }

        if (registro.getUsuario() != null) {

            Usuario usuario = registro.getUsuario();

            dto.setIdUsuario(usuario.getIdUsuario());
            dto.setNombreUsuario(usuario.getNombre());
        }

        return dto;
    }
}