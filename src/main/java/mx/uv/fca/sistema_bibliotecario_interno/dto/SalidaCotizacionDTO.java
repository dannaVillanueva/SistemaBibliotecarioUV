package mx.uv.fca.sistema_bibliotecario_interno.dto;

import java.math.BigDecimal;

public class SalidaCotizacionDTO {

    private String programaEducativo;
    private String academia;
    private String isbn;
    private String titulo;
    private String tipo;
    private String editorial;
    private Integer cantidad;
    private BigDecimal precioUnitarioEstimado;
    private BigDecimal subtotal;
    private String filu;

    public SalidaCotizacionDTO() {
    }

    public String getProgramaEducativo() {
        return programaEducativo;
    }

    public void setProgramaEducativo(String programaEducativo) {
        this.programaEducativo = programaEducativo;
    }

    public String getAcademia() {
        return academia;
    }

    public void setAcademia(String academia) {
        this.academia = academia;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitarioEstimado() {
        return precioUnitarioEstimado;
    }

    public void setPrecioUnitarioEstimado(
            BigDecimal precioUnitarioEstimado) {

        this.precioUnitarioEstimado =
                precioUnitarioEstimado;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public String getFilu() {
        return filu;
    }

    public void setFilu(String filu) {
        this.filu = filu;
    }
}