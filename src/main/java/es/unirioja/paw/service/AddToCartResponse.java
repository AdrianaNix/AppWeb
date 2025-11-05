package es.unirioja.paw.service;

import es.unirioja.paw.jpa.CestaCompraEntity;
import es.unirioja.paw.jpa.LineaCestaCompraEntity;

public class AddToCartResponse {
    private final CestaCompraEntity cesta;
    private final LineaCestaCompraEntity lineaAñadida;

    public AddToCartResponse(CestaCompraEntity cesta,
                             LineaCestaCompraEntity lineaAñadida) {
        this.cesta = cesta;
        this.lineaAñadida = lineaAñadida;
    }

    public CestaCompraEntity getCesta() { return cesta; }
    public LineaCestaCompraEntity getLineaAñadida() { return lineaAñadida; }
}
