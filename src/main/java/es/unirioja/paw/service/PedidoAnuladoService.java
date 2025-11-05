package es.unirioja.paw.service;

import es.unirioja.paw.jpa.PedidoanuladoEntity;
import java.util.List;

/**
 * Servicios relacionados con pedidos anulados
 */
public interface PedidoAnuladoService {

    /**
     * Lista de pedidos anulados de un cliente
     * @param codigoCliente código del cliente
     * @return lista de pedidos anulados
     */
    List<PedidoanuladoEntity> findByCliente(String codigoCliente);

    /**
     * Obtiene un pedido anulado por su código
     * @param codigoPedido código del pedido
     * @return el pedido anulado, o null si no existe
     */
    PedidoanuladoEntity findOne(String codigoPedido);
}
