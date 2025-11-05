package es.unirioja.paw.rest;

import es.unirioja.paw.service.UpdateCartLineResponse;
import es.unirioja.paw.service.CestaCompraUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController                            
@RequestMapping("/api/cesta")               
public class ApiCestaController {

    @Autowired
    private CestaCompraUseCase cestaUseCase;

    // DTO interno para binding del JSON entrante
    public static class UpdateCartLineRequest {
        private String lineaId;
        private int cantidad;
        public String getLineaId() { return lineaId; }
        public void setLineaId(String lineaId) { this.lineaId = lineaId; }
        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    }

    @PostMapping("/linea")                   
    public ResponseEntity<UpdateCartLineResponse> updateLinea(
            @RequestBody UpdateCartLineRequest req) {
        return cestaUseCase.update(req.getLineaId(), req.getCantidad())
                .map(resp -> ResponseEntity.ok(resp))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}


