package es.unirioja.paw.api;

import java.util.Date;
import java.util.List;

public class PedidoDto {
    private String codigo;
    private String codigoCliente;
    private Date fechacierre;
    private Integer cursado;
    private List<LineaDto> lineas;

    // getters & setters
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getCodigoCliente() { return codigoCliente; }
    public void setCodigoCliente(String codigoCliente) { this.codigoCliente = codigoCliente; }
    public Date getFechacierre() { return fechacierre; }
    public void setFechacierre(Date fechacierre) { this.fechacierre = fechacierre; }
    public Integer getCursado() { return cursado; }
    public void setCursado(Integer cursado) { this.cursado = cursado; }
    public List<LineaDto> getLineas() { return lineas; }
    public void setLineas(List<LineaDto> lineas) { this.lineas = lineas; }
}
