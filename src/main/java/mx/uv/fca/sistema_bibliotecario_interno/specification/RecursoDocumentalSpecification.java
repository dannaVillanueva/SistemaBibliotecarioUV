package mx.uv.fca.sistema_bibliotecario_interno.specification;

import mx.uv.fca.sistema_bibliotecario_interno.model.RecursoDocumental;

import org.springframework.data.jpa.domain.Specification;

public class RecursoDocumentalSpecification {

    public static Specification<RecursoDocumental> tituloContiene(
            String titulo) {

        return (root, query, cb) -> {

            if (titulo == null || titulo.isBlank()) {
                return cb.conjunction();
            }

            return cb.like(
                    cb.lower(root.get("titulo")),
                    "%" + titulo.toLowerCase() + "%"
            );
        };
    }

    public static Specification<RecursoDocumental> autorContiene(
            String autor) {

        return (root, query, cb) -> {

            if (autor == null || autor.isBlank()) {
                return cb.conjunction();
            }

            return cb.like(
                    cb.lower(root.get("autor")),
                    "%" + autor.toLowerCase() + "%"
            );
        };
    }

    public static Specification<RecursoDocumental> isbnIgual(
            String isbn) {

        return (root, query, cb) -> {

            if (isbn == null || isbn.isBlank()) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("isbn"),
                    isbn
            );
        };
    }

    public static Specification<RecursoDocumental> tipoIgual(
            String tipo) {

        return (root, query, cb) -> {

            if (tipo == null || tipo.isBlank()) {
                return cb.conjunction();
            }

            return cb.equal(
                    cb.lower(root.get("tipo")),
                    tipo.toLowerCase()
            );
        };
    }

    public static Specification<RecursoDocumental> anioIgual(
            String anio) {

        return (root, query, cb) -> {

            if (anio == null || anio.isBlank()) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("anio"),
                    anio
            );
        };
    }

    public static Specification<RecursoDocumental> editorialIgual(
            Integer idEditorial) {

        return (root, query, cb) -> {

            if (idEditorial == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("editorial").get("idEditorial"),
                    idEditorial
            );
        };
    }

    public static Specification<RecursoDocumental> catalogoIgual(
            Boolean enCatalogo) {

        return (root, query, cb) -> {

            if (enCatalogo == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("enCatalogoBiblioFca"),
                    enCatalogo
            );
        };
    }

    public static Specification<RecursoDocumental> experienciaEducativaIgual(
            Integer idExperienciaEducativa) {

        return (root, query, cb) -> {

            if (idExperienciaEducativa == null) {
                return cb.conjunction();
            }

            query.distinct(true);

            return cb.equal(
                    root.join("experienciasEducativas")
                            .get("idExperienciaEducativa"),
                    idExperienciaEducativa
            );
        };
    }


    public static Specification<RecursoDocumental> academiaIgual(
            Integer idAcademia) {

        return (root, query, cb) -> {

            if (idAcademia == null) {
                return cb.conjunction();
            }

            query.distinct(true);

            return cb.equal(
                    root.join("experienciasEducativas")
                            .join("academias")
                            .get("idAcademia"),
                    idAcademia
            );
        };
    }


    public static Specification<RecursoDocumental> programaEducativoIgual(
            Integer idProgramaEducativo) {

        return (root, query, cb) -> {

            if (idProgramaEducativo == null) {
                return cb.conjunction();
            }

            query.distinct(true);

            return cb.equal(
                    root.join("experienciasEducativas")
                            .join("academias")
                            .join("programasEducativos")
                            .get("idProgramaEducativo"),
                    idProgramaEducativo
            );
        };
    }



}