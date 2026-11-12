package mx.uv.fca.sistema_bibliotecario_interno.repository;

import mx.uv.fca.sistema_bibliotecario_interno.model.HistorialCotizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HistorialCotizacionRepository extends JpaRepository<HistorialCotizacion, Long> {
    List<HistorialCotizacion> findAllByOrderByFechaHoraDescIdHistorialDesc();
    List<HistorialCotizacion> findByIdCotizacionOrderByFechaHoraDescIdHistorialDesc(Integer idCotizacion);
}
