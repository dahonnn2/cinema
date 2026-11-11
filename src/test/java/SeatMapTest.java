import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class SeatMapTest {
    private SeatMap<Seat> newMap() {
        return new SeatMap<>(2, 5, (r, c) -> new StandardSeat(r, c));
    }

    @Test
    void sellMarksSeatAsSold() {
        SeatMap<Seat> map = newMap();
        map.sell(1, 3);
        assertTrue(map.isSold(1, 3));
        assertFalse(map.isSold(1, 4));
        assertEquals(1, map.getSoldCount());
    }

    @Test
    void sellingSameSeatTwiceThrows() {
        SeatMap<Seat> map = newMap();
        map.sell(2, 2);
        assertThrows(IllegalStateException.class, () -> map.sell(2, 2));
    }

    @Test
    void invalidCoordinatesThrow() {
        SeatMap<Seat> map = newMap();
        assertThrows(IllegalArgumentException.class, () -> map.getSeat(0, 1));
        assertThrows(IllegalArgumentException.class, () -> map.getSeat(3, 1));
        assertThrows(IllegalArgumentException.class, () -> map.getSeat(1, 6));
    }

    @Test
    void occupancyIsCalculatedInPercent() {
        SeatMap<Seat> map = newMap();   // 10 орын
        map.sell(1, 1);
        map.sell(1, 2);
        map.sell(2, 1);
        assertEquals(30.0, map.getOccupancy(), 0.0001);
    }

    @Test
    void releaseFreesSeat() {
        SeatMap<Seat> map = newMap();
        map.sell(1, 1);
        assertTrue(map.release(1, 1));
        assertFalse(map.release(1, 1));
        assertEquals(0, map.getSoldCount());
        assertEquals(10, map.getFreeSeats().size());
    }

    @Test
    void revenueUsesScreeningPriceAndSeatType() {
        Screening s = new RegularScreening("T1", "Film", LocalDateTime.of(2026, 11, 7, 18, 0), "Hall", 1000);
        SeatMap<Seat> map = new SeatMap<>(1, 2, (r, c) -> c == 1 ? new StandardSeat(r, c) : new VipSeat(r, c));
        map.sell(1, 1);   // 1000 * 1.0
        map.sell(1, 2);   // 1000 * 1.8
        assertEquals(2800.0, map.calcRevenue(s), 0.0001);
    }

    @Test
    void iterationVisitsAllSeats() {
        SeatMap<Seat> map = newMap();
        int count = 0;
        for (Seat ignored : map) {
            count++;
        }
        assertEquals(map.getTotalSeats(), count);
    }
}
