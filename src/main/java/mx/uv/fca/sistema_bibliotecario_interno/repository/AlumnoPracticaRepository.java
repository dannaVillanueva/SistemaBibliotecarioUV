package mx.uv.fca.sistema_bibliotecario_interno.repository;

import mx.uv.fca.sistema_bibliotecario_interno.model.AlumnoPractica;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AlumnoPracticaRepository
        extends JpaRepository<AlumnoPractica, Integer>,
        JpaSpecificationExecutor<AlumnoPractica> {

    Optional<AlumnoPractica> findByMatricula(
            String matricula
    );

    boolean existsByMatricula(
            String matricula
    );

    List<AlumnoPractica> findByActivo(
            Boolean activo
    );

    // Bloqueo transaccional para evitar
    // registros simultáneos conflictivos
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT a
        FROM AlumnoPractica a
        WHERE a.idAlumno = :idAlumno
    """)
    Optional<AlumnoPractica> buscarParaActualizar(
            @Param("idAlumno") Integer idAlumno
    );
}