public class ComfortSeat extends Seat {
    public ComfortSeat(int row, int number) {
        super(row, number);
    }

    @Override
    public double getPriceMultiplier() {
        return 1.3;
    }

    @Override
    public String getType() {
        return "Comfort";
    }
}
