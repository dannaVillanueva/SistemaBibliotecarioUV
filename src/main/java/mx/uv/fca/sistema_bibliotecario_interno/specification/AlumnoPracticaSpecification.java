package mx.uv.fca.sistema_bibliotecario_interno.specification;

import mx.uv.fca.sistema_bibliotecario_interno.model.AlumnoPractica;
import mx.uv.fca.sistema_bibliotecario_interno.model.TipoPractica;

import org.springframework.data.jpa.domain.Specification;

public class AlumnoPracticaSpecification {

    public static Specification<AlumnoPractica> matriculaContiene(
            String matricula) {

        return (root, query, cb) -> {

            if (matricula == null || matricula.isBlank()) {
                return cb.conjunction();
            }

            return cb.like(
                    cb.lower(root.get("matricula")),
                    "%" + matricula.toLowerCase() + "%"
            );
        };
    }

    public static Specification<AlumnoPractica> nombreContiene(
            String nombre) {

        return (root, query, cb) -> {

            if (nombre == null || nombre.isBlank()) {
                return cb.conjunction();
            }

            String texto =
                    "%" + nombre.toLowerCase() + "%";

            return cb.or(
                    cb.like(
                            cb.lower(root.get("nombre")),
                            texto
                    ),
                    cb.like(
                            cb.lower(root.get("apellidos")),
                            texto
                    )
            );
        };
    }

    public static Specification<AlumnoPractica> tipoIgual(
            TipoPractica tipo) {

        return (root, query, cb) -> {

            if (tipo == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("tipo"),
                    tipo
            );
        };
    }

    public static Specification<AlumnoPractica> activoIgual(
            Boolean activo) {

        return (root, query, cb) -> {

            if (activo == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("activo"),
                    activo
            );
        };
    }

    public static Specification<AlumnoPractica> programaEducativoIgual(
            Integer idProgramaEducativo) {

        return (root, query, cb) -> {

            if (idProgramaEducativo == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("programaEducativo")
                            .get("idProgramaEducativo"),
                    idProgramaEducativo
            );
        };
    }
}