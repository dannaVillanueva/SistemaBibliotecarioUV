package mx.uv.fca.sistema_bibliotecario_interno.service;

import mx.uv.fca.sistema_bibliotecario_interno.dto.BajaRequestDTO;
import mx.uv.fca.sistema_bibliotecario_interno.dto.BajaResponseDTO;
import mx.uv.fca.sistema_bibliotecario_interno.model.Baja;
import mx.uv.fca.sistema_bibliotecario_interno.model.RecursoDocumental;
import mx.uv.fca.sistema_bibliotecario_interno.model.Usuario;
import mx.uv.fca.sistema_bibliotecario_interno.repository.BajaRepository;
import mx.uv.fca.sistema_bibliotecario_interno.repository.RecursoDocumentalRepository;
import mx.uv.fca.sistema_bibliotecario_interno.repository.UsuarioRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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

    public BajaResponseDTO registrar(BajaRequestDTO datos) {
        RecursoDocumental recurso = recursoRepository
                .findById(datos.getIdRecursoDocumental())
                .orElseThrow(() ->
                        new RuntimeException(
                                "No existe el recurso documental"
                        )
                );
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
        Baja baja = new Baja();

        baja.setRecursoDocumental(recurso);
        baja.setUsuario(usuario);
        baja.setMotivo(datos.getMotivo());
        baja.setObservaciones(datos.getObservaciones());

        if (datos.getFechaBaja() != null) {
            baja.setFechaBaja(datos.getFechaBaja());
        } else {
            baja.setFechaBaja(LocalDate.now());
        }

        Baja guardada = bajaRepository.save(baja);

        return convertirDTO(guardada);
    }

    public List<BajaResponseDTO> listar() {

        return bajaRepository.findAll()
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    public BajaResponseDTO buscarPorId(Integer id) {

        Baja baja = bajaRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "No existe la baja"
                        )
                );

        return convertirDTO(baja);
    }

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

                recurso.getIdRecursoDocumental(),
                recurso.getTitulo(),
                recurso.getIsbn(),

                usuario.getIdUsuario(),
                usuario.getNombre()
        );
    }
}