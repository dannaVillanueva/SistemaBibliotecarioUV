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

    public Descarte() {
    }

    public Integer getIdDescarte() {
        return idDescarte;
    }

    public void setIdDescarte(Integer idDescarte) {
        this.idDescarte = idDescarte;
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