package mx.uv.fca.sistema_bibliotecario_interno.service;

import mx.uv.fca.sistema_bibliotecario_interno.dto.CotizacionCompletaDTO;
import mx.uv.fca.sistema_bibliotecario_interno.model.Cotizacion;
import mx.uv.fca.sistema_bibliotecario_interno.model.DetalleCotizacion;
import mx.uv.fca.sistema_bibliotecario_interno.repository.CotizacionRepository;
import mx.uv.fca.sistema_bibliotecario_interno.repository.DetalleCotizacionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.util.*;

@Service
public class CotizacionService {
    private final CotizacionRepository repository;
    private final DetalleCotizacionRepository detalleRepository;
    private final DetalleCotizacionService detalleService;
    private final HistorialCotizacionService historial;

    public CotizacionService(CotizacionRepository repository,
                             DetalleCotizacionRepository detalleRepository,
                             DetalleCotizacionService detalleService,
                             HistorialCotizacionService historial) {
        this.repository = repository;
        this.detalleRepository = detalleRepository;
        this.detalleService = detalleService;
        this.historial = historial;
    }

    public List<Cotizacion> listar() { return repository.findAll(); }
    public Optional<Cotizacion> buscarPorId(Integer id) { return repository.findById(id); }
    private List<DetalleCotizacion> detalles(Integer id) {
        return detalleRepository.findByCotizacion_IdCotizacion(id);
    }
    public List<DetalleCotizacion> obtenerDetalles(Integer id) {
        if (!repository.existsById(id)) throw new IllegalArgumentException("La cotización indicada no existe.");
        return detalles(id);
    }

    @Transactional
    public Cotizacion guardar(Cotizacion cotizacion) {
        if (cotizacion.getIdCotizacion() != null) {
            // Evita sobrescrituras sin auditoría desde POST /api/cotizaciones.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Utiliza el endpoint de actualización.");
        }
        cotizacion.setEstado("pendiente");
        if (cotizacion.getFechaSolicitud() == null) cotizacion.setFechaSolicitud(LocalDate.now());
        Cotizacion guardada = repository.saveAndFlush(cotizacion);
        historial.registrar(guardada.getIdCotizacion(), "CREACION", null,
                historial.snapshot(guardada, List.of()), null, guardada.getEstado(), "Creación sin libros");
        return guardada;
    }

    @Transactional
    public Cotizacion guardarCotizacionCompleta(CotizacionCompletaDTO solicitud) {
        validarSolicitud(solicitud);
        Cotizacion c = solicitud.getCotizacion();
        c.setIdCotizacion(null);
        c.setEstado("pendiente");
        if (c.getFechaSolicitud() == null) c.setFechaSolicitud(LocalDate.now());
        Cotizacion guardada = repository.saveAndFlush(c);
        for (DetalleCotizacion d : solicitud.getDetalles()) {
            if (d == null) throw new IllegalArgumentException("La lista contiene un detalle vacío.");
            d.setIdDetalleCotizacion(null);
            d.setCotizacion(guardada);
            detalleService.guardar(d);
        }
        detalleRepository.flush();
        historial.registrar(guardada.getIdCotizacion(), "CREACION", null,
                historial.snapshot(guardada, detalles(guardada.getIdCotizacion())),
                null, guardada.getEstado(), "Cotización registrada con libros");
        return guardada;
    }

    @Transactional
    public Optional<Cotizacion> actualizarCompleta(Integer id, CotizacionCompletaDTO solicitud) {
        validarSolicitud(solicitud);
        Optional<Cotizacion> encontrada = repository.findById(id);
        if (encontrada.isEmpty()) return Optional.empty();
        Cotizacion actual = encontrada.get();
        String anterior = historial.snapshot(actual, detalles(id));
        String estadoAnterior = actual.getEstado();
        Cotizacion entrada = solicitud.getCotizacion();
        copiarCampos(actual, entrada);
        repository.saveAndFlush(actual);
        List<DetalleCotizacion> anteriores = detalles(id);
        Set<Integer> conservar = new HashSet<>();
        for (DetalleCotizacion d : solicitud.getDetalles()) {
            if (d == null) throw new IllegalArgumentException("La lista contiene un detalle vacío.");
            Integer idDetalle = d.getIdDetalleCotizacion();
            if (idDetalle != null) {
                boolean pertenece = anteriores.stream().anyMatch(a -> idDetalle.equals(a.getIdDetalleCotizacion()));
                if (!pertenece || !conservar.add(idDetalle)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Detalle duplicado o ajeno a la cotización.");
                }
            }
            d.setCotizacion(actual);
            detalleService.guardar(d);
        }
        for (DetalleCotizacion d : anteriores) {
            if (!conservar.contains(d.getIdDetalleCotizacion())) detalleRepository.delete(d);
        }
        detalleRepository.flush();
        String nuevo = historial.snapshot(actual, detalles(id));
        historial.registrar(id, "EDICION", anterior, nuevo,
                estadoAnterior, actual.getEstado(), "Actualización de datos y libros");
        return Optional.of(actual);
    }

    @Transactional
    public Optional<Cotizacion> actualizarCabecera(Integer id, Cotizacion entrada) {
        Optional<Cotizacion> encontrada = repository.findById(id);
        if (encontrada.isEmpty()) return Optional.empty();
        Cotizacion actual = encontrada.get();
        String anterior = historial.snapshot(actual, detalles(id));
        String estadoAnterior = actual.getEstado();
        copiarCampos(actual, entrada);
        repository.saveAndFlush(actual);
        historial.registrar(id, "EDICION", anterior, historial.snapshot(actual, detalles(id)),
                estadoAnterior, actual.getEstado(), "Actualización de datos generales");
        return Optional.of(actual);
    }

    private void copiarCampos(Cotizacion destino, Cotizacion origen) {
        // El estado se cambia exclusivamente mediante PATCH.
        destino.setFechaSolicitud(origen.getFechaSolicitud());
        destino.setObservaciones(origen.getObservaciones());
        destino.setSolicitante(origen.getSolicitante());
        destino.setCorreoSolicitante(origen.getCorreoSolicitante());
        destino.setTelefonoSolicitante(origen.getTelefonoSolicitante());
        destino.setDependencia(origen.getDependencia());
        destino.setJustificacion(origen.getJustificacion());
        destino.setProveedorSugerido(origen.getProveedorSugerido());
    }

    @Transactional
    public Optional<Cotizacion> cambiarEstado(Integer id, String nuevoEstado) {
        if (!List.of("aprobada", "rechazada").contains(nuevoEstado))
            throw new IllegalArgumentException("Estado no permitido.");
        Optional<Cotizacion> encontrada = repository.findById(id);
        if (encontrada.isEmpty()) return Optional.empty();
        Cotizacion c = encontrada.get();
        String actual = c.getEstado() == null ? "" : c.getEstado().trim().toLowerCase(Locale.ROOT);
        if (!"pendiente".equals(actual))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Solo se pueden resolver cotizaciones pendientes.");
        String anterior = historial.snapshot(c, detalles(id));
        c.setEstado(nuevoEstado);
        repository.saveAndFlush(c);
        historial.registrar(id, "CAMBIO_ESTADO", anterior, historial.snapshot(c, detalles(id)),
                actual, nuevoEstado, "Cotización " + nuevoEstado);
        return Optional.of(c);
    }

    @Transactional
    public void eliminar(Integer id) {
        Cotizacion c = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cotización no encontrada."));
        List<DetalleCotizacion> existentes = detalles(id);
        historial.registrar(id, "ELIMINACION", historial.snapshot(c, existentes), null,
                c.getEstado(), null, "Eliminación de cotización y libros");
        detalleRepository.deleteAll(existentes);
        detalleRepository.flush();
        repository.delete(c);
    }

    private void validarSolicitud(CotizacionCompletaDTO solicitud) {
        if (solicitud == null || solicitud.getCotizacion() == null)
            throw new IllegalArgumentException("Los datos de la cotización son obligatorios.");
        if (solicitud.getDetalles() == null || solicitud.getDetalles().isEmpty())
            throw new IllegalArgumentException("La cotización debe contener al menos un libro.");
    }
}
