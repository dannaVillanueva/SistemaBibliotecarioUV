package mx.uv.fca.sistema_bibliotecario_interno.dto;

import java.time.LocalDate;

public class BajaResponseDTO {

    private Integer idBaja;
    private LocalDate fechaBaja;
    private String motivo;
    private String observaciones;
    private Integer cantidad;

    private Integer idRecursoDocumental;
    private String tituloRecurso;
    private String isbn;

    private Integer idUsuario;
    private String nombreUsuario;

    public BajaResponseDTO() {
    }

    public BajaResponseDTO(
            Integer idBaja,
            LocalDate fechaBaja,
            String motivo,
            String observaciones,
            Integer cantidad,
            Integer idRecursoDocumental,
            String tituloRecurso,
            String isbn,
            Integer idUsuario,
            String nombreUsuario) {

        this.idBaja = idBaja;
        this.fechaBaja = fechaBaja;
        this.motivo = motivo;
        this.observaciones = observaciones;
        this.cantidad = cantidad;
        this.idRecursoDocumental = idRecursoDocumental;
        this.tituloRecurso = tituloRecurso;
        this.isbn = isbn;
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
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

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
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