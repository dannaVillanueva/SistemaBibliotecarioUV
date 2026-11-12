package mx.uv.fca.sistema_bibliotecario_interno.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AlumnoPracticaResponseDTO {

    private Integer idAlumno;
    private String matricula;
    private String nombre;
    private String apellidos;
    private String nombreCompleto;
    private String correo;
    private String telefono;
    private String tipo;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    private Integer horasRequeridas;
    private BigDecimal horasRealizadas;
    private BigDecimal horasRestantes;
    private BigDecimal porcentajeAvance;

    private Boolean activo;
    private String estado;

    private Integer idProgramaEducativo;
    private String programaEducativo;
    private String programaEducativoCorto;

    public AlumnoPracticaResponseDTO() {
    }

    public Integer getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(Integer idAlumno) {
        this.idAlumno = idAlumno;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Integer getHorasRequeridas() {
        return horasRequeridas;
    }

    public void setHorasRequeridas(Integer horasRequeridas) {
        this.horasRequeridas = horasRequeridas;
    }

    public BigDecimal getHorasRealizadas() {
        return horasRealizadas;
    }

    public void setHorasRealizadas(BigDecimal horasRealizadas) {
        this.horasRealizadas = horasRealizadas;
    }

    public BigDecimal getHorasRestantes() {
        return horasRestantes;
    }

    public void setHorasRestantes(BigDecimal horasRestantes) {
        this.horasRestantes = horasRestantes;
    }

    public BigDecimal getPorcentajeAvance() {
        return porcentajeAvance;
    }

    public void setPorcentajeAvance(BigDecimal porcentajeAvance) {
        this.porcentajeAvance = porcentajeAvance;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getIdProgramaEducativo() {
        return idProgramaEducativo;
    }

    public void setIdProgramaEducativo(Integer idProgramaEducativo) {
        this.idProgramaEducativo = idProgramaEducativo;
    }

    public String getProgramaEducativo() {
        return programaEducativo;
    }

    public void setProgramaEducativo(String programaEducativo) {
        this.programaEducativo = programaEducativo;
    }

    public String getProgramaEducativoCorto() {
        return programaEducativoCorto;
    }

    public void setProgramaEducativoCorto(String programaEducativoCorto) {
        this.programaEducativoCorto = programaEducativoCorto;
    }
}