package es.unirioja.paw.service;

import es.unirioja.paw.jpa.ArticuloEntity;
import es.unirioja.paw.jpa.CestaCompraEntity;
import es.unirioja.paw.jpa.LineaCestaCompraEntity;
import es.unirioja.paw.repository.ArticuloRepository;
import es.unirioja.paw.repository.CestaCompraRepository;
import es.unirioja.paw.repository.LineaCestaCompraRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CestaCompraUseCaseImpl implements CestaCompraUseCase {

    @Autowired
    private CestaCompraRepository cestaRepo;
    @Autowired
    private ArticuloRepository articuloRepo;
    @Autowired
    private LineaCestaCompraRepository lineaRepo;

    @Override
    @Transactional
    public Optional<AddToCartResponse> add(AddToCartRequest r) {
        CestaCompraEntity cesta = r.getCesta();

        // 1) buscar línea existente
        Optional<LineaCestaCompraEntity> maybeLinea
                = cesta.findLineaArticulo(r.getCodigoArticulo());

        LineaCestaCompraEntity linea;
        if (maybeLinea.isPresent()) {
            // 2a) incrementar
            linea = maybeLinea.get();
            linea.incrementCantidad(1);
        } else {
            // 2b) crear nueva línea
            linea = new LineaCestaCompraEntity();
            ArticuloEntity art = articuloRepo.findById(r.getCodigoArticulo())
                    .orElseThrow(() -> new IllegalArgumentException("Artículo no encontrado"));
            linea.setArticulo(art);
            linea.setCantidad(1);
            linea.setPrecio(art.getPvp());
            linea.setCesta(cesta);
            cesta.getLineas().add(linea);
        }

        // 3) guardar
        cesta = cestaRepo.save(cesta);
        r.setCesta(cesta);

        return Optional.of(new AddToCartResponse(cesta, linea));
    }

    @Override
    @Transactional
    public Optional<UpdateCartLineResponse> update(String lineaId, int cantidad) {
        var lineaOpt = lineaRepo.findById(lineaId);
        if (lineaOpt.isEmpty()) {
            return Optional.empty();
        }
        var linea = lineaOpt.get();
        var cesta = linea.getCesta();

        if (cantidad <= 0) {
            // 1) Quita la línea de la colección
            cesta.getLineas().removeIf(l -> l.getCodigo().equals(lineaId));
            // 2) Guarda la cesta para que orphanRemoval elimine la línea
            cestaRepo.save(cesta);
        } else {
            // 3) Fija cantidad > 0 y guarda solo la línea
            linea.setCantidad(cantidad);
            lineaRepo.save(linea);
        }

        // 4) Recalcula total
        float total = (float) cesta.getLineas().stream()
                .mapToDouble(l -> l.getPrecio() * l.getCantidad())
                .sum();

        // 5) Subtotal = 0 si la borraste, o precio*cantidad
        float subtotal = (cantidad <= 0 ? 0f
                : linea.getPrecio().floatValue() * cantidad);

        return Optional.of(new UpdateCartLineResponse(lineaId, subtotal, total));
    }

}
