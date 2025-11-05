/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.unirioja.paw.web;


import es.unirioja.paw.web.data.PedidoStat;
import es.unirioja.paw.web.data.ShippingRegion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Controlador para la sección "Plazos de entrega".
 */
@Controller
public class DeliveryController {

    private final Logger logger = LoggerFactory.getLogger(DeliveryController.class);

    /**
     * Muestra la página de plazos de entrega con datos simulados.
     */
    @GetMapping("/about/delivery")
    public String delivery(Model model) {
        model.addAttribute("stats", mockPedidoStats());
        return "delivery";
    }

    private List<PedidoStat> mockPedidoStats() {
        return IntStream.range(0, 200)
                .mapToObj(this::mockPedidoStat)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private PedidoStat mockPedidoStat(int i) {
        logger.debug("Generando pedido {}", i);
        // primer dia del año pasado
        LocalDate firstDayOfYear = LocalDate.now().minusYears(1).withDayOfYear(1);
        Random random = new Random();
        // fecha de envio aleatoria 
        // no se asegura que se generen pedidos para todos los meses, puedes modificar el codigo si lo estimas
        LocalDate shippingAt = firstDayOfYear.plusDays(
                random.nextInt(firstDayOfYear.lengthOfYear())
        );
        // region destino aleatoria
        ShippingRegion region = ShippingRegion.values()[random.nextInt(ShippingRegion.values().length)];
        // fecha de entrega aleatoria en funcion del destino
        LocalDate deliveryAt = shippingAt.plusDays(random.nextInt(region.maxTransitDays() - 1));

        PedidoStat e = new PedidoStat(
                UUID.randomUUID().toString(),
                region,
                shippingAt,
                deliveryAt
        );
        logger.debug("Pedido {}: {} dias", e.getShippingRegion(), e.getTransitTimeAsDays());
        return e;
    }
}
