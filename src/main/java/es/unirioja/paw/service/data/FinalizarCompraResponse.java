package es.unirioja.paw.service.data;

import es.unirioja.paw.jpa.PedidoEntity;

public class FinalizarCompraResponse {

    public final PedidoEntity pedido;

    public FinalizarCompraResponse(PedidoEntity pedido) {
        this.pedido = pedido;
    }

}
