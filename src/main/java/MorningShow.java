import java.time.LocalDateTime;

public class MorningShow extends Screening implements IKidsFriendly {
    private static final double MORNING_DISCOUNT = 0.3;
    private static final int KIDS_AGE_LIMIT = 12;
    private static final double KIDS_DISCOUNT = 0.5;

    public MorningShow(String id, String film, LocalDateTime startTime, String hall, double basePrice) {
        super(id, film, startTime, hall, basePrice);
    }

    public MorningShow(MorningShow other) {
        super(other);
    }

    @Override
    public double calcTicketPrice(Seat seat) {
        return getBasePrice() * seat.getPriceMultiplier() * (1 - MORNING_DISCOUNT);
    }

    /** Балалар билеті: таңғы жеңілдіктің үстіне балалар жеңілдігі. */
    public double calcKidsPrice(Seat seat) {
        return calcTicketPrice(seat) * (1 - KIDS_DISCOUNT);
    }

    @Override
    public String getFormat() {
        return "2D Таңғы сеанс";
    }

    @Override
    public Screening copy() {
        return new MorningShow(this);
    }

    @Override
    public int getKidsAgeLimit() {
        return KIDS_AGE_LIMIT;
    }

    @Override
    public double getKidsDiscount() {
        return KIDS_DISCOUNT;
    }
}
