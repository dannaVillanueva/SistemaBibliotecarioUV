package mx.uv.fca.sistema_bibliotecario_interno.service;

import mx.uv.fca.sistema_bibliotecario_interno.dto.DonacionRequest;
import mx.uv.fca.sistema_bibliotecario_interno.model.Donacion;
import mx.uv.fca.sistema_bibliotecario_interno.model.RecursoDocumental;
import mx.uv.fca.sistema_bibliotecario_interno.repository.DonacionRepository;
import mx.uv.fca.sistema_bibliotecario_interno.repository.RecursoDocumentalRepository;
import mx.uv.fca.sistema_bibliotecario_interno.specification.DonacionSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class DonacionService {
    private final DonacionRepository repository;
    private final RecursoDocumentalRepository recursos;

    public DonacionService(DonacionRepository repository, RecursoDocumentalRepository recursos) {
        this.repository = repository;
        this.recursos = recursos;
    }
    public List<Donacion> listar() { return repository.findAll(); }
    public Optional<Donacion> buscarPorId(Integer id) { return repository.findById(id); }

    private void validar(DonacionRequest r) {
        if (r.cantidad() == null || r.cantidad() < 1)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cantidad debe ser mayor a cero");
        if (r.nombreDonante() == null || r.nombreDonante().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Indica el nombre del donante");
        if (r.fechaDonacion() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Indica la fecha");
        if (r.precioEstimado() != null && r.precioEstimado().signum() < 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El precio no puede ser negativo");
        if ((r.idRecursoDocumental() == null) == (r.libroNuevo() == null))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Selecciona un recurso existente o proporciona uno nuevo");
        if (r.libroNuevo() != null && (r.libroNuevo().titulo() == null || r.libroNuevo().titulo().isBlank()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El libro nuevo requiere título");
    }
    private RecursoDocumental buscarRecurso(Integer id) {
        return recursos.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Recurso documental no encontrado"));
    }
    private void ajustarExistencias(RecursoDocumental recurso, int diferencia) {
        int anterior = recurso.getCantidad() == null ? 0 : recurso.getCantidad();
        long resultado = (long) anterior + diferencia;
        if (resultado < 0 || resultado > Integer.MAX_VALUE)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Existencias insuficientes o cantidad excesiva");
        recurso.setCantidad((int) resultado);
        recursos.save(recurso);
    }
    private void copiar(Donacion destino, DonacionRequest r, RecursoDocumental recurso) {
        destino.setNombreDonante(r.nombreDonante().trim());
        destino.setFechaDonacion(r.fechaDonacion());
        destino.setCantidad(r.cantidad());
        destino.setPrecioEstimado(r.precioEstimado());
        destino.setObservaciones(r.observaciones());
        destino.setRecursoDocumental(recurso);
    }
    @Transactional
    public Donacion crear(DonacionRequest r) {
        validar(r);
        RecursoDocumental recurso;
        if (r.idRecursoDocumental() != null) {
            recurso = buscarRecurso(r.idRecursoDocumental());
        } else {
            DonacionRequest.LibroNuevo l = r.libroNuevo();
            recurso = new RecursoDocumental();
            recurso.setTitulo(l.titulo().trim());
            recurso.setAutor(l.autor());
            recurso.setIsbn(l.isbn());
            recurso.setEdicion(l.edicion());
            recurso.setAnio(l.anio());
            recurso.setTipo(l.tipo());
            recurso.setMovimiento("DONACION");
            recurso.setEnCatalogoBiblioFca(true);
            recurso.setCantidad(0);
            recurso = recursos.save(recurso);
        }
        ajustarExistencias(recurso, r.cantidad());
        Donacion d = new Donacion();
        copiar(d, r, recurso);
        return repository.save(d);
    }
    @Transactional
    public Donacion actualizar(Integer id, DonacionRequest r) {
        validar(r);
        Donacion actual = repository.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Donación no encontrada"));
        if (r.libroNuevo() != null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Al editar selecciona el recurso existente. Los libros nuevos solo se crean al registrar.");
        RecursoDocumental anterior = actual.getRecursoDocumental();
        RecursoDocumental nuevo = buscarRecurso(r.idRecursoDocumental());
        int cantidadAnterior = actual.getCantidad() == null ? 1 : actual.getCantidad();
        if (anterior.getIdRecursoDocumental().equals(nuevo.getIdRecursoDocumental())) {
            ajustarExistencias(nuevo, r.cantidad() - cantidadAnterior);
        } else {
            ajustarExistencias(anterior, -cantidadAnterior);
            ajustarExistencias(nuevo, r.cantidad());
        }
        copiar(actual, r, nuevo);
        return repository.save(actual);
    }
    @Transactional
    public void eliminar(Integer id) {
        Donacion actual = repository.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Donación no encontrada"));
        ajustarExistencias(actual.getRecursoDocumental(),
            -(actual.getCantidad() == null ? 1 : actual.getCantidad()));
        repository.delete(actual);
    }
    public List<Donacion> buscar(String nombreDonante, LocalDate fechaDonacion,
        Integer idRecursoDocumental, String titulo, String isbn,
        BigDecimal precioMinimo, BigDecimal precioMaximo) {
        Specification<Donacion> spec = Specification.where(
            DonacionSpecification.nombreDonanteContiene(nombreDonante))
            .and(DonacionSpecification.fechaIgual(fechaDonacion))
            .and(DonacionSpecification.recursoIgual(idRecursoDocumental))
            .and(DonacionSpecification.tituloRecursoContiene(titulo))
            .and(DonacionSpecification.isbnIgual(isbn))
            .and(DonacionSpecification.precioMinimo(precioMinimo))
            .and(DonacionSpecification.precioMaximo(precioMaximo));
        return repository.findAll(spec);
    }
}
