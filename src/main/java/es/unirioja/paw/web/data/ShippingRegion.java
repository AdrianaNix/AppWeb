package es.unirioja.paw.web.data;

public enum ShippingRegion {

    PeninsulaBaleares("PB") {
        @Override
        public int maxTransitDays() {
            return 4;
        }
    },
    IslasCanarias("IC") {
        @Override
        public int maxTransitDays() {
            return 7;
        }
    },
    Internacional("IN") {
        @Override
        public int maxTransitDays() {
            return 15;
        }
    },;

    private final String value;

    ShippingRegion(String value) {
        this.value = value;
    }

    public abstract int maxTransitDays();

    @Override
    public String toString() {
        return value;
    }

}
