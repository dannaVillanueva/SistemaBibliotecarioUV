package mx.uv.fca.sistema_bibliotecario_interno.service;

import mx.uv.fca.sistema_bibliotecario_interno.model.ExperienciaEducativa;
import mx.uv.fca.sistema_bibliotecario_interno.repository.ExperienciaEducativaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExperienciaEducativaService {

    private final ExperienciaEducativaRepository repository;

    public ExperienciaEducativaService(
            ExperienciaEducativaRepository repository) {
        this.repository = repository;
    }

    public List<ExperienciaEducativa> listar() {
        return repository.findAll();
    }

    public Optional<ExperienciaEducativa> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public ExperienciaEducativa guardar(
            ExperienciaEducativa experiencia) {
        return repository.save(experiencia);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}