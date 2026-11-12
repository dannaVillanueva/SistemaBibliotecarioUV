package mx.uv.fca.sistema_bibliotecario_interno.service;

import mx.uv.fca.sistema_bibliotecario_interno.dto.CotizacionSalidaDTO;
import mx.uv.fca.sistema_bibliotecario_interno.dto.SalidaCotizacionDTO;
import mx.uv.fca.sistema_bibliotecario_interno.model.Cotizacion;
import mx.uv.fca.sistema_bibliotecario_interno.model.DetalleCotizacion;
import mx.uv.fca.sistema_bibliotecario_interno.model.RecursoDocumental;
import mx.uv.fca.sistema_bibliotecario_interno.repository.CotizacionRepository;
import mx.uv.fca.sistema_bibliotecario_interno.repository.DetalleCotizacionRepository;
import mx.uv.fca.sistema_bibliotecario_interno.specification.DetalleCotizacionSpecification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class SalidaCotizacionService {

    private final CotizacionRepository cotizacionRepository;
    private final DetalleCotizacionRepository detalleRepository;

    public SalidaCotizacionService(
            CotizacionRepository cotizacionRepository,
            DetalleCotizacionRepository detalleRepository) {

        this.cotizacionRepository = cotizacionRepository;
        this.detalleRepository = detalleRepository;
    }

    // SALIDA DE UNA COTIZACIÓN
    public CotizacionSalidaDTO obtenerSalida(Integer idCotizacion) {

        Cotizacion cotizacion = cotizacionRepository
                .findById(idCotizacion)
                .orElseThrow(() ->
                        new RuntimeException("Cotización no encontrada"));

        List<DetalleCotizacion> detalles =
                detalleRepository
                        .findByCotizacion_IdCotizacion(idCotizacion);

        return convertirACotizacionSalida(cotizacion, detalles);
    }


    // BÚSQUEDA CON FILTROS
    public List<CotizacionSalidaDTO> buscarCotizaciones(
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
                        .and(DetalleCotizacionSpecification.fechaIgual(fecha))
                        .and(DetalleCotizacionSpecification.recursoIgual(
                                idRecursoDocumental))
                        .and(DetalleCotizacionSpecification.programaIgual(
                                idProgramaEducativo))
                        .and(DetalleCotizacionSpecification.academiaIgual(
                                idAcademia))
                        .and(DetalleCotizacionSpecification.filuIgual(filu));

        List<DetalleCotizacion> resultados =
                detalleRepository.findAll(spec);

        /*
         * Agrupamos los detalles encontrados por cotización.
         *
         * LinkedHashMap conserva el orden en que fueron encontrados.
         */
        Map<Integer, List<DetalleCotizacion>> agrupados =
                new LinkedHashMap<>();

        for (DetalleCotizacion detalle : resultados) {

            Integer idCotizacion =
                    detalle.getCotizacion().getIdCotizacion();

            agrupados
                    .computeIfAbsent(
                            idCotizacion,
                            key -> new ArrayList<>()
                    )
                    .add(detalle);
        }

        List<CotizacionSalidaDTO> salida =
                new ArrayList<>();

        for (Map.Entry<Integer, List<DetalleCotizacion>> entry
                : agrupados.entrySet()) {

            List<DetalleCotizacion> detallesFiltrados =
                    entry.getValue();

            Cotizacion cotizacion =
                    detallesFiltrados.get(0).getCotizacion();

            salida.add(
                    convertirACotizacionSalida(
                            cotizacion,
                            detallesFiltrados
                    )
            );
        }

        return salida;
    }


    // CONVIERTE LA COTIZACIÓN AL DTO LIMPIO
    private CotizacionSalidaDTO convertirACotizacionSalida(
            Cotizacion cotizacion,
            List<DetalleCotizacion> detalles) {

        List<SalidaCotizacionDTO> detallesDTO =
                new ArrayList<>();

        BigDecimal total = BigDecimal.ZERO;

        for (DetalleCotizacion detalle : detalles) {

            RecursoDocumental recurso =
                    detalle.getRecursoDocumental();

            SalidaCotizacionDTO dto =
                    new SalidaCotizacionDTO();

            if (detalle.getProgramaEducativo() != null) {
                dto.setProgramaEducativo(
                        detalle
                                .getProgramaEducativo()
                                .getNombre()
                );
            }

            if (detalle.getAcademia() != null) {
                dto.setAcademia(
                        detalle
                                .getAcademia()
                                .getNombre()
                );
            }

            if (recurso != null) {

                dto.setIsbn(recurso.getIsbn());
                dto.setTitulo(recurso.getTitulo());
                dto.setTipo(recurso.getTipo());

                if (recurso.getEditorial() != null) {
                    dto.setEditorial(
                            recurso
                                    .getEditorial()
                                    .getNombre()
                    );
                }
            }

            dto.setCantidad(
                    detalle.getCantidad()
            );

            dto.setPrecioUnitarioEstimado(
                    detalle.getPrecioUnitarioEstimado()
            );

            dto.setSubtotal(
                    detalle.getSubtotal()
            );

            dto.setFilu(
                    detalle.getFilu()
            );

            if (detalle.getSubtotal() != null) {
                total = total.add(
                        detalle.getSubtotal()
                );
            }

            detallesDTO.add(dto);
        }

        CotizacionSalidaDTO salida =
                new CotizacionSalidaDTO();

        salida.setIdCotizacion(
                cotizacion.getIdCotizacion()
        );

        salida.setFechaSolicitud(
                cotizacion.getFechaSolicitud()
        );

        salida.setEstado(
                cotizacion.getEstado()
        );

        salida.setObservaciones(
                cotizacion.getObservaciones()
        );

        salida.setTotal(total);

        salida.setDetalles(detallesDTO);

        return salida;
    }
}