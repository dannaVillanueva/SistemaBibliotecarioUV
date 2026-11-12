package mx.uv.fca.sistema_bibliotecario_interno.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_cotizacion", indexes = {
    @Index(name = "idx_historial_cotizacion", columnList = "id_cotizacion")
})
public class HistorialCotizacion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial")
    private Long idHistorial;

    // Sin FK: el historial permanece aunque se elimine la cotización.
    @Column(name = "id_cotizacion", nullable = false)
    private Integer idCotizacion;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "accion", nullable = false, length = 30)
    private String accion;

    @Column(name = "estado_anterior", length = 30)
    private String estadoAnterior;

    @Column(name = "estado_nuevo", length = 30)
    private String estadoNuevo;

    @Column(name = "usuario", length = 150)
    private String usuario;

    @Column(name = "observaciones", length = 1000)
    private String observaciones;

    @Lob @Column(name = "datos_anteriores", columnDefinition = "LONGTEXT")
    private String datosAnteriores;

    @Lob @Column(name = "datos_nuevos", columnDefinition = "LONGTEXT")
    private String datosNuevos;

    public Long getIdHistorial() { return idHistorial; }
    public void setIdHistorial(Long v) { idHistorial = v; }
    public Integer getIdCotizacion() { return idCotizacion; }
    public void setIdCotizacion(Integer v) { idCotizacion = v; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime v) { fechaHora = v; }
    public String getAccion() { return accion; }
    public void setAccion(String v) { accion = v; }
    public String getEstadoAnterior() { return estadoAnterior; }
    public void setEstadoAnterior(String v) { estadoAnterior = v; }
    public String getEstadoNuevo() { return estadoNuevo; }
    public void setEstadoNuevo(String v) { estadoNuevo = v; }
    public String getUsuario() { return usuario; }
    public void setUsuario(String v) { usuario = v; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String v) { observaciones = v; }
    public String getDatosAnteriores() { return datosAnteriores; }
    public void setDatosAnteriores(String v) { datosAnteriores = v; }
    public String getDatosNuevos() { return datosNuevos; }
    public void setDatosNuevos(String v) { datosNuevos = v; }
}
