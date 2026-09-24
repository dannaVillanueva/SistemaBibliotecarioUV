package mx.uv.fca.sistema_bibliotecario_interno.repository;

import mx.uv.fca.sistema_bibliotecario_interno.model.Donacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonacionRepository
        extends JpaRepository<Donacion, Integer> {
}