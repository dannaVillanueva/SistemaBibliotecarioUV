package mx.uv.fca.sistema_bibliotecario_interno.repository;

import mx.uv.fca.sistema_bibliotecario_interno.model.RecursoDocumental;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RecursoDocumentalRepository
        extends JpaRepository<RecursoDocumental, Integer>,
        JpaSpecificationExecutor<RecursoDocumental> {
}