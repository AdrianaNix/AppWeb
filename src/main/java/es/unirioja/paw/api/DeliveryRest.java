package es.unirioja.paw.api;

import es.unirioja.paw.jpa.PedidoEntity;
import es.unirioja.paw.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/delivery")
public class DeliveryRest {

    @Autowired
    private PedidoRepository pedidoRepo;

    @PostMapping("/{pedidoId}/schedule")
    public ResponseEntity<PedidoDto> schedule(
            @PathVariable("pedidoId") String pedidoId,
            @RequestBody ScheduleRequest req) {

        Optional<PedidoEntity> op = pedidoRepo.findById(pedidoId);
        if (!op.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        PedidoEntity pedido = op.get();
        if (pedido.getCursado() == null || pedido.getCursado() != 1) {
            pedido.setCursado(1);
            Instant now = Instant.now();
            Date entrega = Date.from(now.plus(req.getNdays(), ChronoUnit.DAYS));
            pedido.setFechacierre(entrega);
            pedidoRepo.save(pedido);
        }

        return ResponseEntity.ok(mapToDto(pedido));
    }

    @PostMapping("/{pedidoId}/schedule/cancel")
    public ResponseEntity<PedidoDto> cancelSchedule(
            @PathVariable("pedidoId") String pedidoId) {

        Optional<PedidoEntity> op = pedidoRepo.findById(pedidoId);
        if (!op.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        PedidoEntity pedido = op.get();
        if (pedido.getCursado() != null && pedido.getCursado() == 1) {
            pedido.setCursado(0);
            pedidoRepo.save(pedido);
        }

        return ResponseEntity.ok(mapToDto(pedido));
    }

    private PedidoDto mapToDto(PedidoEntity e) {
        PedidoDto dto = new PedidoDto();
        dto.setCodigo(e.getCodigo());
        dto.setCodigoCliente(e.getCodigoCliente());
        dto.setFechacierre(e.getFechacierre());
        dto.setCursado(e.getCursado());
        List<LineaDto> lineas = e.getLineas().stream()
                .map(l -> new LineaDto(l.getCodigo(), l.getPrecioReal()))
                .collect(Collectors.toList());
        dto.setLineas(lineas);
        return dto;
    }

    public static class ScheduleRequest {
        private int ndays;
        public int getNdays() { return ndays; }
        public void setNdays(int ndays) { this.ndays = ndays; }
    }
}
