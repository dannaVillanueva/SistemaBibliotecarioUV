package mx.uv.fca.sistema_bibliotecario_interno.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "baja")
public class Baja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_baja")
    private Integer idBaja;

    @Column(name = "fecha_baja", nullable = false)
    private LocalDate fechaBaja;

    @Column(nullable = false, length = 500)
    private String motivo;

    @Column(length = 1000)
    private String observaciones;

    @ManyToOne
    @JoinColumn(
            name = "id_recurso_documental",
            nullable = false
    )
    private RecursoDocumental recursoDocumental;

    @ManyToOne
    @JoinColumn(
            name = "id_usuario",
            nullable = false
    )
    private Usuario usuario;

    public Baja() {
    }

    public Integer getIdBaja() {
        return idBaja;
    }

    public void setIdBaja(Integer idBaja) {
        this.idBaja = idBaja;
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

    public RecursoDocumental getRecursoDocumental() {
        return recursoDocumental;
    }

    public void setRecursoDocumental(
            RecursoDocumental recursoDocumental) {

        this.recursoDocumental = recursoDocumental;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}