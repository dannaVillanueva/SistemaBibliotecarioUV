package mx.uv.fca.sistema_bibliotecario_interno.repository;

import mx.uv.fca.sistema_bibliotecario_interno.model.RecursoDocumental;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface RecursoDocumentalRepository
        extends JpaRepository<RecursoDocumental, Integer>,
        JpaSpecificationExecutor<RecursoDocumental> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
       SELECT r
       FROM RecursoDocumental r
       WHERE r.idRecursoDocumental = :id
       """)
    Optional<RecursoDocumental> buscarParaActualizar(
            @Param("id") Integer id
    );

}