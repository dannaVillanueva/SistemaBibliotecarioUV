package mx.uv.fca.sistema_bibliotecario_interno.repository;

import mx.uv.fca.sistema_bibliotecario_interno.model.Descarte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DescarteRepository
        extends JpaRepository<Descarte, Integer> {

    List<Descarte> findByRecursoDocumental_IdRecursoDocumental(
            Integer idRecursoDocumental
    );

    List<Descarte> findByUsuario_IdUsuario(
            Integer idUsuario
    );
}