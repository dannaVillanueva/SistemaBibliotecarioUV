package mx.uv.fca.sistema_bibliotecario_interno.specification;

import mx.uv.fca.sistema_bibliotecario_interno.model.DetalleCotizacion;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class DetalleCotizacionSpecification {

    public static Specification<DetalleCotizacion> estadoIgual(
            String estado) {

        return (root, query, cb) -> {

            if (estado == null || estado.isBlank()) {
                return cb.conjunction();
            }

            return cb.equal(
                    cb.lower(
                            root.get("cotizacion")
                                    .get("estado")
                    ),
                    estado.toLowerCase()
            );
        };
    }

    public static Specification<DetalleCotizacion> fechaIgual(
            LocalDate fecha) {

        return (root, query, cb) -> {

            if (fecha == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("cotizacion")
                            .get("fechaSolicitud"),
                    fecha
            );
        };
    }

    public static Specification<DetalleCotizacion> recursoIgual(
            Integer idRecursoDocumental) {

        return (root, query, cb) -> {

            if (idRecursoDocumental == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("recursoDocumental")
                            .get("idRecursoDocumental"),
                    idRecursoDocumental
            );
        };
    }

    public static Specification<DetalleCotizacion> programaIgual(
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

    public static Specification<DetalleCotizacion> academiaIgual(
            Integer idAcademia) {

        return (root, query, cb) -> {

            if (idAcademia == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("academia")
                            .get("idAcademia"),
                    idAcademia
            );
        };
    }

    public static Specification<DetalleCotizacion> filuIgual(
            String filu) {

        return (root, query, cb) -> {

            if (filu == null || filu.isBlank()) {
                return cb.conjunction();
            }

            return cb.equal(
                    cb.lower(root.get("filu")),
                    filu.toLowerCase()
            );
        };
    }
}