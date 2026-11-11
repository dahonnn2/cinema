import java.util.Objects;

/**
 * Орын (зал сызбасындағы бір орын). SeatMap<T extends Seat> үшін шектеу ретінде қолданылады.
 */
public abstract class Seat implements Comparable<Seat> {
    private final int row;
    private final int number;

    protected Seat(int row, int number) {
        if (row < 1 || number < 1) {
            throw new IllegalArgumentException("Қатар мен орын нөмірі 1-ден кем болмауы керек");
        }
        this.row = row;
        this.number = number;
    }

    public int getRow() {
        return row;
    }

    public int getNumber() {
        return number;
    }

    /** Орын түріне байланысты баға коэффициенті. */
    public abstract double getPriceMultiplier();

    public abstract String getType();

    @Override
    public int compareTo(Seat other) {
        int byRow = Integer.compare(row, other.row);
        return byRow != 0 ? byRow : Integer.compare(number, other.number);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seat seat = (Seat) o;
        return row == seat.row && number == seat.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, number);
    }

    @Override
    public String toString() {
        return getType() + "[" + row + "-" + number + "]";
    }
}
