package mx.uv.fca.sistema_bibliotecario_interno.dto;

import java.time.LocalDate;

public class BajaRequestDTO {

    private LocalDate fechaBaja;
    private String motivo;
    private String observaciones;
    private Integer idRecursoDocumental;
    private Integer cantidad;

    public BajaRequestDTO() {
    }

    public LocalDate getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(LocalDate fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Integer getIdRecursoDocumental() {
        return idRecursoDocumental;
    }

    public void setIdRecursoDocumental(
            Integer idRecursoDocumental) {
        this.idRecursoDocumental = idRecursoDocumental;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}