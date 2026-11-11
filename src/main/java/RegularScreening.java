import java.time.LocalDateTime;

public class RegularScreening extends Screening {
    public RegularScreening(String id, String film, LocalDateTime startTime, String hall, double basePrice) {
        super(id, film, startTime, hall, basePrice);
    }

    public RegularScreening(RegularScreening other) {
        super(other);
    }

    @Override
    public double calcTicketPrice(Seat seat) {
        return getBasePrice() * seat.getPriceMultiplier();
    }

    @Override
    public String getFormat() {
        return "2D";
    }

    @Override
    public Screening copy() {
        return new RegularScreening(this);
    }
}
