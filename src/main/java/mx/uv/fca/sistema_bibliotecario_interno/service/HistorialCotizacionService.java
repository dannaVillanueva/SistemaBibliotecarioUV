package mx.uv.fca.sistema_bibliotecario_interno.service;

import mx.uv.fca.sistema_bibliotecario_interno.model.Cotizacion;
import mx.uv.fca.sistema_bibliotecario_interno.model.DetalleCotizacion;
import mx.uv.fca.sistema_bibliotecario_interno.model.HistorialCotizacion;
import mx.uv.fca.sistema_bibliotecario_interno.repository.HistorialCotizacionRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class HistorialCotizacionService {

    private final HistorialCotizacionRepository repository;
    private final ObjectMapper mapper;

    public HistorialCotizacionService(HistorialCotizacionRepository repository, ObjectMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<HistorialCotizacion> listar() {
        return repository.findAllByOrderByFechaHoraDescIdHistorialDesc();
    }

    public List<HistorialCotizacion> listarPorCotizacion(Integer id) {
        return repository.findByIdCotizacionOrderByFechaHoraDescIdHistorialDesc(id);
    }

    // Guardamos valores, no referencias JPA: cada versión queda congelada.
    public String snapshot(Cotizacion c, List<DetalleCotizacion> detalles) {
        if (c == null) {
            return null;
        }

        Map<String, Object> datos = new LinkedHashMap<>();
        datos.put("idCotizacion", c.getIdCotizacion());
        datos.put("fechaSolicitud", c.getFechaSolicitud() == null ? null : c.getFechaSolicitud().toString());
        datos.put("estado", c.getEstado());
        datos.put("observaciones", c.getObservaciones());
        datos.put("solicitante", c.getSolicitante());
        datos.put("correoSolicitante", c.getCorreoSolicitante());
        datos.put("telefonoSolicitante", c.getTelefonoSolicitante());
        datos.put("dependencia", c.getDependencia());
        datos.put("justificacion", c.getJustificacion());
        datos.put("proveedorSugerido", c.getProveedorSugerido());

        List<Map<String, Object>> libros = new ArrayList<>();
        if (detalles != null) {
            for (DetalleCotizacion d : detalles) {
                Map<String, Object> libro = new LinkedHashMap<>();
                libro.put("idDetalleCotizacion", d.getIdDetalleCotizacion());
                libro.put("idRecursoDocumental", d.getRecursoDocumental() == null ? null : d.getRecursoDocumental().getIdRecursoDocumental());
                libro.put("tituloExterno", d.getTituloExterno());
                libro.put("autorExterno", d.getAutorExterno());
                libro.put("isbnExterno", d.getIsbnExterno());
                libro.put("editorialExterna", d.getEditorialExterna());
                libro.put("idEditorial", d.getEditorial() == null ? null : d.getEditorial().getIdEditorial());
                libro.put("urlCotizacion", d.getUrlCotizacion());
                libro.put("estadoAdquisicion", d.getEstadoAdquisicion());
                libro.put("edicionExterna", d.getEdicionExterna());
                libro.put("anioExterno", d.getAnioExterno());
                libro.put("idProgramaEducativo", d.getProgramaEducativo() == null ? null : d.getProgramaEducativo().getIdProgramaEducativo());
                libro.put("idAcademia", d.getAcademia() == null ? null : d.getAcademia().getIdAcademia());
                libro.put("cantidad", d.getCantidad());
                libro.put("precioUnitarioEstimado", d.getPrecioUnitarioEstimado());
                libro.put("subtotal", d.getSubtotal());
                libro.put("filu", d.getFilu());
                libros.add(libro);
            }
        }

        datos.put("libros", libros);

        try {
            return mapper.writeValueAsString(datos);
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo crear la versión histórica.", e);
        }
    }

    public void registrar(Integer id, String accion, String anterior, String nuevo,
                         String estadoAnterior, String estadoNuevo, String observaciones) {
        HistorialCotizacion h = new HistorialCotizacion();
        h.setIdCotizacion(id);
        h.setFechaHora(LocalDateTime.now());
        h.setAccion(accion);
        h.setEstadoAnterior(estadoAnterior);
        h.setEstadoNuevo(estadoNuevo);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        h.setUsuario(auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())
                ? auth.getName() : "No identificado");

        h.setObservaciones(observaciones);
        h.setDatosAnteriores(anterior);
        h.setDatosNuevos(nuevo);
        repository.save(h);
    }
}
