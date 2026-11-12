package mx.uv.fca.sistema_bibliotecario_interno.service;

import mx.uv.fca.sistema_bibliotecario_interno.dto.BajaRequestDTO;
import mx.uv.fca.sistema_bibliotecario_interno.dto.BajaResponseDTO;
import mx.uv.fca.sistema_bibliotecario_interno.model.Baja;
import mx.uv.fca.sistema_bibliotecario_interno.model.RecursoDocumental;
import mx.uv.fca.sistema_bibliotecario_interno.model.Usuario;
import mx.uv.fca.sistema_bibliotecario_interno.repository.BajaRepository;
import mx.uv.fca.sistema_bibliotecario_interno.repository.RecursoDocumentalRepository;
import mx.uv.fca.sistema_bibliotecario_interno.repository.UsuarioRepository;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class BajaService {

    private final BajaRepository bajaRepository;
    private final RecursoDocumentalRepository recursoRepository;
    private final UsuarioRepository usuarioRepository;

    public BajaService(
            BajaRepository bajaRepository,
            RecursoDocumentalRepository recursoRepository,
            UsuarioRepository usuarioRepository) {

        this.bajaRepository = bajaRepository;
        this.recursoRepository = recursoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public BajaResponseDTO registrar(BajaRequestDTO datos) {

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
                    "El motivo de la baja es obligatorio"
            );
        }

        RecursoDocumental recurso = recursoRepository
                .findById(datos.getIdRecursoDocumental())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "No existe el recurso documental"
                        )
                );

        int disponibles = recurso.getCantidad() == null
                ? 0
                : recurso.getCantidad();

        if (datos.getCantidad() > disponibles) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No hay suficientes ejemplares. Disponibles: "
                            + disponibles
            );
        }

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getName())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Debes iniciar sesión para registrar una baja"
            );
        }

        String correo = authentication.getName();

        Usuario usuario = usuarioRepository
                .findByCorreo(correo)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "No se encontró el usuario autenticado"
                        )
                );

        recurso.setCantidad(
                disponibles - datos.getCantidad()
        );

        recursoRepository.save(recurso);

        Baja baja = new Baja();

        baja.setRecursoDocumental(recurso);
        baja.setUsuario(usuario);
        baja.setCantidad(datos.getCantidad());
        baja.setMotivo(datos.getMotivo().trim());
        baja.setObservaciones(datos.getObservaciones());

        baja.setFechaBaja(
                datos.getFechaBaja() != null
                        ? datos.getFechaBaja()
                        : LocalDate.now()
        );

        Baja guardada = bajaRepository.save(baja);

        return convertirDTO(guardada);
    }

    @Transactional(readOnly = true)
    public List<BajaResponseDTO> listar() {

        return bajaRepository.findAll()
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public BajaResponseDTO buscarPorId(Integer id) {

        Baja baja = bajaRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "No existe la baja"
                        )
                );

        return convertirDTO(baja);
    }

    @Transactional(readOnly = true)
    public List<BajaResponseDTO> buscarPorRecurso(
            Integer idRecursoDocumental) {

        return bajaRepository
                .findByRecursoDocumental_IdRecursoDocumental(
                        idRecursoDocumental
                )
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    private BajaResponseDTO convertirDTO(Baja baja) {

        RecursoDocumental recurso =
                baja.getRecursoDocumental();

        Usuario usuario =
                baja.getUsuario();

        return new BajaResponseDTO(
                baja.getIdBaja(),
                baja.getFechaBaja(),
                baja.getMotivo(),
                baja.getObservaciones(),
                baja.getCantidad(),
                recurso.getIdRecursoDocumental(),
                recurso.getTitulo(),
                recurso.getIsbn(),
                usuario.getIdUsuario(),
                usuario.getNombre()
        );
    }
}