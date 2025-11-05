package es.unirioja.paw.service;

public class UpdateCartLineResponse {
    private String lineaId;
    private float subtotal;
    private float total;               // ← nuevo campo

    public UpdateCartLineResponse(String lineaId, float subtotal, float total) {
        this.lineaId  = lineaId;
        this.subtotal = subtotal;
        this.total    = total;
    }

    public String getLineaId()  { return lineaId;  }
    public float  getSubtotal() { return subtotal; }
    public float  getTotal()    { return total;    }
}
