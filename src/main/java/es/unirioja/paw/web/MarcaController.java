package es.unirioja.paw.web;

import es.unirioja.paw.jpa.Fabricante;
import es.unirioja.paw.service.ArticuloFinder;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/marcas")
public class MarcaController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ArticuloFinder articuloFinder;

    @GetMapping()
    public String index(Model model) {
        checkBeanIsPresent();
        return fabricantes(model);
    }

    @GetMapping("/todas")
    public String all(Model model) {
        return fabricantes(model);
    }

    private String fabricantes(Model model) {
        List<Fabricante> itemCollection = articuloFinder.findFabricantes();
        model.addAttribute("fabricantes", itemCollection);
        logger.info("Fabricantes encontrados: {}", itemCollection.size());
        return "ejemplo/marca-list";
    }

    @GetMapping("/{marca:.+}")
    public String detail(Model model, @PathVariable("marca") String marca) {
        model.addAttribute("marca", marca);
        return "ejemplo/marca-detail";
    }

    private void checkBeanIsPresent() {
        if (articuloFinder == null) {
            logger.warn("Bean ArticuloFinder is null");
        } else {
            logger.info("Bean ArticuloFinder: {}", articuloFinder);
        }
    }

}
