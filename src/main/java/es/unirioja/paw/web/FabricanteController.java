package es.unirioja.paw.web;

import es.unirioja.paw.service.ArticuloFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/fabricantes")
public class FabricanteController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private ArticuloFinder articuloFinder;

    @Autowired
    public FabricanteController(ArticuloFinder articuloFinder) {
        this.articuloFinder = articuloFinder;
    }

    @GetMapping()
    public String redirectToSomewhere() {
        checkBeanIsPresent();
        return "redirect:/marcas";
    }

    private void checkBeanIsPresent() {
        if (articuloFinder == null) {
            logger.warn("Bean ArticuloFinder is null");
        } else {
            logger.info("Bean ArticuloFinder: {}", articuloFinder);
        }
    }

}
