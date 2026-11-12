package mx.uv.fca.sistema_bibliotecario_interno.service;

import mx.uv.fca.sistema_bibliotecario_interno.dto.*;
import mx.uv.fca.sistema_bibliotecario_interno.model.*;
import mx.uv.fca.sistema_bibliotecario_interno.repository.*;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class DescarteService {

    private final DescarteRepository descarteRepository;
    private final RecursoDocumentalRepository recursoRepository;
    private final UsuarioRepository usuarioRepository;
    private final BajaRepository bajaRepository;

    public DescarteService(
            DescarteRepository descarteRepository,
            RecursoDocumentalRepository recursoRepository,
            UsuarioRepository usuarioRepository,
            BajaRepository bajaRepository) {

        this.descarteRepository = descarteRepository;
        this.recursoRepository = recursoRepository;
        this.usuarioRepository = usuarioRepository;
        this.bajaRepository = bajaRepository;
    }

    private Usuario usuarioActual() {
        Authentication auth = SecurityContextHolder
                .getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()
                || "anonymousUser".equals(auth.getName())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Debes iniciar sesión"
            );
        }

        return usuarioRepository.findByCorreo(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "No se encontró el usuario autenticado"
                ));
    }

    @Transactional
    public DescarteResponseDTO registrar(DescarteRequestDTO datos) {

        if (datos == null ||
                datos.getIdRecursoDocumental() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Debes seleccionar un recurso documental"
            );
        }

        if (datos.getCantidad() == null ||
                datos.getCantidad() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La cantidad debe ser mayor que cero"
            );
        }

        if (datos.getMotivo() == null ||
                datos.getMotivo().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El motivo es obligatorio"
            );
        }

        RecursoDocumental recurso = recursoRepository
                .findById(datos.getIdRecursoDocumental())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe el recurso documental"
                ));

        int disponibles = recurso.getCantidad() == null
                ? 0 : recurso.getCantidad();

        if (datos.getCantidad() > disponibles) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La cantidad solicitada supera las existencias"
            );
        }

        Descarte descarte = new Descarte();
        descarte.setFechaDescarte(
                datos.getFechaDescarte() == null
                        ? LocalDate.now()
                        : datos.getFechaDescarte()
        );
        descarte.setMotivo(datos.getMotivo().trim());
        descarte.setObservaciones(datos.getObservaciones());
        descarte.setCantidad(datos.getCantidad());
        descarte.setEstado("PENDIENTE");
        descarte.setRecursoDocumental(recurso);
        descarte.setUsuario(usuarioActual());

        return convertirDTO(descarteRepository.save(descarte));
    }

    @Transactional
    public DescarteResponseDTO aprobar(
            Integer id,
            DescarteResolucionDTO datos) {

        Usuario responsable = usuarioActual();

        Descarte descarte = descarteRepository
                .buscarParaResolver(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe la solicitud de descarte"
                ));

        validarPendiente(descarte);

        RecursoDocumental recurso = recursoRepository
                .buscarParaActualizar(
                        descarte.getRecursoDocumental()
                                .getIdRecursoDocumental()
                )
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe el recurso documental"
                ));

        int disponibles = recurso.getCantidad() == null
                ? 0 : recurso.getCantidad();

        int cantidad = descarte.getCantidad();

        if (cantidad <= 0 || cantidad > disponibles) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Inventario insuficiente para aprobar. " +
                            "Disponibles: " + disponibles
            );
        }

        // Se descuenta el inventario una sola vez.
        recurso.setCantidad(disponibles - cantidad);
        recursoRepository.save(recurso);

        // Se crea la baja como comprobante del movimiento.
        // No se llama a BajaService.registrar(), porque
        // ese método volvería a descontar existencias.
        Baja baja = new Baja();
        baja.setFechaBaja(LocalDate.now());
        baja.setCantidad(cantidad);
        baja.setMotivo(
                "Descarte aprobado #" + descarte.getIdDescarte()
                        + ": " + descarte.getMotivo()
        );
        baja.setObservaciones(
                "Baja generada automáticamente por aprobación " +
                        "de solicitud de descarte #" +
                        descarte.getIdDescarte()
        );
        baja.setRecursoDocumental(recurso);
        baja.setUsuario(responsable);

        Baja bajaGuardada = bajaRepository.save(baja);

        descarte.setEstado("APROBADO");
        descarte.setFechaResolucion(LocalDate.now());
        descarte.setUsuarioResolucion(responsable);
        descarte.setObservacionesResolucion(
                observacionesResolucion(datos)
        );
        descarte.setBaja(bajaGuardada);

        return convertirDTO(descarteRepository.save(descarte));
    }

    @Transactional
    public DescarteResponseDTO rechazar(
            Integer id,
            DescarteResolucionDTO datos) {

        Usuario responsable = usuarioActual();

        Descarte descarte = descarteRepository
                .buscarParaResolver(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe la solicitud de descarte"
                ));

        validarPendiente(descarte);

        String observaciones = observacionesResolucion(datos);

        if (observaciones == null || observaciones.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Debes indicar el motivo del rechazo"
            );
        }

        descarte.setEstado("RECHAZADO");
        descarte.setFechaResolucion(LocalDate.now());
        descarte.setUsuarioResolucion(responsable);
        descarte.setObservacionesResolucion(observaciones);

        // Un rechazo nunca modifica el inventario.
        return convertirDTO(descarteRepository.save(descarte));
    }

    private void validarPendiente(Descarte descarte) {
        if (!"PENDIENTE".equals(descarte.getEstado())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Esta solicitud ya fue resuelta"
            );
        }
    }

    private String observacionesResolucion(
            DescarteResolucionDTO datos) {

        if (datos == null ||
                datos.getObservacionesResolucion() == null) {
            return null;
        }

        String texto = datos.getObservacionesResolucion().trim();

        if (texto.length() > 1000) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Las observaciones no pueden superar 1000 caracteres"
            );
        }

        return texto;
    }

    @Transactional(readOnly = true)
    public List<DescarteResponseDTO> listar() {
        return descarteRepository.findAll().stream()
                .map(this::convertirDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DescarteResponseDTO> pendientes() {
        return descarteRepository.findByEstado("PENDIENTE")
                .stream().map(this::convertirDTO).toList();
    }

    @Transactional(readOnly = true)
    public DescarteResponseDTO buscarPorId(Integer id) {
        return convertirDTO(descarteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe el descarte"
                )));
    }

    @Transactional(readOnly = true)
    public List<DescarteResponseDTO> buscarPorRecurso(Integer id) {
        return descarteRepository
                .findByRecursoDocumental_IdRecursoDocumental(id)
                .stream().map(this::convertirDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<DescarteResponseDTO> buscarPorUsuario(Integer id) {
        return descarteRepository.findByUsuario_IdUsuario(id)
                .stream().map(this::convertirDTO).toList();
    }

    private DescarteResponseDTO convertirDTO(Descarte d) {

        RecursoDocumental r = d.getRecursoDocumental();
        Usuario solicitante = d.getUsuario();
        Usuario responsable = d.getUsuarioResolucion();

        return new DescarteResponseDTO(
                d.getIdDescarte(),
                d.getFechaDescarte(),
                d.getMotivo(),
                d.getObservaciones(),
                d.getCantidad(),
                d.getEstado(),
                d.getFechaResolucion(),
                d.getObservacionesResolucion(),
                r.getIdRecursoDocumental(),
                r.getTitulo(),
                r.getIsbn(),
                solicitante.getIdUsuario(),
                solicitante.getNombre(),
                responsable == null
                        ? null : responsable.getIdUsuario(),
                responsable == null
                        ? null : responsable.getNombre(),
                d.getBaja() == null
                        ? null : d.getBaja().getIdBaja()
        );
    }
}