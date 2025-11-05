package es.unirioja.paw.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

    @GetMapping("/")
    public String index(Model model) {
        // Aquí podrías añadir datos al modelo si lo deseas
        return "landing"; // El nombre de la vista Thymeleaf
    }
}
