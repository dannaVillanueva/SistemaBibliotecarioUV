package mx.uv.fca.sistema_bibliotecario_interno.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "recurso_documental")
public class RecursoDocumental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRecursoDocumental;

    private String titulo;
    private String autor;
    private String edicion;
    private String anio;
    private String isbn;
    private Integer cantidad;
    private String movimiento;
    private Boolean enCatalogoBiblioFca;
    private String observaciones;
    private String tipo;

    @Column(name = "formato", length = 20)
    private String formato;

    @Column(name = "url_recurso", length = 2048)
    private String urlRecurso;

    @ManyToOne
    @JoinColumn(name = "id_editorial")
    private Editorial editorial;

    // Relación anterior: se conserva por compatibilidad
    // con los recursos registrados.
    @ManyToMany
    @JoinTable(
            name = "experiencia_recurso",
            joinColumns = @JoinColumn(
                    name = "id_recurso_documental"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "id_experiencia_educativa"
            )
    )
    private Set<ExperienciaEducativa> experienciasEducativas =
            new HashSet<>();

    // Nueva relación: combinaciones específicas de
    // programa, academia y experiencia educativa.
    @OneToMany(
            mappedBy = "recursoDocumental",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<VinculacionAcademicaRecurso> vinculacionesAcademicas =
            new HashSet<>();

    public RecursoDocumental() {
    }

    public Integer getIdRecursoDocumental() {
        return idRecursoDocumental;
    }

    public void setIdRecursoDocumental(
            Integer idRecursoDocumental) {
        this.idRecursoDocumental = idRecursoDocumental;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEdicion() {
        return edicion;
    }

    public void setEdicion(String edicion) {
        this.edicion = edicion;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(String movimiento) {
        this.movimiento = movimiento;
    }

    public Boolean getEnCatalogoBiblioFca() {
        return enCatalogoBiblioFca;
    }

    public void setEnCatalogoBiblioFca(
            Boolean enCatalogoBiblioFca) {
        this.enCatalogoBiblioFca = enCatalogoBiblioFca;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFormato() { return formato; }

    public void setFormato(String formato) { this.formato = formato; }

    public String getUrlRecurso() { return urlRecurso; }

    public void setUrlRecurso(String urlRecurso) { this.urlRecurso = urlRecurso; }

    public Editorial getEditorial() {
        return editorial;
    }

    public void setEditorial(Editorial editorial) {
        this.editorial = editorial;
    }

    public Set<ExperienciaEducativa>
    getExperienciasEducativas() {
        return experienciasEducativas;
    }

    public void setExperienciasEducativas(
            Set<ExperienciaEducativa> experienciasEducativas) {
        this.experienciasEducativas = experienciasEducativas;
    }

    public Set<VinculacionAcademicaRecurso>
    getVinculacionesAcademicas() {
        return vinculacionesAcademicas;
    }

    public void setVinculacionesAcademicas(
            Set<VinculacionAcademicaRecurso> vinculacionesAcademicas) {
        this.vinculacionesAcademicas = vinculacionesAcademicas;
    }
}
