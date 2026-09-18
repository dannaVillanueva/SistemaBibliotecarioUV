package mx.uv.fca.sistema_bibliotecario_interno.service;

import mx.uv.fca.sistema_bibliotecario_interno.model.ProgramaEducativo;
import mx.uv.fca.sistema_bibliotecario_interno.repository.ProgramaEducativoRepository;
import org.springframework.stereotype.Service;

    import java.util.List;
    import java.util.Optional;

    @Service
    public class ProgramaEducativoService {

        private final ProgramaEducativoRepository repository;

        public ProgramaEducativoService(ProgramaEducativoRepository repository) {
            this.repository = repository;
        }

        public List<ProgramaEducativo> listar() {
            return repository.findAll();
        }

        public Optional<ProgramaEducativo> buscarPorId(Integer id) {
            return repository.findById(id);
        }

        public ProgramaEducativo guardar(ProgramaEducativo programa) {
            return repository.save(programa);
        }

        public void eliminar(Integer id) {
            repository.deleteById(id);
        }
    }
