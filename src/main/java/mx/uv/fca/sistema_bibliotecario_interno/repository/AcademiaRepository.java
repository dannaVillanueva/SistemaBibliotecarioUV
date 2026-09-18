package mx.uv.fca.sistema_bibliotecario_interno.repository;

import mx.uv.fca.sistema_bibliotecario_interno.model.Academia;
import org.springframework.data.jpa.repository.*;

public interface AcademiaRepository
    extends JpaRepository<Academia, Integer>{
}