
package mx.uv.fca.sistema_bibliotecario_interno.dto;

import java.util.ArrayList;
import java.util.List;

public class RecursoDocumentalRequestDTO {

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
    private String formato;
    private String urlRecurso;

    private Integer idEditorial;

    // Se utilizará cuando se registre
    // una editorial nueva desde Angular.
    private String nuevaEditorial;

    private List<VinculacionAcademicaRequestDTO>
            vinculacionesAcademicas = new ArrayList<>();

    public RecursoDocumentalRequestDTO() {
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

    public Integer getIdEditorial() {
        return idEditorial;
    }

    public void setIdEditorial(Integer idEditorial) {
        this.idEditorial = idEditorial;
    }

    public String getNuevaEditorial() {
        return nuevaEditorial;
    }

    public void setNuevaEditorial(String nuevaEditorial) {
        this.nuevaEditorial = nuevaEditorial;
    }

    public List<VinculacionAcademicaRequestDTO>
    getVinculacionesAcademicas() {
        return vinculacionesAcademicas;
    }

    public void setVinculacionesAcademicas(
            List<VinculacionAcademicaRequestDTO>
                    vinculacionesAcademicas) {
        this.vinculacionesAcademicas = vinculacionesAcademicas;
    }
}
