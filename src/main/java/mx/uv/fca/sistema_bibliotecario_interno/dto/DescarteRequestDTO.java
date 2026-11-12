package mx.uv.fca.sistema_bibliotecario_interno.dto;

import java.time.LocalDate;

public class DescarteRequestDTO {

    private LocalDate fechaDescarte;
    private String motivo;
    private String observaciones;
    private Integer idRecursoDocumental;
    private Integer cantidad;

    public DescarteRequestDTO() {}

    public LocalDate getFechaDescarte() { return fechaDescarte; }
    public void setFechaDescarte(LocalDate v) { fechaDescarte = v; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String v) { motivo = v; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String v) { observaciones = v; }

    public Integer getIdRecursoDocumental() {
        return idRecursoDocumental;
    }
    public void setIdRecursoDocumental(Integer v) {
        idRecursoDocumental = v;
    }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer v) { cantidad = v; }
}