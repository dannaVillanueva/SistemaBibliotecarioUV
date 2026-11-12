package mx.uv.fca.sistema_bibliotecario_interno.dto;

import java.math.BigDecimal;

public class ResumenHorasDTO {

    private Integer idAlumno;

    private String nombreCompleto;

    private String matricula;

    private String tipo;

    private BigDecimal horasHoy;

    private BigDecimal horasSemana;

    private BigDecimal horasMes;

    private BigDecimal horasRequeridas;

    private BigDecimal horasRealizadas;

    private BigDecimal horasRestantes;

    private BigDecimal porcentajeAvance;

    public ResumenHorasDTO() {
    }

    public Integer getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(Integer idAlumno) {
        this.idAlumno = idAlumno;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getHorasHoy() {
        return horasHoy;
    }

    public void setHorasHoy(BigDecimal horasHoy) {
        this.horasHoy = horasHoy;
    }

    public BigDecimal getHorasSemana() {
        return horasSemana;
    }

    public void setHorasSemana(BigDecimal horasSemana) {
        this.horasSemana = horasSemana;
    }

    public BigDecimal getHorasMes() {
        return horasMes;
    }

    public void setHorasMes(BigDecimal horasMes) {
        this.horasMes = horasMes;
    }

    public BigDecimal getHorasRequeridas() {
        return horasRequeridas;
    }

    public void setHorasRequeridas(BigDecimal horasRequeridas) {
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
}