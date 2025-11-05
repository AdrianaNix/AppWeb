package es.unirioja.paw.service.data;

import es.unirioja.paw.jpa.CestaCompraEntity;
import java.time.LocalDate;

public class FinalizarCompraRequest {

    public final CestaCompraEntity cesta;
    public final String calle;
    public final String codigoPostal;
    public final String ciudad;
    public final String provincia;
    public final LocalDate fechaEntregaDeseada;

    public FinalizarCompraRequest(CestaCompraEntity cesta, String calle, String codigoPostal, String ciudad, String provincia, LocalDate fechaEntregaDeseada) {
        this.cesta = cesta;
        this.calle = calle;
        this.codigoPostal = codigoPostal;
        this.ciudad = ciudad;
        this.provincia = provincia;
        this.fechaEntregaDeseada = fechaEntregaDeseada;
    }

}
