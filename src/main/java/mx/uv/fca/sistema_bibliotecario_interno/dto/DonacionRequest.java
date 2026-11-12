package mx.uv.fca.sistema_bibliotecario_interno.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DonacionRequest(
    String nombreDonante,
    LocalDate fechaDonacion,
    Integer cantidad,
    BigDecimal precioEstimado,
    String observaciones,
    Integer idRecursoDocumental,
    LibroNuevo libroNuevo
) {
    public record LibroNuevo(String titulo, String autor, String isbn,
                             String edicion, String anio, String tipo) {}
}
