package es.unirioja.paw.service.data;

import es.unirioja.paw.jpa.PedidoanuladoEntity;

public class AnularPedidoResponse {

    public PedidoanuladoEntity pedidoAnulado;

    public AnularPedidoResponse(PedidoanuladoEntity pedidoAnulado) {
        this.pedidoAnulado = pedidoAnulado;
    }

}
