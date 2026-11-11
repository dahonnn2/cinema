import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Map;
import org.junit.jupiter.api.Test;

class CinemaAnalyticsTest {
    private Cinema cinema() {
        Cinema cinema = new Cinema();
        LocalDateTime t = LocalDateTime.of(2026, 11, 7, 18, 0);
        cinema.addScreening(new RegularScreening("A1", "Alpha", t, "H1", 1000),
                new SeatMap<Seat>(2, 5, (r, c) -> new StandardSeat(r, c)));
        cinema.addScreening(new RegularScreening("A2", "Alpha", t.plusHours(3), "H1", 1000),
                new SeatMap<Seat>(2, 5, (r, c) -> new StandardSeat(r, c)));
        cinema.addScreening(new RegularScreening("B1", "Beta", t, "H2", 1000),
                new SeatMap<Seat>(2, 5, (r, c) -> new StandardSeat(r, c)));
        return cinema;
    }

    @Test
    void revenueByFilmSumsAllSessionsOfFilm() {
        Cinema cinema = cinema();
        cinema.sellTicket("A1", 1, 1);
        cinema.sellTicket("A2", 1, 1);
        cinema.sellTicket("A2", 1, 2);
        cinema.sellTicket("B1", 1, 1);
        Map<String, Double> revenue = CinemaAnalytics.revenueByFilm(cinema);
        assertEquals(3000.0, revenue.get("Alpha"), 0.0001);
        assertEquals(1000.0, revenue.get("Beta"), 0.0001);
        assertEquals("Alpha", revenue.keySet().iterator().next());   // кему реті
    }

    @Test
    void occupancyBySessionIsSortedDescending() {
        Cinema cinema = cinema();
        cinema.sellTicket("B1", 1, 1);
        cinema.sellTicket("B1", 1, 2);
        cinema.sellTicket("A1", 1, 1);
        Map<Screening, Double> occupancy = CinemaAnalytics.occupancyBySession(cinema);
        Screening first = occupancy.keySet().iterator().next();
        assertEquals("B1", first.getId());
        assertEquals(20.0, occupancy.get(first), 0.0001);
    }

    @Test
    void unknownScreeningThrows() {
        assertThrows(IllegalArgumentException.class, () -> cinema().sellTicket("NOPE", 1, 1));
    }
}
