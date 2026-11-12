package mx.uv.fca.sistema_bibliotecario_interno.dto;

import mx.uv.fca.sistema_bibliotecario_interno.model.Cotizacion;
import mx.uv.fca.sistema_bibliotecario_interno.model.DetalleCotizacion;

import java.util.List;

public class CotizacionCompletaDTO {

    private Cotizacion cotizacion;
    private List<DetalleCotizacion> detalles;

    public CotizacionCompletaDTO() {
    }

    public Cotizacion getCotizacion() {
        return cotizacion;
    }

    public void setCotizacion(Cotizacion cotizacion) {
        this.cotizacion = cotizacion;
    }

    public List<DetalleCotizacion> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleCotizacion> detalles) {
        this.detalles = detalles;
    }
}