package mx.uv.fca.sistema_bibliotecario_interno.service;

import mx.uv.fca.sistema_bibliotecario_interno.model.DetalleCotizacion;
import mx.uv.fca.sistema_bibliotecario_interno.repository.DetalleCotizacionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class DetalleCotizacionService {

    private final DetalleCotizacionRepository repository;

    public DetalleCotizacionService(
            DetalleCotizacionRepository repository) {
        this.repository = repository;
    }

    public List<DetalleCotizacion> listar() {
        return repository.findAll();
    }

    public Optional<DetalleCotizacion> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public DetalleCotizacion guardar(DetalleCotizacion detalle) {

        // Calcular automáticamente el subtotal
        if (detalle.getCantidad() != null
                && detalle.getPrecioUnitarioEstimado() != null) {

            BigDecimal subtotal =
                    detalle.getPrecioUnitarioEstimado()
                            .multiply(
                                    BigDecimal.valueOf(
                                            detalle.getCantidad()
                                    )
                            );

            detalle.setSubtotal(subtotal);
        }

        return repository.save(detalle);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}