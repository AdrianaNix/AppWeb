
package es.unirioja.paw.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class TeamController {

    @GetMapping("/about/team")
    public String teamPage(Model model) {
        // Aquí podrías añadir datos al modelo si lo deseas
        return "team"; // El nombre de la vista Thymeleaf
    }
}