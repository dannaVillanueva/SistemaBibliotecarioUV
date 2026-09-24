package mx.uv.fca.sistema_bibliotecario_interno.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class CotizacionSalidaDTO {

    private Integer idCotizacion;
    private LocalDate fechaSolicitud;
    private String estado;
    private String observaciones;
    private BigDecimal total;

    private List<SalidaCotizacionDTO> detalles;

    public CotizacionSalidaDTO() {
    }

    public Integer getIdCotizacion() {
        return idCotizacion;
    }

    public void setIdCotizacion(Integer idCotizacion) {
        this.idCotizacion = idCotizacion;
    }

    public LocalDate getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDate fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<SalidaCotizacionDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(
            List<SalidaCotizacionDTO> detalles) {

        this.detalles = detalles;
    }
}