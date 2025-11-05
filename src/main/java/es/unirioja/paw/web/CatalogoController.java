package es.unirioja.paw.web;

import es.unirioja.paw.exception.ArticuloNotFoundException;
import es.unirioja.paw.jpa.ArticuloEntity;
import es.unirioja.paw.repository.ArticuloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/catalogo")
public class CatalogoController {

    private static final int PAGE_SIZE = 15;

    @Autowired
    private ArticuloRepository articuloRepository;

    @GetMapping
    public String verCatalogo(
            @RequestParam(name = "p", defaultValue = "1") int numPagina,
            @RequestParam(name = "fabricante", defaultValue = "-1") String fabricante,
            @RequestParam(name = "tipo", defaultValue = "-1") String tipo,
            @RequestParam(name = "precio", defaultValue = "-1") String precio, // solo para recordar selección
            Model model) {

        // 1) Paginación y orden
        PageRequest pageReq = PageRequest.of(numPagina - 1, PAGE_SIZE,
                Sort.by("nombre").ascending());

        // 2) Preparamos ejemplo para filtro dinámico
        ArticuloEntity probe = new ArticuloEntity();
        if (!"-1".equals(fabricante)) {
            probe.setFabricante(fabricante);
        }
        if (!"-1".equals(tipo)) {
            probe.setTipo(tipo);
        }

        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnoreNullValues()
                .withStringMatcher(StringMatcher.CONTAINING)
                .withIgnorePaths("codigo", "descripcion", "pvp", "foto", "rating");

        // 3) Ejecutamos consulta con Example
        Page<ArticuloEntity> page = articuloRepository.findAll(
                Example.of(probe, matcher),
                pageReq
        );

        // 4) Pasamos al modelo
        model.addAttribute("page", page);
        model.addAttribute("currentPage", numPagina);
        model.addAttribute("fabricante", fabricante);
        model.addAttribute("tipo", tipo);
        model.addAttribute("precio", precio);
        return "catalogo";
    }

    @GetMapping("/{articleId}")
    public String verFicha(@PathVariable("articleId") String codigo, Model model) {
        ArticuloEntity art = articuloRepository.findById(codigo)
                .orElseThrow(() -> new ArticuloNotFoundException("Artículo con código " + codigo + " no encontrado."));
        model.addAttribute("articulo", art);
        return "ficha";
    }
}
