package mx.uv.fca.sistema_bibliotecario_interno.dto;

import java.time.LocalDate;

public class DescarteResponseDTO {

    private Integer idDescarte;
    private LocalDate fechaDescarte;
    private String motivo;
    private String observaciones;

    private Integer idRecursoDocumental;
    private String tituloRecurso;
    private String isbn;

    private Integer idUsuario;
    private String nombreUsuario;

    public DescarteResponseDTO() {
    }

    public DescarteResponseDTO(
            Integer idDescarte,
            LocalDate fechaDescarte,
            String motivo,
            String observaciones,
            Integer idRecursoDocumental,
            String tituloRecurso,
            String isbn,
            Integer idUsuario,
            String nombreUsuario) {

        this.idDescarte = idDescarte;
        this.fechaDescarte = fechaDescarte;
        this.motivo = motivo;
        this.observaciones = observaciones;
        this.idRecursoDocumental = idRecursoDocumental;
        this.tituloRecurso = tituloRecurso;
        this.isbn = isbn;
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
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

    public Integer getIdRecursoDocumental() {
        return idRecursoDocumental;
    }

    public void setIdRecursoDocumental(Integer idRecursoDocumental) {
        this.idRecursoDocumental = idRecursoDocumental;
    }

    public String getTituloRecurso() {
        return tituloRecurso;
    }

    public void setTituloRecurso(String tituloRecurso) {
        this.tituloRecurso = tituloRecurso;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
}