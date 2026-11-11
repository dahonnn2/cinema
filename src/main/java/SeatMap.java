import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Stream;

/**
 * 6-ЗЖ, 8-нұсқа: орын түрі параметр болатын зал сызбасы.
 * Шектеу: T extends Seat (SeatMap<String> компиляция кезінде қабылданбайды).
 * Коллекциялар: List<List<T>> (екі өлшемді сызба) және Set<T> (сатылған орындар жиыны).
 */
public class SeatMap<T extends Seat> implements Iterable<T> {
    private final int rows;
    private final int cols;
    private final List<List<T>> grid = new ArrayList<>();
    private final Set<T> soldSeats = new HashSet<>();

    public SeatMap(int rows, int cols, BiFunction<Integer, Integer, ? extends T> seatFactory) {
        if (rows < 1 || cols < 1) {
            throw new IllegalArgumentException("Қатарлар мен орындар саны оң болуы керек");
        }
        if (seatFactory == null) {
            throw new IllegalArgumentException("Орын фабрикасы көрсетілмеген");
        }
        this.rows = rows;
        this.cols = cols;
        for (int r = 1; r <= rows; r++) {
            List<T> line = new ArrayList<>();
            for (int c = 1; c <= cols; c++) {
                line.add(seatFactory.apply(r, c));
            }
            grid.add(line);
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getTotalSeats() {
        return rows * cols;
    }

    /** Нөмірлеу 1-ден басталады. */
    public T getSeat(int row, int col) {
        if (row < 1 || row > rows || col < 1 || col > cols) {
            throw new IllegalArgumentException("Мұндай орын жоқ: қатар " + row + ", орын " + col);
        }
        return grid.get(row - 1).get(col - 1);
    }

    public boolean isSold(int row, int col) {
        return soldSeats.contains(getSeat(row, col));
    }

    public T sell(int row, int col) {
        T seat = getSeat(row, col);
        if (!soldSeats.add(seat)) {
            throw new IllegalStateException("Орын бос емес: " + seat);
        }
        return seat;
    }

    public boolean release(int row, int col) {
        return soldSeats.remove(getSeat(row, col));
    }

    public int getSoldCount() {
        return soldSeats.size();
    }

    public Set<T> getSoldSeats() {
        return Collections.unmodifiableSet(soldSeats);
    }

    public List<T> getFreeSeats() {
        return stream().filter(s -> !soldSeats.contains(s)).toList();
    }

    /** Толу деңгейі, %. */
    public double getOccupancy() {
        return 100.0 * soldSeats.size() / getTotalSeats();
    }

    /** Берілген сеанстың бағасы бойынша сатылған билеттерден түскен түсім. */
    public double calcRevenue(Screening screening) {
        return soldSeats.stream().mapToDouble(screening::calcTicketPrice).sum();
    }

    public Stream<T> stream() {
        return grid.stream().flatMap(List::stream);
    }

    @Override
    public Iterator<T> iterator() {
        return stream().iterator();
    }

    /** Консольге шығару: X — сатылған, . — бос. */
    public String render() {
        StringBuilder sb = new StringBuilder();
        for (int r = 1; r <= rows; r++) {
            sb.append(String.format("%2d  ", r));
            for (int c = 1; c <= cols; c++) {
                sb.append(isSold(r, c) ? "X " : ". ");
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }
}
