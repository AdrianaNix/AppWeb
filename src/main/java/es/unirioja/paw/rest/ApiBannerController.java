package es.unirioja.paw.rest;

import es.unirioja.paw.jpa.ClienteEntity;
import es.unirioja.paw.web.SessionConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@RestController
@RequestMapping("/api/banner")
public class ApiBannerController {

    @GetMapping(value = "/header", produces = "application/json; charset=utf-8")

    @ResponseBody
    public String headerBanner(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String mensaje = "";
        Locale locale = request.getLocale();
        String mes = new SimpleDateFormat("MMMM", locale).format(new Date());

        // Si el usuario no está identificado como cliente
        if (session == null) {
            mensaje = "Envíos gratis en " + mes + " a partir de 130€";
        } else {
            ClienteEntity cliente = (ClienteEntity) session.getAttribute(SessionConstants.CLIENTE_KEY);
            if (cliente != null) {
                // Comprobamos si los puntos de fidelidad son nulos
                Integer puntosFidelidad = cliente.getPuntosFidelidad();
                
                if (puntosFidelidad != null) {
                    // Si el cliente tiene puntos de fidelidad menores a 100
                    if (puntosFidelidad < 100) {
                        mensaje = "Envíos gratis en " + mes + " a partir de 100€";
                    } 
                    // Si el cliente tiene entre 100 y 250 puntos
                    else if (puntosFidelidad >= 100 && puntosFidelidad <= 250) {
                        mensaje = "Gracias por tu fidelidad! Disfruta un <b>10%</b> de descuento exclusivo para clientes frecuentes.";
                    } 
                    // Si el cliente tiene más de 250 puntos
                    else {
                        mensaje = "Gracias por tu fidelidad! Disfruta un <b>15%</b> de descuento exclusivo para clientes frecuentes.";
                    }
                } else {
                    // Si los puntos de fidelidad son null, asignamos un valor predeterminado
                    mensaje = "Envíos gratis en " + mes + " a partir de 130€"; // Puedes asignar un valor por defecto aquí si es necesario
                }
            } else {
                mensaje = "Envíos gratis en " + mes + " a partir de 130€"; // En caso de que no haya cliente en la sesión
            }
        }

        // Devolver el mensaje como respuesta de texto
        return mensaje;
    }
}
