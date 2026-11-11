import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/** 6-ЗЖ, 8-нұсқа сұраныстары (Stream API). */
public final class CinemaAnalytics {
    private CinemaAnalytics() {
    }

    /** Сұраныс 1: сеанстар бойынша толу деңгейі (%), толу деңгейі бойынша кему ретімен. */
    public static Map<Screening, Double> occupancyBySession(Cinema cinema) {
        return cinema.getAllSeatMaps().entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue().getOccupancy(), a.getValue().getOccupancy()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().getOccupancy(),
                        (a, b) -> a,
                        LinkedHashMap::new));
    }

    /** Сұраныс 2: фильмдер бойынша түсім, түсім бойынша кему ретімен. */
    public static Map<String, Double> revenueByFilm(Cinema cinema) {
        Map<String, Double> sums = cinema.getAllSeatMaps().entrySet().stream()
                .collect(Collectors.groupingBy(
                        e -> e.getKey().getFilm(),
                        Collectors.summingDouble(e -> e.getValue().calcRevenue(e.getKey()))));
        return sums.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new));
    }
}
