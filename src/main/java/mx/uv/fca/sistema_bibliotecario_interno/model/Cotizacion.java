package mx.uv.fca.sistema_bibliotecario_interno.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "cotizacion")
public class Cotizacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCotizacion;
    private LocalDate fechaSolicitud;
    private String estado;
    @Column(length = 3000)
    private String observaciones;
    private String solicitante;
    private String correoSolicitante;
    private String telefonoSolicitante;
    private String dependencia;
    @Column(length = 3000)
    private String justificacion;
    private String proveedorSugerido;

    public Cotizacion() {}
    public Integer getIdCotizacion() { return idCotizacion; }
    public void setIdCotizacion(Integer value) { this.idCotizacion = value; }
    public LocalDate getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(LocalDate value) { this.fechaSolicitud = value; }
    public String getEstado() { return estado; }
    public void setEstado(String value) { this.estado = value; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String value) { this.observaciones = value; }
    public String getSolicitante() { return solicitante; }
    public void setSolicitante(String value) { this.solicitante = value; }
    public String getCorreoSolicitante() { return correoSolicitante; }
    public void setCorreoSolicitante(String value) { this.correoSolicitante = value; }
    public String getTelefonoSolicitante() { return telefonoSolicitante; }
    public void setTelefonoSolicitante(String value) { this.telefonoSolicitante = value; }
    public String getDependencia() { return dependencia; }
    public void setDependencia(String value) { this.dependencia = value; }
    public String getJustificacion() { return justificacion; }
    public void setJustificacion(String value) { this.justificacion = value; }
    public String getProveedorSugerido() { return proveedorSugerido; }
    public void setProveedorSugerido(String value) { this.proveedorSugerido = value; }
}
