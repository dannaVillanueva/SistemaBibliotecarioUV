package mx.uv.fca.sistema_bibliotecario_interno.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(
        name = "vinculacion_academica_recurso",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_vinculacion_academica",
                columnNames = {
                        "id_recurso_documental",
                        "id_experiencia_educativa",
                        "id_academia",
                        "id_programa_educativo"
                }
        )
)
public class VinculacionAcademicaRecurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idVinculacion;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "id_recurso_documental",
            nullable = false
    )
    private RecursoDocumental recursoDocumental;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(
            name = "id_experiencia_educativa",
            nullable = false
    )
    private ExperienciaEducativa experienciaEducativa;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(
            name = "id_academia",
            nullable = false
    )
    private Academia academia;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(
            name = "id_programa_educativo",
            nullable = false
    )
    private ProgramaEducativo programaEducativo;

    public VinculacionAcademicaRecurso() {
    }

    public Integer getIdVinculacion() {
        return idVinculacion;
    }

    public void setIdVinculacion(Integer idVinculacion) {
        this.idVinculacion = idVinculacion;
    }

    public RecursoDocumental getRecursoDocumental() {
        return recursoDocumental;
    }

    public void setRecursoDocumental(
            RecursoDocumental recursoDocumental) {
        this.recursoDocumental = recursoDocumental;
    }

    public ExperienciaEducativa getExperienciaEducativa() {
        return experienciaEducativa;
    }

    public void setExperienciaEducativa(
            ExperienciaEducativa experienciaEducativa) {
        this.experienciaEducativa = experienciaEducativa;
    }

    public Academia getAcademia() {
        return academia;
    }

    public void setAcademia(Academia academia) {
        this.academia = academia;
    }

    public ProgramaEducativo getProgramaEducativo() {
        return programaEducativo;
    }

    public void setProgramaEducativo(
            ProgramaEducativo programaEducativo) {
        this.programaEducativo = programaEducativo;
    }
}