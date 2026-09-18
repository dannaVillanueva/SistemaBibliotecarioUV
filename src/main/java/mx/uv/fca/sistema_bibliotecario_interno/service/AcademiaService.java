package mx.uv.fca.sistema_bibliotecario_interno.service;

import mx.uv.fca.sistema_bibliotecario_interno.model.Academia;
import mx.uv.fca.sistema_bibliotecario_interno.repository.AcademiaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AcademiaService {

    private final AcademiaRepository repository;

    public AcademiaService(AcademiaRepository repository) {
        this.repository = repository;
    }

    public List<Academia> listar() {
        return repository.findAll();
    }

    public Optional<Academia> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public Academia guardar(Academia academia) {
        return repository.save(academia);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}