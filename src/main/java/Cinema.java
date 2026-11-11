import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Кинотеатр: сеанстар репозиторийі және Map<Screening, SeatMap<Seat>> (сөздік: сеанс -> зал сызбасы).
 */
public class Cinema {
    private final Repository<Screening> screenings = new Repository<>();
    private final Map<Screening, SeatMap<Seat>> seatMaps = new LinkedHashMap<>();

    public void addScreening(Screening screening, SeatMap<Seat> seatMap) {
        screenings.add(screening);
        seatMaps.put(screening, seatMap);
    }

    public Repository<Screening> getScreenings() {
        return screenings;
    }

    public SeatMap<Seat> getSeatMap(String screeningId) {
        Screening screening = findScreening(screeningId);
        return seatMaps.get(screening);
    }

    public Map<Screening, SeatMap<Seat>> getAllSeatMaps() {
        return Collections.unmodifiableMap(seatMaps);
    }

    public Seat sellTicket(String screeningId, int row, int col) {
        return getSeatMap(screeningId).sell(row, col);
    }

    public Screening findScreening(String screeningId) {
        return screenings.findById(screeningId)
                .orElseThrow(() -> new IllegalArgumentException("Сеанс табылмады: " + screeningId));
    }
}
