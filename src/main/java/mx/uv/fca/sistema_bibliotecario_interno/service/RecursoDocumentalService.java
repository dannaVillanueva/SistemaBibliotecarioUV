package mx.uv.fca.sistema_bibliotecario_interno.service;

import mx.uv.fca.sistema_bibliotecario_interno.specification.RecursoDocumentalSpecification;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;
import mx.uv.fca.sistema_bibliotecario_interno.model.RecursoDocumental;
import mx.uv.fca.sistema_bibliotecario_interno.repository.RecursoDocumentalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecursoDocumentalService {

    private final RecursoDocumentalRepository repository;

    public RecursoDocumentalService(
            RecursoDocumentalRepository repository) {
        this.repository = repository;
    }

    public List<RecursoDocumental> listar() {
        return repository.findAll();
    }

    public Optional<RecursoDocumental> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public RecursoDocumental guardar(RecursoDocumental recurso) {
        return repository.save(recurso);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }

    public List<RecursoDocumental> buscar(
            String titulo,
            String autor,
            String isbn,
            String tipo,
            String anio,
            Integer idEditorial,
            Boolean enCatalogo,
            Integer idExperienciaEducativa,
            Integer idAcademia,
            Integer idProgramaEducativo) {

        Specification<RecursoDocumental> spec =
                Specification
                        .where(
                                RecursoDocumentalSpecification
                                        .tituloContiene(titulo)
                        )
                        .and(
                                RecursoDocumentalSpecification
                                        .autorContiene(autor)
                        )
                        .and(
                                RecursoDocumentalSpecification
                                        .isbnIgual(isbn)
                        )
                        .and(
                                RecursoDocumentalSpecification
                                        .tipoIgual(tipo)
                        )
                        .and(
                                RecursoDocumentalSpecification
                                        .anioIgual(anio)
                        )
                        .and(
                                RecursoDocumentalSpecification
                                        .editorialIgual(idEditorial)
                        )
                        .and(
                                RecursoDocumentalSpecification
                                        .catalogoIgual(enCatalogo)
                        )
                        .and(
                                RecursoDocumentalSpecification
                                        .experienciaEducativaIgual(
                                                idExperienciaEducativa
                                        )
                        )
                        .and(
                                RecursoDocumentalSpecification
                                        .academiaIgual(idAcademia)
                        )
                        .and(
                                RecursoDocumentalSpecification
                                        .programaEducativoIgual(
                                                idProgramaEducativo
                                        )
                        );
        return repository.findAll(spec);
    }
}