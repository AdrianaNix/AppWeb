package es.unirioja.paw.web.data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Datos estadisticos del pedido
 */
public class PedidoStat {

    /**
     * Codigo de pedido
     */
    private final String uuid;

    /**
     * Region destino de envio (PB, IC, IN)
     */
    private final String shippingRegion;

    /**
     * Fecha de envio
     */
    private final LocalDate shippingAt;

    /**
     * Fecha de entrega
     */
    private final LocalDate deliveryAt;

    /**
     * Diferencia en dias
     */
    private final long transitTimeAsDays;

    /**
     * Mes de envio
     */
    private final int shippingMonth;

    public PedidoStat(String uuid, ShippingRegion shippingRegion, LocalDate shippingAt, LocalDate deliveryAt) {
        this.uuid = uuid;
        this.shippingRegion = shippingRegion.toString();
        this.shippingAt = shippingAt;
        this.deliveryAt = deliveryAt;
        this.shippingMonth = shippingAt.getMonthValue();
        this.transitTimeAsDays = ChronoUnit.DAYS.between(shippingAt, deliveryAt);
    }

    public String getUuid() {
        return uuid;
    }

    public String getShippingRegion() {
        return shippingRegion;
    }

    public String getShippingAt() {
        return shippingAt.format(DateTimeFormatter.ISO_DATE);
    }

    public String getDeliveryAt() {
        return deliveryAt.format(DateTimeFormatter.ISO_DATE);
    }

    public long getTransitTimeAsDays() {
        return transitTimeAsDays;
    }

    public int getShippingMonth() {
        return shippingMonth;
    }

}