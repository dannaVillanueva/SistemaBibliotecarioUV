package mx.uv.fca.sistema_bibliotecario_interno.service;

import mx.uv.fca.sistema_bibliotecario_interno.model.Donacion;
import mx.uv.fca.sistema_bibliotecario_interno.repository.DonacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DonacionService {

    private final DonacionRepository repository;

    public DonacionService(DonacionRepository repository) {
        this.repository = repository;
    }

    public List<Donacion> listar() {
        return repository.findAll();
    }

    public Optional<Donacion> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public Donacion guardar(Donacion donacion) {
        return repository.save(donacion);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}
