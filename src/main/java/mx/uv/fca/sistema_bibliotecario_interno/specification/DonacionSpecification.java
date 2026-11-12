package mx.uv.fca.sistema_bibliotecario_interno.specification;

import mx.uv.fca.sistema_bibliotecario_interno.model.Donacion;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DonacionSpecification {

    public static Specification<Donacion> nombreDonanteContiene(
            String nombreDonante) {

        return (root, query, cb) -> {

            if (nombreDonante == null || nombreDonante.isBlank()) {
                return cb.conjunction();
            }

            return cb.like(
                    cb.lower(root.get("nombreDonante")),
                    "%" + nombreDonante.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Donacion> fechaIgual(
            LocalDate fechaDonacion) {

        return (root, query, cb) -> {

            if (fechaDonacion == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("fechaDonacion"),
                    fechaDonacion
            );
        };
    }

    public static Specification<Donacion> recursoIgual(
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

    public static Specification<Donacion> tituloRecursoContiene(
            String titulo) {

        return (root, query, cb) -> {

            if (titulo == null || titulo.isBlank()) {
                return cb.conjunction();
            }

            return cb.like(
                    cb.lower(
                            root.get("recursoDocumental")
                                    .get("titulo")
                    ),
                    "%" + titulo.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Donacion> isbnIgual(
            String isbn) {

        return (root, query, cb) -> {

            if (isbn == null || isbn.isBlank()) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("recursoDocumental")
                            .get("isbn"),
                    isbn
            );
        };
    }

    public static Specification<Donacion> precioMinimo(
            BigDecimal precioMinimo) {

        return (root, query, cb) -> {

            if (precioMinimo == null) {
                return cb.conjunction();
            }

            return cb.greaterThanOrEqualTo(
                    root.get("precioEstimado"),
                    precioMinimo
            );
        };
    }

    public static Specification<Donacion> precioMaximo(
            BigDecimal precioMaximo) {

        return (root, query, cb) -> {

            if (precioMaximo == null) {
                return cb.conjunction();
            }

            return cb.lessThanOrEqualTo(
                    root.get("precioEstimado"),
                    precioMaximo
            );
        };
    }
}