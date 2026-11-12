package mx.uv.fca.sistema_bibliotecario_interno.repository;

import mx.uv.fca.sistema_bibliotecario_interno.model.RegistroHoras;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface RegistroHorasRepository
        extends JpaRepository<RegistroHoras, Integer> {

    // Historial completo del alumno
    List<RegistroHoras> findByAlumno_IdAlumno(
            Integer idAlumno
    );

    // Todos los turnos de una fecha
    List<RegistroHoras> findByAlumno_IdAlumnoAndFecha(
            Integer idAlumno,
            LocalDate fecha
    );

    // Turnos entre dos fechas
    List<RegistroHoras> findByAlumno_IdAlumnoAndFechaBetween(
            Integer idAlumno,
            LocalDate inicio,
            LocalDate fin
    );

    // Se conserva por compatibilidad
    boolean existsByAlumno_IdAlumnoAndFecha(
            Integer idAlumno,
            LocalDate fecha
    );

    // Comprobar horarios superpuestos
    // al crear una nueva asistencia
    @Query("""
        SELECT CASE WHEN COUNT(r) > 0
            THEN true ELSE false END
        FROM RegistroHoras r
        WHERE r.alumno.idAlumno = :idAlumno
          AND r.fecha = :fecha
          AND r.horaEntrada < :horaSalida
          AND r.horaSalida > :horaEntrada
    """)
    boolean existeSolapamiento(
            @Param("idAlumno") Integer idAlumno,
            @Param("fecha") LocalDate fecha,
            @Param("horaEntrada") LocalTime horaEntrada,
            @Param("horaSalida") LocalTime horaSalida
    );

    // Comprobar horarios superpuestos
    // al editar una asistencia
    @Query("""
        SELECT CASE WHEN COUNT(r) > 0
            THEN true ELSE false END
        FROM RegistroHoras r
        WHERE r.alumno.idAlumno = :idAlumno
          AND r.fecha = :fecha
          AND r.idRegistro <> :idRegistro
          AND r.horaEntrada < :horaSalida
          AND r.horaSalida > :horaEntrada
    """)
    boolean existeSolapamientoExcluyendo(
            @Param("idAlumno") Integer idAlumno,
            @Param("fecha") LocalDate fecha,
            @Param("horaEntrada") LocalTime horaEntrada,
            @Param("horaSalida") LocalTime horaSalida,
            @Param("idRegistro") Integer idRegistro
    );
}