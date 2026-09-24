package mx.uv.fca.sistema_bibliotecario_interno.repository;

import mx.uv.fca.sistema_bibliotecario_interno.model.Baja;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BajaRepository
        extends JpaRepository<Baja, Integer> {

    List<Baja> findByRecursoDocumental_IdRecursoDocumental(
            Integer idRecursoDocumental
    );

    List<Baja> findByUsuario_IdUsuario(
            Integer idUsuario
    );
}