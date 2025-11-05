package es.unirioja.paw.service;

import es.unirioja.paw.jpa.CestaCompraEntity;
import es.unirioja.paw.jpa.PedidoEntity;
import es.unirioja.paw.web.form.PedidoForm;

public interface RealizarPedidoUseCase {
    /**
     * Crea un PedidoEntity a partir de la cesta y los datos del form,
     * borra la cesta y devuelve la entidad guardada.
     */
    PedidoEntity realizarPedido(PedidoForm form, CestaCompraEntity cesta);
}
