
package mx.uv.fca.sistema_bibliotecario_interno.repository;

import mx.uv.fca.sistema_bibliotecario_interno.model.ProgramaEducativo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramaEducativoRepository
        extends JpaRepository<ProgramaEducativo, Integer> {
}
