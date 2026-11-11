import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class ScreeningTest {
    private static final LocalDateTime TIME = LocalDateTime.of(2026, 11, 7, 19, 0);

    @Test
    void imaxIsMoreExpensiveThanRegular() {
        Seat seat = new StandardSeat(1, 1);
        Screening regular = new RegularScreening("R", "Film", TIME, "Hall", 2000);
        Screening imax = new IMAXScreening("I", "Film", TIME, "IMAX", 2000, 500, 1000);
        assertTrue(imax.calcTicketPrice(seat) > regular.calcTicketPrice(seat));
    }

    @Test
    void vipSurchargeAppliesOnlyToVipSeats() {
        VipScreening s = new VipScreening("V", "Film", TIME, "VIP", 1000, 500);
        assertEquals(1000.0, s.calcTicketPrice(new StandardSeat(1, 1)), 0.0001);
        assertEquals(1000 * 1.8 + 500, s.calcTicketPrice(new VipSeat(1, 2)), 0.0001);
    }

    @Test
    void copyIsIndependentFromOriginal() {
        Screening original = new RegularScreening("R", "Film", TIME, "Hall", 2000);
        Screening copy = original.copy();
        copy.setFilm("Other");
        assertEquals("Film", original.getFilm());
        assertEquals(original, copy);   // id бірдей
    }

    @Test
    void invalidPriceIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new RegularScreening("R", "Film", TIME, "Hall", 0));
    }

    @Test
    void screeningsAreSortedByStartTime() {
        Screening late = new RegularScreening("L", "Film", TIME.plusHours(3), "Hall", 1000);
        Screening early = new RegularScreening("E", "Film", TIME, "Hall", 1000);
        assertTrue(early.compareTo(late) < 0);
    }
}
