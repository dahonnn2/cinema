import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * 5-ЗЖ: абстрактілі класс (8-нұсқа, кинотеатр). 6-ЗЖ-да Repository<T extends Screening> үшін шектеу.
 */
public abstract class Screening implements Comparable<Screening> {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final String id;
    private String film;
    private LocalDateTime startTime;
    private String hall;
    private double basePrice;

    protected Screening(String id, String film, LocalDateTime startTime, String hall, double basePrice) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Сеанс идентификаторы бос болмауы керек");
        }
        this.id = id;
        setFilm(film);
        setStartTime(startTime);
        setHall(hall);
        setBasePrice(basePrice);
    }

    /** Көшіру конструкторы (терең көшіру: барлық өрістер қарапайым немесе өзгермейтін типтер). */
    protected Screening(Screening other) {
        this(other.id, other.film, other.startTime, other.hall, other.basePrice);
    }

    // ---- абстрактілі әдістер ----
    public abstract double calcTicketPrice(Seat seat);

    public abstract String getFormat();

    public abstract Screening copy();

    // ---- үлгілік (template) әдіс ----
    public final String getTicketInfo(Seat seat) {
        return String.format("%s | %s | %s | %s | %s | %.0f тг",
                film, getFormat(), startTime.format(FMT), hall, seat, calcTicketPrice(seat));
    }

    // ---- қасиеттер ----
    public String getId() {
        return id;
    }

    public String getFilm() {
        return film;
    }

    public final void setFilm(String film) {
        if (film == null || film.isBlank()) {
            throw new IllegalArgumentException("Фильм атауы бос болмауы керек");
        }
        this.film = film;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public final void setStartTime(LocalDateTime startTime) {
        this.startTime = Objects.requireNonNull(startTime, "Сеанс уақыты көрсетілмеген");
    }

    public String getHall() {
        return hall;
    }

    public final void setHall(String hall) {
        if (hall == null || hall.isBlank()) {
            throw new IllegalArgumentException("Зал атауы бос болмауы керек");
        }
        this.hall = hall;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public final void setBasePrice(double basePrice) {
        if (basePrice <= 0) {
            throw new IllegalArgumentException("Базалық баға оң болуы керек");
        }
        this.basePrice = basePrice;
    }

    // ---- салыстыру: уақыты бойынша ----
    @Override
    public int compareTo(Screening other) {
        int byTime = startTime.compareTo(other.startTime);
        return byTime != 0 ? byTime : id.compareTo(other.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return id.equals(((Screening) o).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s: %s (%s), %s, %s", id, film, getFormat(), startTime.format(FMT), hall);
    }
}
