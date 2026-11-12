package mx.uv.fca.sistema_bibliotecario_interno.service;

import mx.uv.fca.sistema_bibliotecario_interno.dto.DonacionSalidaDTO;
import mx.uv.fca.sistema_bibliotecario_interno.model.Donacion;
import mx.uv.fca.sistema_bibliotecario_interno.model.RecursoDocumental;
import mx.uv.fca.sistema_bibliotecario_interno.repository.DonacionRepository;
import mx.uv.fca.sistema_bibliotecario_interno.specification.DonacionSpecification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class SalidaDonacionService {

    private final DonacionRepository repository;

    public SalidaDonacionService(
            DonacionRepository repository) {

        this.repository = repository;
    }

    // LISTAR TODAS LAS DONACIONES
    public List<DonacionSalidaDTO> listar() {

        return repository.findAll()
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    // BUSCAR UNA DONACIÓN POR ID
    public DonacionSalidaDTO buscarPorId(Integer id) {

        Donacion donacion = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Donación no encontrada"));

        return convertirADTO(donacion);
    }

    // BUSCAR DONACIONES CON FILTROS
    public List<DonacionSalidaDTO> buscar(
            String nombreDonante,
            LocalDate fechaDonacion,
            Integer idRecursoDocumental,
            String titulo,
            String isbn,
            BigDecimal precioMinimo,
            BigDecimal precioMaximo) {

        Specification<Donacion> spec =
                Specification.where(
                                DonacionSpecification
                                        .nombreDonanteContiene(nombreDonante)
                        )
                        .and(DonacionSpecification
                                .fechaIgual(fechaDonacion))
                        .and(DonacionSpecification
                                .recursoIgual(idRecursoDocumental))
                        .and(DonacionSpecification
                                .tituloRecursoContiene(titulo))
                        .and(DonacionSpecification
                                .isbnIgual(isbn))
                        .and(DonacionSpecification
                                .precioMinimo(precioMinimo))
                        .and(DonacionSpecification
                                .precioMaximo(precioMaximo));

        return repository.findAll(spec)
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    // CONVERSIÓN DE ENTIDAD A DTO
    private DonacionSalidaDTO convertirADTO(
            Donacion donacion) {

        DonacionSalidaDTO dto =
                new DonacionSalidaDTO();

        dto.setIdDonacion(
                donacion.getIdDonacion()
        );

        dto.setNombreDonante(
                donacion.getNombreDonante()
        );

        dto.setFechaDonacion(
                donacion.getFechaDonacion()
        );

        dto.setPrecioEstimado(
                donacion.getPrecioEstimado()
        );

        dto.setObservaciones(
                donacion.getObservaciones()
        );

        RecursoDocumental recurso =
                donacion.getRecursoDocumental();

        if (recurso != null) {

            dto.setIdRecursoDocumental(
                    recurso.getIdRecursoDocumental()
            );

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
                        recurso.getEditorial().getNombre()
                );
            }
        }

        return dto;
    }
}