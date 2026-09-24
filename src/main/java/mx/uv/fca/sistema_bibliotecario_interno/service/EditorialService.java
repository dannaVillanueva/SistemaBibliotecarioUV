package mx.uv.fca.sistema_bibliotecario_interno.service;

import mx.uv.fca.sistema_bibliotecario_interno.model.Editorial;
import mx.uv.fca.sistema_bibliotecario_interno.repository.EditorialRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EditorialService {

    private final EditorialRepository repository;

    public EditorialService(EditorialRepository repository) {
        this.repository = repository;
    }

    public List<Editorial> listar() {
        return repository.findAll();
    }

    public Optional<Editorial> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public Editorial guardar(Editorial editorial) {
        return repository.save(editorial);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}