// src/main/java/es/unirioja/paw/service/AnularPedidoUseCase.java
package es.unirioja.paw.service;

public interface AnularPedidoUseCase {

    /**
     * Anula el pedido identificado por su código:
     *  1. Recupera el PedidoEntity original
     *  2. Crea y guarda un PedidoanuladoEntity con sus datos y líneas
     *  3. Elimina el PedidoEntity original
     *
     * @param codigoPedido el código del pedido a anular
     */
    void execute(String codigoPedido);
}

