
package mx.uv.fca.sistema_bibliotecario_interno.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

import mx.uv.fca.sistema_bibliotecario_interno.model.RecursoDocumental;
import mx.uv.fca.sistema_bibliotecario_interno.model.VinculacionAcademicaRecurso;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecursoDocumentalSpecification {

    private RecursoDocumentalSpecification() {
    }

    public static Specification<RecursoDocumental> tituloContiene(
            String titulo) {

        return (root, query, cb) -> {
            if (titulo == null || titulo.isBlank()) {
                return cb.conjunction();
            }

            return cb.like(
                    cb.lower(root.get("titulo")),
                    "%" + titulo.trim().toLowerCase(Locale.ROOT) + "%"
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
                    "%" + autor.trim().toLowerCase(Locale.ROOT) + "%"
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
                    isbn.trim()
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
                    tipo.trim().toLowerCase(Locale.ROOT)
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
                    anio.trim()
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

    public static Specification<RecursoDocumental>
    experienciaEducativaIgual(Integer idExperienciaEducativa) {

        return vinculacionAcademicaIgual(
                idExperienciaEducativa,
                null,
                null
        );
    }

    public static Specification<RecursoDocumental>
    academiaIgual(Integer idAcademia) {

        return vinculacionAcademicaIgual(
                null,
                idAcademia,
                null
        );
    }

    public static Specification<RecursoDocumental>
    programaEducativoIgual(Integer idProgramaEducativo) {

        return vinculacionAcademicaIgual(
                null,
                null,
                idProgramaEducativo
        );
    }

    public static Specification<RecursoDocumental>
    vinculacionAcademicaIgual(
            Integer idExperienciaEducativa,
            Integer idAcademia,
            Integer idProgramaEducativo) {

        return (root, query, cb) -> {

            if (idExperienciaEducativa == null
                    && idAcademia == null
                    && idProgramaEducativo == null) {

                return cb.conjunction();
            }

            // Busca las combinaciones específicas nuevas.
            Subquery<Integer> nuevas = query.subquery(Integer.class);

            Root<VinculacionAcademicaRecurso> vinculacion =
                    nuevas.from(VinculacionAcademicaRecurso.class);

            List<Predicate> condicionesNuevas = new ArrayList<>();

            condicionesNuevas.add(
                    cb.equal(
                            vinculacion.get("recursoDocumental")
                                    .get("idRecursoDocumental"),
                            root.get("idRecursoDocumental")
                    )
            );

            if (idExperienciaEducativa != null) {
                condicionesNuevas.add(
                        cb.equal(
                                vinculacion.get("experienciaEducativa")
                                        .get("idExperienciaEducativa"),
                                idExperienciaEducativa
                        )
                );
            }

            if (idAcademia != null) {
                condicionesNuevas.add(
                        cb.equal(
                                vinculacion.get("academia")
                                        .get("idAcademia"),
                                idAcademia
                        )
                );
            }

            if (idProgramaEducativo != null) {
                condicionesNuevas.add(
                        cb.equal(
                                vinculacion.get("programaEducativo")
                                        .get("idProgramaEducativo"),
                                idProgramaEducativo
                        )
                );
            }

            nuevas.select(
                    vinculacion.get("idVinculacion")
            ).where(
                    cb.and(condicionesNuevas.toArray(new Predicate[0]))
            );

            // Compatibilidad con los recursos anteriores.
            // Solo se usa si todavía no tienen vinculaciones nuevas.
            Subquery<Integer> antiguas = query.subquery(Integer.class);

            Root<RecursoDocumental> recursoAnterior =
                    antiguas.from(RecursoDocumental.class);

            Join<Object, Object> experiencia =
                    recursoAnterior.join("experienciasEducativas");

            List<Predicate> condicionesAntiguas = new ArrayList<>();

            condicionesAntiguas.add(
                    cb.equal(
                            recursoAnterior.get("idRecursoDocumental"),
                            root.get("idRecursoDocumental")
                    )
            );

            if (idExperienciaEducativa != null) {
                condicionesAntiguas.add(
                        cb.equal(
                                experiencia.get("idExperienciaEducativa"),
                                idExperienciaEducativa
                        )
                );
            }

            if (idAcademia != null || idProgramaEducativo != null) {

                Join<Object, Object> academia =
                        experiencia.join("academias");

                if (idAcademia != null) {
                    condicionesAntiguas.add(
                            cb.equal(
                                    academia.get("idAcademia"),
                                    idAcademia
                            )
                    );
                }

                if (idProgramaEducativo != null) {

                    Join<Object, Object> programa =
                            academia.join("programasEducativos");

                    condicionesAntiguas.add(
                            cb.equal(
                                    programa.get("idProgramaEducativo"),
                                    idProgramaEducativo
                            )
                    );
                }
            }

            antiguas.select(
                    recursoAnterior.get("idRecursoDocumental")
            ).where(
                    cb.and(condicionesAntiguas.toArray(new Predicate[0]))
            );

            // Si ya existen vinculaciones nuevas, estas tienen
            // prioridad sobre las relaciones antiguas.
            Subquery<Integer> tieneNuevas =
                    query.subquery(Integer.class);

            Root<VinculacionAcademicaRecurso> existente =
                    tieneNuevas.from(VinculacionAcademicaRecurso.class);

            tieneNuevas.select(
                    existente.get("idVinculacion")
            ).where(
                    cb.equal(
                            existente.get("recursoDocumental")
                                    .get("idRecursoDocumental"),
                            root.get("idRecursoDocumental")
                    )
            );

            return cb.or(
                    cb.exists(nuevas),
                    cb.and(
                            cb.not(cb.exists(tieneNuevas)),
                            cb.exists(antiguas)
                    )
            );
        };
    }
}