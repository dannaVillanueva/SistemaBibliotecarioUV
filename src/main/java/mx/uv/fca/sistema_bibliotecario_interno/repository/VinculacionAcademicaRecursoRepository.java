
package mx.uv.fca.sistema_bibliotecario_interno.repository;

import mx.uv.fca.sistema_bibliotecario_interno.model.VinculacionAcademicaRecurso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VinculacionAcademicaRecursoRepository
        extends JpaRepository<VinculacionAcademicaRecurso, Integer> {

    List<VinculacionAcademicaRecurso>
    findByRecursoDocumental_IdRecursoDocumental(
            Integer idRecursoDocumental
    );
}
