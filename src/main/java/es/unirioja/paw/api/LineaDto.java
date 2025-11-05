package es.unirioja.paw.api;

public class LineaDto {
    private String producto;
    private Double precioReal;

    public LineaDto() {}
    public LineaDto(String producto, Double precioReal) {
        this.producto = producto;
        this.precioReal = precioReal;
    }
    public String getProducto() { return producto; }
    public void setProducto(String producto) { this.producto = producto; }
    public Double getPrecioReal() { return precioReal; }
    public void setPrecioReal(Double precioReal) { this.precioReal = precioReal; }
}