package es.unirioja.paw.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class BlogController {

    // Slugs válidos
  /*  private final List<String> validSlugs = List.of(
            "paw-2025",
            "tendencias-decorativas",
            "consejos-renovar-casa",
            "semana-verde"
    );*/

    @GetMapping("/blog/{slug}")
    public String showBlogEntry(@PathVariable("slug") String slug, Model model) {

        switch (slug) {
        case "paw-2023":
            model.addAttribute("blogTitle", "Electrosa con los estudiantes de la Universidad desde 2003");
            model.addAttribute("blogAuthor", "Francisco");
            model.addAttribute("blogDate", "1 Octubre, 2023");
            break;
        case "tendencias-decorativas":
            model.addAttribute("blogTitle", "Tendencias decorativas que no pasan de moda");
            model.addAttribute("blogAuthor", "Dustin");
            model.addAttribute("blogDate", "26 Diciembre, 2024");
            break;
        case "consejos-renovar-casa":
            model.addAttribute("blogTitle", "5 consejos clave para renovar tu casa");
            model.addAttribute("blogAuthor", "Tormund");
            model.addAttribute("blogDate", "9 Marzo, 2025");
            break;
        case "semana-verde":
            model.addAttribute("blogTitle", "Semana verde del ahorro en cocinas");
            model.addAttribute("blogAuthor", "Víctor");
            model.addAttribute("blogDate", "26 Enero, 2025");
            break;
        default:
            return "error/404"; 
        }
        model.addAttribute("slug", slug);
        return "blog";
    }
}
