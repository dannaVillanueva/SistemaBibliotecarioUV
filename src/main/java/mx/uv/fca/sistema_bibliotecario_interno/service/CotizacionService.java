package mx.uv.fca.sistema_bibliotecario_interno.service;

import mx.uv.fca.sistema_bibliotecario_interno.model.Cotizacion;
import mx.uv.fca.sistema_bibliotecario_interno.repository.CotizacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CotizacionService {

    private final CotizacionRepository repository;

    public CotizacionService(CotizacionRepository repository) {
        this.repository = repository;
    }

    public List<Cotizacion> listar() {
        return repository.findAll();
    }

    public Optional<Cotizacion> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public Cotizacion guardar(Cotizacion cotizacion) {
        return repository.save(cotizacion);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}