/** VIP орындары бар сеанстар. */
public interface IVipSeats {
    double getVipSurcharge();

    boolean isVipSeat(Seat seat);
}
