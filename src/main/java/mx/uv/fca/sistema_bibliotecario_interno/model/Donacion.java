package mx.uv.fca.sistema_bibliotecario_interno.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "donacion")
public class Donacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDonacion;

    @Column(nullable = false)
    private Integer cantidad = 1;

    private String nombreDonante;

    private LocalDate fechaDonacion;

    @Column(precision = 10, scale = 2)
    private BigDecimal precioEstimado;

    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "id_recurso_documental", nullable = false)
    private RecursoDocumental recursoDocumental;

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public Donacion() {
    }

    public Integer getIdDonacion() {
        return idDonacion;
    }

    public void setIdDonacion(Integer idDonacion) {
        this.idDonacion = idDonacion;
    }

    public String getNombreDonante() {
        return nombreDonante;
    }

    public void setNombreDonante(String nombreDonante) {
        this.nombreDonante = nombreDonante;
    }

    public LocalDate getFechaDonacion() {
        return fechaDonacion;
    }

    public void setFechaDonacion(LocalDate fechaDonacion) {
        this.fechaDonacion = fechaDonacion;
    }

    public BigDecimal getPrecioEstimado() {
        return precioEstimado;
    }

    public void setPrecioEstimado(BigDecimal precioEstimado) {
        this.precioEstimado = precioEstimado;
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

    public void setRecursoDocumental(RecursoDocumental recursoDocumental) {
        this.recursoDocumental = recursoDocumental;
    }
}