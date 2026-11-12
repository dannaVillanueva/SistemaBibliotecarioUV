package mx.uv.fca.sistema_bibliotecario_interno.repository;

import mx.uv.fca.sistema_bibliotecario_interno.model.Descarte;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DescarteRepository
        extends JpaRepository<Descarte, Integer> {

    List<Descarte> findByRecursoDocumental_IdRecursoDocumental(
            Integer idRecursoDocumental
    );

    List<Descarte> findByUsuario_IdUsuario(Integer idUsuario);

    List<Descarte> findByEstado(String estado);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT d FROM Descarte d WHERE d.idDescarte = :id")
    Optional<Descarte> buscarParaResolver(@Param("id") Integer id);
}