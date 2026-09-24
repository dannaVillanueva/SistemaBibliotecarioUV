package mx.uv.fca.sistema_bibliotecario_interno.service;

import mx.uv.fca.sistema_bibliotecario_interno.dto.DescarteRequestDTO;
import mx.uv.fca.sistema_bibliotecario_interno.dto.DescarteResponseDTO;
import mx.uv.fca.sistema_bibliotecario_interno.model.Descarte;
import mx.uv.fca.sistema_bibliotecario_interno.model.RecursoDocumental;
import mx.uv.fca.sistema_bibliotecario_interno.model.Usuario;
import mx.uv.fca.sistema_bibliotecario_interno.repository.DescarteRepository;
import mx.uv.fca.sistema_bibliotecario_interno.repository.RecursoDocumentalRepository;
import mx.uv.fca.sistema_bibliotecario_interno.repository.UsuarioRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DescarteService {

    private final DescarteRepository descarteRepository;
    private final RecursoDocumentalRepository recursoRepository;
    private final UsuarioRepository usuarioRepository;

    public DescarteService(
            DescarteRepository descarteRepository,
            RecursoDocumentalRepository recursoRepository,
            UsuarioRepository usuarioRepository) {

        this.descarteRepository = descarteRepository;
        this.recursoRepository = recursoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // REGISTRAR DESCARTE
    public DescarteResponseDTO registrar(
            DescarteRequestDTO datos) {

        // Buscar recurso documental
        RecursoDocumental recurso = recursoRepository
                .findById(datos.getIdRecursoDocumental())
                .orElseThrow(() ->
                        new RuntimeException(
                                "No existe el recurso documental"
                        )
                );

        // Obtener usuario autenticado
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String correo = authentication.getName();

        Usuario usuario = usuarioRepository
                .findByCorreo(correo)
                .orElseThrow(() ->
                        new RuntimeException(
                                "No se encontró el usuario autenticado"
                        )
                );

        // Crear descarte
        Descarte descarte = new Descarte();

        descarte.setRecursoDocumental(recurso);
        descarte.setUsuario(usuario);
        descarte.setMotivo(datos.getMotivo());
        descarte.setObservaciones(datos.getObservaciones());

        // Si no se proporciona fecha, usar la actual
        if (datos.getFechaDescarte() != null) {
            descarte.setFechaDescarte(
                    datos.getFechaDescarte()
            );
        } else {
            descarte.setFechaDescarte(
                    LocalDate.now()
            );
        }

        // Guardar
        Descarte guardado =
                descarteRepository.save(descarte);

        return convertirDTO(guardado);
    }

    // LISTAR TODOS
    public List<DescarteResponseDTO> listar() {

        return descarteRepository
                .findAll()
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    // BUSCAR POR ID
    public DescarteResponseDTO buscarPorId(
            Integer id) {

        Descarte descarte =
                descarteRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "No existe el descarte"
                                )
                        );

        return convertirDTO(descarte);
    }

    // BUSCAR POR RECURSO
    public List<DescarteResponseDTO> buscarPorRecurso(
            Integer idRecursoDocumental) {

        return descarteRepository
                .findByRecursoDocumental_IdRecursoDocumental(
                        idRecursoDocumental
                )
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    // BUSCAR POR USUARIO
    public List<DescarteResponseDTO> buscarPorUsuario(
            Integer idUsuario) {

        return descarteRepository
                .findByUsuario_IdUsuario(idUsuario)
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    // CONVERTIR ENTIDAD A DTO
    private DescarteResponseDTO convertirDTO(
            Descarte descarte) {

        RecursoDocumental recurso =
                descarte.getRecursoDocumental();

        Usuario usuario =
                descarte.getUsuario();

        return new DescarteResponseDTO(
                descarte.getIdDescarte(),
                descarte.getFechaDescarte(),
                descarte.getMotivo(),
                descarte.getObservaciones(),

                recurso.getIdRecursoDocumental(),
                recurso.getTitulo(),
                recurso.getIsbn(),

                usuario.getIdUsuario(),
                usuario.getNombre()
        );
    }
}