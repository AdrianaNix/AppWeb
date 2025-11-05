// src/main/java/es/unirioja/paw/rest/ApiArticuloController.java
package es.unirioja.paw.rest;

import es.unirioja.paw.jpa.ArticuloEntity;
import es.unirioja.paw.jpa.Oferta3X2Entity;
import es.unirioja.paw.repository.ArticuloRepository;
import es.unirioja.paw.repository.OfertaRepository;
import es.unirioja.paw.rest.data.OfertaArticuloRestEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/articulo")
public class ApiArticuloController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ArticuloRepository articuloRepository;

    @Autowired
    private OfertaRepository ofertaRepository;

    @GetMapping("/{articuloId:.+}/oferta")
    @ResponseBody
    public OfertaArticuloRestEntity findOfertaArticulo(@PathVariable("articuloId") String articuloId) {

        Optional<ArticuloEntity> artOpt = articuloRepository.findById(articuloId);
        if (artOpt.isEmpty()) {
            logger.warn("Articulo {}: no encontrado", articuloId);
            return null;
        }
        Optional<Oferta3X2Entity> offOpt = ofertaRepository.findById(articuloId);
        if (offOpt.isEmpty()) {
            logger.warn("Articulo {}: no tiene oferta", articuloId);
            return null;
        }
        return buildOferta(artOpt.get(), offOpt.get());
    }

    private OfertaArticuloRestEntity buildOferta(ArticuloEntity articulo, Oferta3X2Entity oferta3x2) {
        OfertaArticuloRestEntity r = new OfertaArticuloRestEntity();
        r.setCodigoArticulo(oferta3x2.getCodigoArticulo());

        // Cálculo de vigencia a partir de fecha_fin
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime fin = oferta3x2.getFechaFin().toLocalDate().atStartOfDay();
        Duration duration = Duration.between(ahora, fin);
        long totalSec = Math.max(0, duration.getSeconds());
        r.setVigenciaDias(totalSec / (24 * 3600));
        r.setVigenciaHoras((totalSec % (24 * 3600)) / 3600);
        r.setVigenciaMinutos((totalSec % 3600) / 60);
        r.setVigenciaSegundos(totalSec % 60);

        // Precios y cantidades
        Double pvp = articulo.getPvp();
        r.setPrecioOriginal(pvp.floatValue());
        r.setCantidadComprada(oferta3x2.getaComprar());
        r.setCantidadPagada(oferta3x2.getaPagar());
        r.setPrecioOfertado(
                oferta3x2.getaPagar() * pvp.floatValue() / oferta3x2.getaComprar()
        );

        return r;
    }
}
