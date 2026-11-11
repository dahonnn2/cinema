public class StandardSeat extends Seat {
    public StandardSeat(int row, int number) {
        super(row, number);
    }

    @Override
    public double getPriceMultiplier() {
        return 1.0;
    }

    @Override
    public String getType() {
        return "Standard";
    }
}
