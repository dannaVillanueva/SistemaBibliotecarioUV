package mx.uv.fca.sistema_bibliotecario_interno.repository;

import mx.uv.fca.sistema_bibliotecario_interno.model.Cotizacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CotizacionRepository
        extends JpaRepository<Cotizacion, Integer> {
}