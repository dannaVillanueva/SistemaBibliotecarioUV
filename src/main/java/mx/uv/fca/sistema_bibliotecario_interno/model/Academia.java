package mx.uv.fca.sistema_bibliotecario_interno.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "academia")
public class Academia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAcademia;

    private String nombre;

    @ManyToMany
    @JoinTable(
            name = "academia_programa",
            joinColumns = @JoinColumn(name = "id_academia"),
            inverseJoinColumns = @JoinColumn(name = "id_programa_educativo")
    )
    private Set<ProgramaEducativo> programasEducativos = new HashSet<>();

    public Academia() {
    }

    public Integer getIdAcademia() {
        return idAcademia;
    }

    public void setIdAcademia(Integer idAcademia) {
        this.idAcademia = idAcademia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<ProgramaEducativo> getProgramasEducativos() {
        return programasEducativos;
    }

    public void setProgramasEducativos(
            Set<ProgramaEducativo> programasEducativos) {
        this.programasEducativos = programasEducativos;
    }
}