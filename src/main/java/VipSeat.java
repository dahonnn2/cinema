public class VipSeat extends Seat {
    public VipSeat(int row, int number) {
        super(row, number);
    }

    @Override
    public double getPriceMultiplier() {
        return 1.8;
    }

    @Override
    public String getType() {
        return "VIP";
    }
}
