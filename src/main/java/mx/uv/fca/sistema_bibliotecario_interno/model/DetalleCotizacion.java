package mx.uv.fca.sistema_bibliotecario_interno.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "detalle_cotizacion")
public class DetalleCotizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_cotizacion")
    private Integer idDetalleCotizacion;

    private Integer cantidad;

    @Column(precision = 10, scale = 2)
    private BigDecimal precioUnitarioEstimado;

    @Column(precision = 10, scale = 2)
    private BigDecimal subtotal;

    private String filu;

    @ManyToOne
    @JoinColumn(name = "id_cotizacion", nullable = false)
    private Cotizacion cotizacion;

    @ManyToOne
    @JoinColumn(name = "id_recurso_documental", nullable = false)
    private RecursoDocumental recursoDocumental;

    @ManyToOne
    @JoinColumn(name = "id_programa_educativo")
    private ProgramaEducativo programaEducativo;

    @ManyToOne
    @JoinColumn(name = "id_academia")
    private Academia academia;

    public DetalleCotizacion() {
    }

    public Integer getIdDetalleCotizacion() {
        return idDetalleCotizacion;
    }

    public void setIdDetalleCotizacion(Integer idDetalleCotizacion) {
        this.idDetalleCotizacion = idDetalleCotizacion;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitarioEstimado() {
        return precioUnitarioEstimado;
    }

    public void setPrecioUnitarioEstimado(BigDecimal precioUnitarioEstimado) {
        this.precioUnitarioEstimado = precioUnitarioEstimado;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public String getFilu() {
        return filu;
    }

    public void setFilu(String filu) {
        this.filu = filu;
    }

    public Cotizacion getCotizacion() {
        return cotizacion;
    }

    public void setCotizacion(Cotizacion cotizacion) {
        this.cotizacion = cotizacion;
    }

    public RecursoDocumental getRecursoDocumental() {
        return recursoDocumental;
    }

    public void setRecursoDocumental(RecursoDocumental recursoDocumental) {
        this.recursoDocumental = recursoDocumental;
    }

    public ProgramaEducativo getProgramaEducativo() {
        return programaEducativo;
    }

    public void setProgramaEducativo(ProgramaEducativo programaEducativo) {
        this.programaEducativo = programaEducativo;
    }

    public Academia getAcademia() {
        return academia;
    }

    public void setAcademia(Academia academia) {
        this.academia = academia;
    }
}