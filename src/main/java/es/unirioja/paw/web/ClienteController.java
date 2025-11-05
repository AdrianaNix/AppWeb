// src/main/java/es/unirioja/paw/web/ClienteController.java
package es.unirioja.paw.web;

import es.unirioja.paw.jpa.ClienteEntity;
import es.unirioja.paw.jpa.PedidoEntity;
import es.unirioja.paw.jpa.PedidoanuladoEntity;
import es.unirioja.paw.service.AnularPedidoUseCase;
import es.unirioja.paw.service.PedidoAnuladoService;
import es.unirioja.paw.service.PedidoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import es.unirioja.paw.exception.AccessNotAuthorizedException;
import es.unirioja.paw.exception.PedidoNotFoundException;

import java.util.List;

@Controller
@RequestMapping({"/cliente", "/clientes"})
public class ClienteController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private PedidoAnuladoService pedidoAnuladoService;

    @Autowired
    private AnularPedidoUseCase anularPedidoUC;

    @GetMapping("/cuenta")
    public String mostrarCuenta(HttpSession session, Model model) {
        ClienteEntity cliente = (ClienteEntity) session.getAttribute("cliente");
        if (cliente == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("cliente", cliente);
        return "cuenta";
    }

    @GetMapping("/pedidos")
    public String listarPedidos(HttpSession session, Model model) {
        ClienteEntity cliente = (ClienteEntity) session.getAttribute("cliente");
        if (cliente == null) {
            return "redirect:/auth/login";
        }
        List<PedidoEntity> pedidos = pedidoService.findByCliente(cliente.getCodigo());
        model.addAttribute("pedidos", pedidos);
        return "pedidos";
    }

    @GetMapping("/pedidos/{codigo}")
    public String detallePedido(@PathVariable("codigo") String codigoPedido,
            HttpSession session,
            Model model) {
        ClienteEntity cliente = (ClienteEntity) session.getAttribute("cliente");
        if (cliente == null) {
            return "redirect:/auth/login";
        }
        PedidoEntity pedido = pedidoService.findOne(codigoPedido);
        if (pedido == null) {
            throw new PedidoNotFoundException("El pedido con código " + codigoPedido + " no existe.");
        }

        if (!cliente.getCodigo().equals(pedido.getCodigoCliente())) {
            throw new AccessNotAuthorizedException("No está autorizado para consultar este pedido");
        }

        model.addAttribute("pedido", pedido);
        return "pedido";
    }

    @PostMapping("/pedidos/{codigo}/anular")
    public String anularPedido(@PathVariable("codigo") String codigoPedido,
            HttpSession session) {
        ClienteEntity cliente = (ClienteEntity) session.getAttribute("cliente");
        if (cliente == null) {
            return "redirect:/auth/login";
        }
        // Ejecutar los pasos de anulación
        anularPedidoUC.execute(codigoPedido);
        return "redirect:/cliente/pedidos/anulados";
    }

    @GetMapping("/pedidos/anulados")
    public String listarAnulados(HttpSession session, Model model) {
        ClienteEntity cliente = (ClienteEntity) session.getAttribute("cliente");
        if (cliente == null) {
            return "redirect:/auth/login";
        }
        List<PedidoanuladoEntity> anulados = pedidoAnuladoService.findByCliente(cliente.getCodigo());
        model.addAttribute("anulados", anulados);
        return "anulados";
    }

    @GetMapping("/pedidos/anulados/{codigo}")
    public String detalleAnulado(@PathVariable("codigo") String codigoPedido,
            HttpSession session,
            Model model) {
        ClienteEntity cliente = (ClienteEntity) session.getAttribute("cliente");
        if (cliente == null) {
            return "redirect:/auth/login";
        }
        PedidoanuladoEntity anulado = pedidoAnuladoService.findOne(codigoPedido);

        if (anulado == null) {
            throw new PedidoNotFoundException("El pedido anulado con código " + codigoPedido + " no existe.");
        }

        if (!cliente.getCodigo().equals(anulado.getCodigoCliente())) {
            throw new AccessNotAuthorizedException("No está autorizado para consultar este pedido");
        }

        model.addAttribute("anulado", anulado);
        return "anulado";
    }
}
