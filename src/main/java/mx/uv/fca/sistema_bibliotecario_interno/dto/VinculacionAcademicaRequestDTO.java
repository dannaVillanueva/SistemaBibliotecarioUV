package mx.uv.fca.sistema_bibliotecario_interno.dto;

public class VinculacionAcademicaRequestDTO {

    private Integer idExperienciaEducativa;
    private Integer idAcademia;
    private Integer idProgramaEducativo;

    public VinculacionAcademicaRequestDTO() {
    }

    public Integer getIdExperienciaEducativa() {
        return idExperienciaEducativa;
    }

    public void setIdExperienciaEducativa(
            Integer idExperienciaEducativa) {
        this.idExperienciaEducativa = idExperienciaEducativa;
    }

    public Integer getIdAcademia() {
        return idAcademia;
    }

    public void setIdAcademia(Integer idAcademia) {
        this.idAcademia = idAcademia;
    }

    public Integer getIdProgramaEducativo() {
        return idProgramaEducativo;
    }

    public void setIdProgramaEducativo(
            Integer idProgramaEducativo) {
        this.idProgramaEducativo = idProgramaEducativo;
    }
}