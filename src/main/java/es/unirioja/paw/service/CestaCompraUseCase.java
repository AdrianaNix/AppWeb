package es.unirioja.paw.service;

import java.util.Optional;

public interface CestaCompraUseCase {

    /**
     * Añade (o incrementa) un artículo en la cesta y la guarda en BD.
     */
    Optional<AddToCartResponse> add(AddToCartRequest request);

    /**
     * Fija la cantidad de una línea de cesta y guarda el cambio.
     *
     * @param lineaId UUID de la línea en la cesta.
     * @param cantidad Nueva cantidad.
     * @return DTO con la línea actualizada (incluye subtotal).
     */
    Optional<UpdateCartLineResponse> update(String lineaId, int cantidad);

}
