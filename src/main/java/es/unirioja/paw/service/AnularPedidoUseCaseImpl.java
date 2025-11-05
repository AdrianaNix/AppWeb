// src/main/java/es/unirioja/paw/service/AnularPedidoUseCaseImpl.java
package es.unirioja.paw.service;

import es.unirioja.paw.jpa.LineaanuladaEntity;
import es.unirioja.paw.jpa.PedidoEntity;
import es.unirioja.paw.jpa.PedidoanuladoEntity;
import es.unirioja.paw.repository.PedidoAnuladoRepository;
import es.unirioja.paw.repository.PedidoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AnularPedidoUseCaseImpl implements AnularPedidoUseCase {

    private final PedidoRepository pedidoRepo;
    private final PedidoAnuladoRepository anuladoRepo;

    @Autowired
    public AnularPedidoUseCaseImpl(PedidoRepository pedidoRepo,
                                   PedidoAnuladoRepository anuladoRepo) {
        this.pedidoRepo = pedidoRepo;
        this.anuladoRepo = anuladoRepo;
    }

    @Override
    @Transactional
    public void execute(String codigoPedido) {
        // 1. Recuperar el pedido original
        PedidoEntity pedido = pedidoRepo.findById(codigoPedido)
            .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado: " + codigoPedido));

        // 2. Construir PedidoanuladoEntity
        PedidoanuladoEntity anulado = new PedidoanuladoEntity();
        anulado.setCodigo(pedido.getCodigo());
        anulado.setCodigoCliente(pedido.getCodigoCliente());
        anulado.setFechacierre(pedido.getFechacierre());
        anulado.setFechaanulacion(new java.util.Date());

        // Inicializar lista de líneas anuladas
        anulado.setLineas(new ArrayList<>());
        // Mapear cada linea de PedidoEntity a LineaanuladaEntity
        for (var linea : pedido.getLineas()) {
            LineaanuladaEntity la = new LineaanuladaEntity();
            la.setCodigo(linea.getCodigo());
            la.setCantidad(linea.getCantidad());
            la.setArticulo(linea.getArticulo());
            la.setPedido(anulado);
            anulado.getLineas().add(la);
        }

        // 3. Guardar el pedido anulado
        anuladoRepo.save(anulado);

        // 4. Borrar el pedido original
        pedidoRepo.deleteById(codigoPedido);
    }
}

