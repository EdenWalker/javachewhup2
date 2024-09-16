    public class OnlineSale extends Sale {
        public static final double ADDITIONAL_OVERHEAD_PERCENTAGE = 0.20;
        public static final double DELIVERY_FEE = 10.0;
        private boolean platform;

        public OnlineSale() {
            super("Online");
            this.platform = false;
        }
        public boolean isPlatform() {
            return platform;
        }
        @Override
        public double calculateFinalPrice(double basePrice) {
            double priceWithOverhead = basePrice* (1 + ADDITIONAL_OVERHEAD_PERCENTAGE);
            this.finalPrice = (priceWithOverhead + DELIVERY_FEE);
            return finalPrice;
        }

        public void setPlatform(boolean platform) {
            this.platform = platform;
        }

    }
