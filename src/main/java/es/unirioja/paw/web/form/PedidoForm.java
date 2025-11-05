package es.unirioja.paw.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class PedidoForm {

    @NotBlank
    private String calle;

    @NotBlank
    private String cp;

    @NotBlank
    private String ciudad;

    @NotBlank
    private String provincia;

    @NotNull
    private LocalDate fechaEntregaDeseada;

    // getters / setters

    public String getCalle() { return calle; }
    public void setCalle(String calle) { this.calle = calle; }

    public String getCp() { return cp; }
    public void setCp(String cp) { this.cp = cp; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }

    public LocalDate getFechaEntregaDeseada() { return fechaEntregaDeseada; }
    public void setFechaEntregaDeseada(LocalDate fechaEntregaDeseada) {
        this.fechaEntregaDeseada = fechaEntregaDeseada;
    }
}
