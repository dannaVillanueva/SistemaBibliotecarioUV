package mx.uv.fca.sistema_bibliotecario_interno.service;

import mx.uv.fca.sistema_bibliotecario_interno.model.DetalleCotizacion;
import mx.uv.fca.sistema_bibliotecario_interno.model.Editorial;
import mx.uv.fca.sistema_bibliotecario_interno.repository.EditorialRepository;
import java.net.URI;
import java.util.Locale;
import mx.uv.fca.sistema_bibliotecario_interno.repository.DetalleCotizacionRepository;
import mx.uv.fca.sistema_bibliotecario_interno.specification.DetalleCotizacionSpecification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DetalleCotizacionService {

    private final DetalleCotizacionRepository repository;
    private final EditorialRepository editorialRepository;

    public DetalleCotizacionService(
            DetalleCotizacionRepository repository,
            EditorialRepository editorialRepository) {

        this.repository = repository;
        this.editorialRepository = editorialRepository;
    }

    public List<DetalleCotizacion> listar() {
        return repository.findAll();
    }

    public Optional<DetalleCotizacion> buscarPorId(
            Integer id) {

        return repository.findById(id);
    }

    public List<DetalleCotizacion> buscar(
            String estado,
            LocalDate fecha,
            Integer idRecursoDocumental,
            Integer idProgramaEducativo,
            Integer idAcademia,
            String filu) {

        Specification<DetalleCotizacion> spec =
                Specification.where(
                                DetalleCotizacionSpecification.estadoIgual(estado)
                        )
                        .and(
                                DetalleCotizacionSpecification.fechaIgual(fecha)
                        )
                        .and(
                                DetalleCotizacionSpecification.recursoIgual(
                                        idRecursoDocumental
                                )
                        )
                        .and(
                                DetalleCotizacionSpecification.programaIgual(
                                        idProgramaEducativo
                                )
                        )
                        .and(
                                DetalleCotizacionSpecification.academiaIgual(
                                        idAcademia
                                )
                        )
                        .and(
                                DetalleCotizacionSpecification.filuIgual(filu)
                        );

        return repository.findAll(spec);
    }

    public DetalleCotizacion guardar(
            DetalleCotizacion detalle) {

        if (detalle == null) {
            throw new IllegalArgumentException(
                    "El detalle es obligatorio."
            );
        }

        if (detalle.getCotizacion() == null
                || detalle.getCotizacion().getIdCotizacion() == null) {

            throw new IllegalArgumentException(
                    "La cotización es obligatoria."
            );
        }

        boolean libroRegistrado =
                detalle.getRecursoDocumental() != null;

        boolean libroExterno =
                detalle.getTituloExterno() != null
                        && !detalle.getTituloExterno().isBlank();

        if (!libroRegistrado && !libroExterno) {
            throw new IllegalArgumentException(
                    "Selecciona un libro registrado "
                            + "o escribe el título de un libro externo."
            );
        }

        if (libroRegistrado && libroExterno) {
            throw new IllegalArgumentException(
                    "No puedes indicar dos orígenes "
                            + "para el mismo libro."
            );
        }

        if (detalle.getCantidad() == null
                || detalle.getCantidad() <= 0) {

            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero."
            );
        }

        BigDecimal precio =
                detalle.getPrecioUnitarioEstimado();

        if (precio == null
                || precio.compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "El precio no puede ser negativo."
            );
        }

        if (precio.compareTo(
                new BigDecimal("99999999.99")) > 0) {

            throw new IllegalArgumentException(
                    "El precio supera el límite permitido."
            );
        }

        String filu = detalle.getFilu();

        if (filu != null && !filu.isBlank()) {

            filu = filu.trim().toUpperCase();

            if (!filu.equals("SI") && !filu.equals("NO")) {
                throw new IllegalArgumentException(
                        "FILU debe ser SI o NO."
                );
            }

            detalle.setFilu(filu);
        }

        precio = precio.setScale(
                2,
                RoundingMode.HALF_UP
        );

        BigDecimal subtotal = precio.multiply(
                BigDecimal.valueOf(detalle.getCantidad())
        );

        if (subtotal.compareTo(
                new BigDecimal("99999999.99")) > 0) {

            throw new IllegalArgumentException(
                    "El subtotal supera el límite permitido."
            );
        }

        detalle.setPrecioUnitarioEstimado(precio);

        detalle.setSubtotal(
                subtotal.setScale(
                        2,
                        RoundingMode.HALF_UP
                )
        );

        normalizarCamposAdquisicion(detalle);
        return repository.save(detalle);
    }

    private void normalizarCamposAdquisicion(DetalleCotizacion d) {
        String estado = d.getEstadoAdquisicion();
        estado = estado == null || estado.isBlank() ? "PENDIENTE" : estado.trim().toUpperCase(Locale.ROOT);
        if (!List.of("PENDIENTE", "COTIZADO", "AUTORIZADO", "COMPRADO", "RECIBIDO", "CANCELADO").contains(estado))
            throw new IllegalArgumentException("Estado de adquisición no permitido.");
        d.setEstadoAdquisicion(estado);

        String url = d.getUrlCotizacion();
        if (url != null && !url.isBlank()) {
            url = url.trim();
            if (url.length() > 2048) throw new IllegalArgumentException("URL demasiado larga.");
            try {
                URI uri = URI.create(url);
                if (!("https".equalsIgnoreCase(uri.getScheme()) || "http".equalsIgnoreCase(uri.getScheme()))
                        || uri.getHost() == null) throw new IllegalArgumentException("URL no válida.");
            } catch (Exception e) { throw new IllegalArgumentException("La URL debe iniciar con http:// o https:// y ser válida."); }
            d.setUrlCotizacion(url);
        } else d.setUrlCotizacion(null);

        if (d.getRecursoDocumental() != null) {
            d.setEditorial(null);
            d.setEditorialExterna(null);
            return;
        }
        String nombre = d.getEditorialExterna() == null ? "" : d.getEditorialExterna().trim();
        Integer id = d.getEditorial() == null ? null : d.getEditorial().getIdEditorial();
        if (id != null) {
            Editorial editorial = editorialRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("La editorial seleccionada no existe."));
            d.setEditorial(editorial);
            d.setEditorialExterna(editorial.getNombre());
        } else if (!nombre.isEmpty()) {
            if (nombre.length() > 255) throw new IllegalArgumentException("Nombre de editorial demasiado largo.");
            Editorial editorial = editorialRepository.findByNombreIgnoreCase(nombre).orElseGet(() -> {
                Editorial nueva = new Editorial();
                nueva.setNombre(nombre);
                return editorialRepository.save(nueva);
            });
            d.setEditorial(editorial);
            d.setEditorialExterna(editorial.getNombre());
        } else {
            d.setEditorial(null);
            d.setEditorialExterna(null);
        }
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}