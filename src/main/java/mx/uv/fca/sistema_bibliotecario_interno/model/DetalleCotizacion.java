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
    @Column(length = 500)
    private String tituloExterno;
    private String autorExterno;
    private String isbnExterno;
    private String editorialExterna;
    @ManyToOne
    @JoinColumn(name = "id_editorial")
    private Editorial editorial;
    @Column(name = "url_cotizacion", length = 2048)
    private String urlCotizacion;
    @Column(name = "estado_adquisicion", length = 30)
    private String estadoAdquisicion = "PENDIENTE";
    private String edicionExterna;
    private Integer anioExterno;
    @ManyToOne
    @JoinColumn(name = "id_cotizacion", nullable = false)
    private Cotizacion cotizacion;

    @ManyToOne
    @JoinColumn(
            name = "id_recurso_documental",
            nullable = true
    )
    private RecursoDocumental recursoDocumental;

    @ManyToOne
    @JoinColumn(name = "id_programa_educativo")
    private ProgramaEducativo programaEducativo;
    @ManyToOne
    @JoinColumn(name = "id_academia")
    private Academia academia;

    public DetalleCotizacion() {}
    public Integer getIdDetalleCotizacion() { return idDetalleCotizacion; }
    public void setIdDetalleCotizacion(Integer value) { this.idDetalleCotizacion = value; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer value) { this.cantidad = value; }
    public BigDecimal getPrecioUnitarioEstimado() { return precioUnitarioEstimado; }
    public void setPrecioUnitarioEstimado(BigDecimal value) { this.precioUnitarioEstimado = value; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal value) { this.subtotal = value; }
    public String getFilu() { return filu; }
    public void setFilu(String value) { this.filu = value; }
    public String getTituloExterno() { return tituloExterno; }
    public void setTituloExterno(String value) { this.tituloExterno = value; }
    public String getAutorExterno() { return autorExterno; }
    public void setAutorExterno(String value) { this.autorExterno = value; }
    public String getIsbnExterno() { return isbnExterno; }
    public void setIsbnExterno(String value) { this.isbnExterno = value; }
    public String getEditorialExterna() { return editorialExterna; }
    public void setEditorialExterna(String value) { this.editorialExterna = value; }
    public Editorial getEditorial() { return editorial; }
    public void setEditorial(Editorial value) { this.editorial = value; }
    public String getUrlCotizacion() { return urlCotizacion; }
    public void setUrlCotizacion(String value) { this.urlCotizacion = value; }
    public String getEstadoAdquisicion() { return estadoAdquisicion; }
    public void setEstadoAdquisicion(String value) { this.estadoAdquisicion = value; }
    public String getEdicionExterna() { return edicionExterna; }
    public void setEdicionExterna(String value) { this.edicionExterna = value; }
    public Integer getAnioExterno() { return anioExterno; }
    public void setAnioExterno(Integer value) { this.anioExterno = value; }
    public Cotizacion getCotizacion() { return cotizacion; }
    public void setCotizacion(Cotizacion value) { this.cotizacion = value; }
    public RecursoDocumental getRecursoDocumental() { return recursoDocumental; }
    public void setRecursoDocumental(RecursoDocumental value) { this.recursoDocumental = value; }
    public ProgramaEducativo getProgramaEducativo() { return programaEducativo; }
    public void setProgramaEducativo(ProgramaEducativo value) { this.programaEducativo = value; }
    public Academia getAcademia() { return academia; }
    public void setAcademia(Academia value) { this.academia = value; }
}
