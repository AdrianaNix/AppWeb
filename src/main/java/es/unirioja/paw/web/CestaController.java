package es.unirioja.paw.web;

import es.unirioja.paw.jpa.CestaCompraEntity;
import es.unirioja.paw.jpa.ClienteEntity;
import es.unirioja.paw.service.AddToCartRequest;
import es.unirioja.paw.service.AddToCartResponse;
import es.unirioja.paw.service.CestaCompraUseCase;
import es.unirioja.paw.service.RealizarPedidoUseCase;
import es.unirioja.paw.web.form.PedidoForm;
import es.unirioja.paw.repository.CestaCompraRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping({"/cliente/cesta", "/clientes/cesta"})
public class CestaController {

    private static final Logger logger = LoggerFactory.getLogger(CestaController.class);

    @Autowired
    private CestaCompraUseCase cestaUseCase;

    @Autowired
    private RealizarPedidoUseCase realizarPedidoUseCase;

    @Autowired
    private CestaCompraRepository cestaRepo;

    /**
     * GET /cliente/cesta
     * Muestra la cesta de la compra.
     */
    @GetMapping
public String verCesta(HttpSession session, Model model) {
    // 1) obtén el cliente
    ClienteEntity cliente = (ClienteEntity) session.getAttribute(SessionConstants.CLIENTE_KEY);
    if (cliente == null) {
        return "redirect:/auth/login";
    }

    // 2) busca (o crea) la cesta en BD
    CestaCompraEntity cestaBD = 
         cestaRepo.findOneByCodigoCliente(cliente.getCodigo());
    if (cestaBD == null) {
        // misma lógica de creación que tenías
        cestaBD = new CestaCompraEntity();
        cestaBD.setCodigoCliente(cliente.getCodigo());
        cestaBD.setFechaInicio(LocalDateTime.now());
        cestaBD.setLineas(new ArrayList<>());
        cestaBD = cestaRepo.save(cestaBD);
    }

    // 3) actualiza el atributo de sesión para no desincronizar
    session.setAttribute(SessionConstants.CESTA_KEY, cestaBD);

    // 4) calcula total y renderiza
    model.addAttribute("cesta", cestaBD);
    double total = cestaBD.getLineas().stream()
        .mapToDouble(l -> l.getPrecio() * l.getCantidad())
        .sum();
    model.addAttribute("total", total);
    return "cesta";
}


    /**
     * POST /cliente/cesta
     * Añade un artículo a la cesta.
     */
    @PostMapping
    public String addToCart(@RequestParam("codigoArticulo") String codigoArticulo,
                            HttpSession session) {
        CestaCompraEntity cesta = cargaCestaSesion(session);
        if (cesta == null) {
            return "redirect:/auth/login";
        }

        AddToCartRequest req = new AddToCartRequest(codigoArticulo, cesta);
        Optional<AddToCartResponse> resp = cestaUseCase.add(req);
        resp.ifPresent(r ->
            session.setAttribute(SessionConstants.CESTA_KEY, r.getCesta())
        );

        return "redirect:/cliente/cesta";
    }

    /**
     * GET /cliente/cesta/realizar
     * Muestra el formulario para tramitar el pedido.
     */
    @GetMapping("/realizar")
    public String showRealizarForm(HttpSession session, Model model) {
        // garantizar que hay cesta
        CestaCompraEntity cesta = cargaCestaSesion(session);
        if (cesta == null || cesta.getLineas().isEmpty()) {
            return "redirect:/cliente/cesta";
        }
        model.addAttribute("pedidoForm", new PedidoForm());
        model.addAttribute("cesta", cesta);
        return "cesta-realizar";
    }

    /**
     * POST /cliente/cesta/realizar
     * Procesa el formulario y crea el Pedido.
     */
    @PostMapping("/realizar")
    public String doRealizarPedido(
            @Valid @ModelAttribute("pedidoForm") PedidoForm form,
            BindingResult br,
            HttpSession session,
            RedirectAttributes flash) {
        
            logger.info("Entrando en doRealizarPedido: form={}, errors={}", form, br.hasErrors());


        CestaCompraEntity cesta = cargaCestaSesion(session);
        if (cesta == null || cesta.getLineas().isEmpty()) {
            return "redirect:/cliente/cesta";
        }

        if (br.hasErrors()) {
            return "cesta-realizar";
        }

        // crear y guardar el pedido
        var pedido = realizarPedidoUseCase.realizarPedido(form, cesta);

        // limpiar cesta de sesión y BD
        session.removeAttribute(SessionConstants.CESTA_KEY);

        flash.addFlashAttribute("msgOk",
            "Pedido " + pedido.getCodigo() + " realizado con éxito");
        return "redirect:/cliente/pedidos/" + pedido.getCodigo();
    }

    /**
     * Recupera la cesta de la sesión o crea una nueva si no existe.
     */
    private CestaCompraEntity cargaCestaSesion(HttpSession session) {
        ClienteEntity cliente = (ClienteEntity)
            session.getAttribute(SessionConstants.CLIENTE_KEY);
        if (cliente == null) {
            logger.warn("No hay cliente en sesión al acceder a la cesta");
            return null;
        }

        CestaCompraEntity cesta =
            (CestaCompraEntity) session.getAttribute(SessionConstants.CESTA_KEY);
        if (cesta == null) {
            cesta = cestaRepo.findOneByCodigoCliente(cliente.getCodigo());
        }
        if (cesta == null) {
            logger.info("Cliente {}: creando nueva cesta", cliente.getCodigo());
            cesta = new CestaCompraEntity();
            cesta.setCodigoCliente(cliente.getCodigo());
            cesta.setFechaInicio(LocalDateTime.now());
            cesta.setLineas(new ArrayList<>());
            cesta = cestaRepo.save(cesta);
        }
        session.setAttribute(SessionConstants.CESTA_KEY, cesta);
        return cesta;
    }
}
