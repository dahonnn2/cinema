import java.time.LocalDateTime;

public class VipScreening extends Screening implements IVipSeats {
    private final double vipSurcharge;

    public VipScreening(String id, String film, LocalDateTime startTime, String hall,
                        double basePrice, double vipSurcharge) {
        super(id, film, startTime, hall, basePrice);
        if (vipSurcharge < 0) {
            throw new IllegalArgumentException("VIP үстемесі теріс болмауы керек");
        }
        this.vipSurcharge = vipSurcharge;
    }

    public VipScreening(VipScreening other) {
        super(other);
        this.vipSurcharge = other.vipSurcharge;
    }

    @Override
    public double calcTicketPrice(Seat seat) {
        double price = getBasePrice() * seat.getPriceMultiplier();
        return isVipSeat(seat) ? price + vipSurcharge : price;
    }

    @Override
    public String getFormat() {
        return "2D VIP";
    }

    @Override
    public Screening copy() {
        return new VipScreening(this);
    }

    @Override
    public double getVipSurcharge() {
        return vipSurcharge;
    }

    @Override
    public boolean isVipSeat(Seat seat) {
        return seat instanceof VipSeat;
    }
}
