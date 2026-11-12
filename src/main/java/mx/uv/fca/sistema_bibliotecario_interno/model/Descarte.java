package mx.uv.fca.sistema_bibliotecario_interno.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "descarte")
public class Descarte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_descarte")
    private Integer idDescarte;

    @Column(name = "fecha_descarte", nullable = false)
    private LocalDate fechaDescarte;

    @Column(nullable = false, length = 500)
    private String motivo;

    @Column(length = 1000)
    private String observaciones;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false, length = 20)
    private String estado = "PENDIENTE";

    @Column(name = "fecha_resolucion")
    private LocalDate fechaResolucion;

    @Column(name = "observaciones_resolucion", length = 1000)
    private String observacionesResolucion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_recurso_documental", nullable = false)
    private RecursoDocumental recursoDocumental;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_resolucion")
    private Usuario usuarioResolucion;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_baja", unique = true)
    private Baja baja;

    public Descarte() {}

    public Integer getIdDescarte() { return idDescarte; }
    public void setIdDescarte(Integer v) { idDescarte = v; }

    public LocalDate getFechaDescarte() { return fechaDescarte; }
    public void setFechaDescarte(LocalDate v) { fechaDescarte = v; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String v) { motivo = v; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String v) { observaciones = v; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer v) { cantidad = v; }

    public String getEstado() { return estado; }
    public void setEstado(String v) { estado = v; }

    public LocalDate getFechaResolucion() { return fechaResolucion; }
    public void setFechaResolucion(LocalDate v) { fechaResolucion = v; }

    public String getObservacionesResolucion() {
        return observacionesResolucion;
    }
    public void setObservacionesResolucion(String v) {
        observacionesResolucion = v;
    }

    public RecursoDocumental getRecursoDocumental() {
        return recursoDocumental;
    }
    public void setRecursoDocumental(RecursoDocumental v) {
        recursoDocumental = v;
    }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario v) { usuario = v; }

    public Usuario getUsuarioResolucion() {
        return usuarioResolucion;
    }
    public void setUsuarioResolucion(Usuario v) {
        usuarioResolucion = v;
    }

    public Baja getBaja() { return baja; }
    public void setBaja(Baja v) { baja = v; }
}