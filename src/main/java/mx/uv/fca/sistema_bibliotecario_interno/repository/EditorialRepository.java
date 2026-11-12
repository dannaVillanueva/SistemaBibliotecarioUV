package mx.uv.fca.sistema_bibliotecario_interno.repository;

import mx.uv.fca.sistema_bibliotecario_interno.model.Editorial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EditorialRepository
        extends JpaRepository<Editorial, Integer> {

    Optional<Editorial> findByNombreIgnoreCase(String nombre);
}
