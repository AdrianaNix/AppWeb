package es.unirioja.paw.rest;

import com.github.javafaker.Faker;
import es.unirioja.paw.jpa.ArticuloEntity;
import es.unirioja.paw.repository.ArticuloRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api/compras")
public class ApiComprasController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Faker faker = new Faker(new Locale("es"));

    @Autowired
    private ArticuloRepository articuloRepository;

    @GetMapping("/ultimo-articulo")
    public ArticuloEntity randomArticulo() {
        long count = articuloRepository.count();
        if (count == 0) {
            logger.warn("No hay artículos en el repositorio");
            return null;
        }

        // número de página entre 0 y count-1
        int pageNumber = faker.number().numberBetween(0, (int)count);
        Pageable pg = PageRequest.of(pageNumber, 1, Sort.by("codigo"));
        Page<ArticuloEntity> page = articuloRepository.findAll(pg);

        if (page.hasContent()) {
            ArticuloEntity art = page.getContent().get(0);
            logger.info("Último artículo (pseudoaleatorio): {}", art.getCodigo());
            return art;
        } else {
            logger.warn("Página {} sin contenido", pageNumber);
            return null;
        }
    }
}
