import java.time.LocalDateTime;

/** Бір мезгілде екі интерфейсті іске асырады: I3D және IVipSeats. */
public class IMAXScreening extends Screening implements I3D, IVipSeats {
    private static final double IMAX_COEFFICIENT = 1.5;

    private final double glassesPrice;
    private final double vipSurcharge;

    public IMAXScreening(String id, String film, LocalDateTime startTime, String hall,
                         double basePrice, double glassesPrice, double vipSurcharge) {
        super(id, film, startTime, hall, basePrice);
        if (glassesPrice < 0 || vipSurcharge < 0) {
            throw new IllegalArgumentException("Қосымша ақылар теріс болмауы керек");
        }
        this.glassesPrice = glassesPrice;
        this.vipSurcharge = vipSurcharge;
    }

    public IMAXScreening(IMAXScreening other) {
        super(other);
        this.glassesPrice = other.glassesPrice;
        this.vipSurcharge = other.vipSurcharge;
    }

    @Override
    public double calcTicketPrice(Seat seat) {
        double price = getBasePrice() * seat.getPriceMultiplier() * IMAX_COEFFICIENT + glassesPrice;
        return isVipSeat(seat) ? price + vipSurcharge : price;
    }

    @Override
    public String getFormat() {
        return "IMAX 3D";
    }

    @Override
    public Screening copy() {
        return new IMAXScreening(this);
    }

    // ---- I3D ----
    @Override
    public double getGlassesPrice() {
        return glassesPrice;
    }

    @Override
    public String get3DTechnology() {
        return "IMAX Laser 3D";
    }

    // ---- IVipSeats ----
    @Override
    public double getVipSurcharge() {
        return vipSurcharge;
    }

    @Override
    public boolean isVipSeat(Seat seat) {
        return seat instanceof VipSeat;
    }
}
