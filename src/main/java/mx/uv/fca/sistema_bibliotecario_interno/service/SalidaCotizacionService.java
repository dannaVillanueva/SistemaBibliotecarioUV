package mx.uv.fca.sistema_bibliotecario_interno.service;

import mx.uv.fca.sistema_bibliotecario_interno.dto.CotizacionSalidaDTO;
import mx.uv.fca.sistema_bibliotecario_interno.dto.SalidaCotizacionDTO;
import mx.uv.fca.sistema_bibliotecario_interno.model.Cotizacion;
import mx.uv.fca.sistema_bibliotecario_interno.model.DetalleCotizacion;
import mx.uv.fca.sistema_bibliotecario_interno.model.RecursoDocumental;
import mx.uv.fca.sistema_bibliotecario_interno.repository.CotizacionRepository;
import mx.uv.fca.sistema_bibliotecario_interno.repository.DetalleCotizacionRepository;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class SalidaCotizacionService {

    private final DetalleCotizacionRepository detalleRepository;
    private final CotizacionRepository cotizacionRepository;

    public SalidaCotizacionService(
            DetalleCotizacionRepository detalleRepository,
            CotizacionRepository cotizacionRepository) {

        this.detalleRepository = detalleRepository;
        this.cotizacionRepository = cotizacionRepository;
    }

    public CotizacionSalidaDTO obtenerSalida(
            Integer idCotizacion) {

        Cotizacion cotizacion =
                cotizacionRepository
                        .findById(idCotizacion)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Cotización no encontrada"
                                )
                        );

        List<DetalleCotizacion> detalles =
                detalleRepository
                        .findByCotizacion_IdCotizacion(
                                idCotizacion
                        );

        List<SalidaCotizacionDTO> filas =
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

                dto.setIsbn(
                        recurso.getIsbn()
                );

                dto.setTitulo(
                        recurso.getTitulo()
                );

                dto.setTipo(
                        recurso.getTipo()
                );

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

            // Sumar subtotal al total general
            if (detalle.getSubtotal() != null) {

                total = total.add(
                        detalle.getSubtotal()
                );
            }

            filas.add(dto);
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

        salida.setDetalles(filas);

        return salida;
    }
}