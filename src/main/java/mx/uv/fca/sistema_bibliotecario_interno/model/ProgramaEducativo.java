package mx.uv.fca.sistema_bibliotecario_interno.model;

import jakarta.persistence.*;

@Entity
@Table(name = "programa_educativo")
public class ProgramaEducativo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProgramaEducativo;

    private String nombre;

    private String anio;

    private String nombreCorto;

    @Column(name = "modalidad", length = 100)
    private String modalidad;

    public String getModalidad() { return modalidad; }

    public void setModalidad(String modalidad) { this.modalidad = modalidad; }

    public ProgramaEducativo() {
    }

    public Integer getIdProgramaEducativo() {
        return idProgramaEducativo;
    }

    public void setIdProgramaEducativo(Integer idProgramaEducativo) {
        this.idProgramaEducativo = idProgramaEducativo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getNombreCorto() {
        return nombreCorto;
    }

    public void setNombreCorto(String nombreCorto) {
        this.nombreCorto = nombreCorto;
    }
}