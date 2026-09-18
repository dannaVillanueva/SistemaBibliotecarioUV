package mx.uv.fca.sistema_bibliotecario_interno.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "experiencia_educativa")
public class ExperienciaEducativa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idExperienciaEducativa;

    private String nombre;

    private String descripcion;

    private Boolean estado;

    @ManyToMany
    @JoinTable(
            name = "academia_experiencia",
            joinColumns = @JoinColumn(name = "id_experiencia_educativa"),
            inverseJoinColumns = @JoinColumn(name = "id_academia")
    )
    private Set<Academia> academias = new HashSet<>();

    public ExperienciaEducativa() {
    }

    public Integer getIdExperienciaEducativa() {
        return idExperienciaEducativa;
    }

    public void setIdExperienciaEducativa(Integer idExperienciaEducativa) {
        this.idExperienciaEducativa = idExperienciaEducativa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Set<Academia> getAcademias() {
        return academias;
    }

    public void setAcademias(Set<Academia> academias) {
        this.academias = academias;
    }
}