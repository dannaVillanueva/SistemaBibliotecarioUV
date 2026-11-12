package mx.uv.fca.sistema_bibliotecario_interno.dto;

import java.time.LocalDate;

public record DescarteResponseDTO(
        Integer idDescarte,
        LocalDate fechaDescarte,
        String motivo,
        String observaciones,
        Integer cantidad,
        String estado,
        LocalDate fechaResolucion,
        String observacionesResolucion,
        Integer idRecursoDocumental,
        String tituloRecurso,
        String isbn,
        Integer idUsuario,
        String nombreUsuario,
        Integer idUsuarioResolucion,
        String nombreUsuarioResolucion,
        Integer idBaja
) {}