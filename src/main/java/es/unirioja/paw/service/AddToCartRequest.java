package es.unirioja.paw.service;

import es.unirioja.paw.jpa.CestaCompraEntity;

public class AddToCartRequest {
    private final String codigoArticulo;
    private CestaCompraEntity cesta;

    public AddToCartRequest(String codigoArticulo, CestaCompraEntity cesta) {
        this.codigoArticulo = codigoArticulo;
        this.cesta = cesta;
    }

    public String getCodigoArticulo() { return codigoArticulo; }
    public CestaCompraEntity getCesta() { return cesta; }
    public void setCesta(CestaCompraEntity cesta) { this.cesta = cesta; }
}
