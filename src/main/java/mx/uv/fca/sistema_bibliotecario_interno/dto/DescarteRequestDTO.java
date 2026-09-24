package mx.uv.fca.sistema_bibliotecario_interno.dto;

import java.time.LocalDate;

public class DescarteRequestDTO {

    private LocalDate fechaDescarte;
    private String motivo;
    private String observaciones;
    private Integer idRecursoDocumental;

    public DescarteRequestDTO() {
    }

    public LocalDate getFechaDescarte() {
        return fechaDescarte;
    }

    public void setFechaDescarte(LocalDate fechaDescarte) {
        this.fechaDescarte = fechaDescarte;
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

    public void setIdRecursoDocumental(Integer idRecursoDocumental) {
        this.idRecursoDocumental = idRecursoDocumental;
    }
}