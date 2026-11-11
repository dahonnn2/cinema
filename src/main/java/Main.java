import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/** 6-ЗЖ демонстрациясы (8-нұсқа: Кинотеатр, SeatMap<T>). */
public class Main {

    public static void main(String[] args) {
        Cinema cinema = buildCinema();

        System.out.println("=== 1. Repository<T extends Screening>: айналып өту (Iterable) ===");
        for (Screening s : cinema.getScreenings()) {
            System.out.println(s);
        }

        System.out.println("\n=== 2. Репозиторий әдістері: findById, findAll(predicate) ===");
        System.out.println("findById(S2): " + cinema.getScreenings().findById("S2").orElse(null));
        System.out.println("findById(S99): " + cinema.getScreenings().findById("S99").isPresent());
        System.out.println("Тек 19:00-ден кейінгі сеанстар:");
        cinema.getScreenings()
                .findAll(s -> s.getStartTime().getHour() >= 19)
                .forEach(s -> System.out.println("  " + s));
        System.out.println("IKidsFriendly интерфейсін іске асыратындар:");
        cinema.getScreenings()
                .findAll(s -> s instanceof IKidsFriendly)
                .forEach(s -> System.out.println("  " + s));

        System.out.println("\n=== 3. Жалпыланған әдістер: max және groupBy ===");
        Screening latest = GenericUtils.max(cinema.getScreenings().getAll());
        System.out.println("Ең кеш басталатын сеанс (Comparable<Screening>): " + latest);
        Map<String, List<Screening>> byFormat = GenericUtils.groupBy(cinema.getScreenings(), Screening::getFormat);
        byFormat.forEach((format, list) -> System.out.println("  " + format + " -> " + list.size() + " сеанс"));
        Map<String, List<Screening>> byFilm = GenericUtils.groupBy(cinema.getScreenings(), Screening::getFilm);
        byFilm.forEach((film, list) -> System.out.println("  " + film + " -> " + list.size() + " сеанс"));

        System.out.println("\n=== 4. SeatMap<T>: билет сату, зал сызбасы ===");
        Seat seat = cinema.sellTicket("S2", 8, 9);
        System.out.println("Сатылды: " + cinema.findScreening("S2").getTicketInfo(seat));
        seat = cinema.sellTicket("S1", 5, 4);
        System.out.println("Сатылды: " + cinema.findScreening("S1").getTicketInfo(seat));
        try {
            cinema.sellTicket("S2", 8, 9);
        } catch (IllegalStateException e) {
            System.out.println("Қате (күтілген): " + e.getMessage());
        }
        try {
            cinema.sellTicket("S1", 50, 1);
        } catch (IllegalArgumentException e) {
            System.out.println("Қате (күтілген): " + e.getMessage());
        }
        System.out.println("S2 залының сызбасы (X — сатылған):");
        System.out.print(cinema.getSeatMap("S2").render());

        System.out.println("=== 5. SeatMap<VipSeat>: тип параметрі нақты кластан ===");
        SeatMap<VipSeat> vipHall = new SeatMap<>(2, 4, (r, c) -> new VipSeat(r, c));
        VipSeat vip = vipHall.sell(1, 2);   // сату нәтижесі — дәл VipSeat, түрлендіру (cast) қажет емес
        System.out.println("VIP залда сатылды: " + vip + ", коэффициент " + vip.getPriceMultiplier());
        // SeatMap<String> bad = ...;  // компиляция қатесі: String — Seat-тың мұрагері емес

        System.out.println("\n=== 6. Орындарды айналып өту (Iterable) ===");
        int comfort = 0;
        for (Seat s : cinema.getSeatMap("S1")) {
            if (s instanceof ComfortSeat) {
                comfort++;
            }
        }
        System.out.println("S1 залындағы Comfort орындар саны: " + comfort);

        System.out.println("\n=== 7. Сұраныстар (Stream API) ===");
        System.out.println("Сеанстар бойынша толу деңгейі:");
        CinemaAnalytics.occupancyBySession(cinema)
                .forEach((s, pct) -> System.out.printf("  %-45s %5.1f%%%n", s, pct));
        System.out.println("Фильмдер бойынша түсім:");
        CinemaAnalytics.revenueByFilm(cinema)
                .forEach((film, sum) -> System.out.printf("  %-15s %10.0f тг%n", film, sum));

        System.out.println("\n=== 8. Көшіру (copy) ===");
        Screening original = cinema.findScreening("S4");
        Screening copy = original.copy();
        copy.setFilm("Басқа фильм");
        System.out.println("Түпнұсқа: " + original.getFilm() + " | Көшірме: " + copy.getFilm());
    }

    /** Залды құрады: соңғы 2 қатар — VIP, алдыңғы 2 қатар — Comfort, қалғаны — Standard. */
    private static SeatMap<Seat> hall(int rows, int cols) {
        return new SeatMap<>(rows, cols, (r, c) ->
                r > rows - 2 ? new VipSeat(r, c)
                        : r > rows - 4 ? new ComfortSeat(r, c)
                        : new StandardSeat(r, c));
    }

    private static Cinema buildCinema() {
        Cinema cinema = new Cinema();
        cinema.addScreening(new RegularScreening("S1", "Interstellar",
                LocalDateTime.of(2026, 11, 7, 19, 0), "Зал 1", 2500), hall(8, 10));
        cinema.addScreening(new IMAXScreening("S2", "Interstellar",
                LocalDateTime.of(2026, 11, 7, 21, 30), "IMAX", 4000, 500, 1500), hall(8, 10));
        cinema.addScreening(new VipScreening("S3", "Dune",
                LocalDateTime.of(2026, 11, 7, 20, 0), "VIP зал", 3500, 2000), hall(6, 8));
        cinema.addScreening(new MorningShow("S4", "Inside Out 2",
                LocalDateTime.of(2026, 11, 7, 10, 0), "Зал 2", 2000), hall(8, 10));
        cinema.addScreening(new RegularScreening("S5", "Dune",
                LocalDateTime.of(2026, 11, 7, 16, 0), "Зал 1", 2500), hall(8, 10));

        // Бастапқы сатылымдар (түрлі толу деңгейі үшін)
        sellRange(cinema, "S1", 1, 4, 1, 10);
        sellRange(cinema, "S2", 6, 8, 1, 6);
        sellRange(cinema, "S3", 5, 6, 1, 8);
        sellRange(cinema, "S4", 1, 2, 1, 5);
        sellRange(cinema, "S5", 1, 1, 1, 3);
        return cinema;
    }

    private static void sellRange(Cinema cinema, String id, int r1, int r2, int c1, int c2) {
        for (int r = r1; r <= r2; r++) {
            for (int c = c1; c <= c2; c++) {
                cinema.sellTicket(id, r, c);
            }
        }
    }
}
