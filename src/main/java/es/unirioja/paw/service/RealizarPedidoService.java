package es.unirioja.paw.service;

import es.unirioja.paw.jpa.*;
import es.unirioja.paw.repository.CestaCompraRepository;
import es.unirioja.paw.repository.PedidoRepository;
import es.unirioja.paw.web.form.PedidoForm;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RealizarPedidoService implements RealizarPedidoUseCase {

    @Autowired
    private PedidoRepository pedidoRepo;

    @Autowired
    private CestaCompraRepository cestaRepo;

    @Override
    public PedidoEntity realizarPedido(PedidoForm form, CestaCompraEntity cesta) {
        // 1) Crear PedidoEntity
        PedidoEntity pedido = new PedidoEntity();
        pedido.setCodigo(UUID.randomUUID().toString());
        pedido.setCodigoCliente(cesta.getCodigoCliente());
        pedido.setFechacierre(new Date());
        pedido.setCursado(0); // “no cursado” al crear
        

        // 2) Dirección embebida
        DireccionEntity dir = new DireccionEntity();
        dir.setCalle(form.getCalle());
        dir.setCp(form.getCp());
        dir.setCiudad(form.getCiudad());
        dir.setProvincia(form.getProvincia());
        pedido.setDireccion(dir);

        // 3) Copiar líneas de la cesta a líneas de pedido
        var lineasPedido = new ArrayList<LineaPedidoEntity>();
        for (var lineaC : cesta.getLineas()) {
            LineaPedidoEntity lp = new LineaPedidoEntity();
            lp.setCodigo(UUID.randomUUID().toString());
            lp.setArticulo(lineaC.getArticulo());
            lp.setCantidad(lineaC.getCantidad());
            lp.setPrecioReal(lineaC.getArticulo().getPvp());

            lp.setPrecioBase(lineaC.getPrecio());
            // Fecha entrega deseada de la forma
            lp.setFechaEntregaDeseada(java.sql.Date.valueOf(form.getFechaEntregaDeseada()));
            lp.setPedido(pedido);
            lineasPedido.add(lp);
        }
        pedido.setLineas(lineasPedido);
        pedido.buildImporte();

        // 4) Guardar pedido
        PedidoEntity saved = pedidoRepo.save(pedido);

        // 5) Borrar la cesta (persistente y en memoria)
        cestaRepo.deleteById(cesta.getCodigo());

        return saved;
    }
}
