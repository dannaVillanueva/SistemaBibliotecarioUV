package mx.uv.fca.sistema_bibliotecario_interno.repository;

import mx.uv.fca.sistema_bibliotecario_interno.model.DetalleCotizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface DetalleCotizacionRepository
        extends JpaRepository<DetalleCotizacion, Integer>,
        JpaSpecificationExecutor<DetalleCotizacion> {

    List<DetalleCotizacion> findByCotizacion_IdCotizacion(
            Integer idCotizacion
    );
}