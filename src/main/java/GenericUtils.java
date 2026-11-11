import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.function.Function;

/** 6-ЗЖ: жалпыланған әдістер (кемінде 2). */
public final class GenericUtils {
    private GenericUtils() {
    }

    /** Салыстырылатын элементтердің ең үлкенін табады. */
    public static <T extends Comparable<? super T>> T max(Collection<? extends T> items) {
        if (items.isEmpty()) {
            throw new NoSuchElementException("Коллекция бос");
        }
        Iterator<? extends T> it = items.iterator();
        T best = it.next();
        while (it.hasNext()) {
            T candidate = it.next();
            if (candidate.compareTo(best) > 0) {
                best = candidate;
            }
        }
        return best;
    }

    /** Элементтерді кілт бойынша топтайды (кірістіру реті сақталады). */
    public static <T, K> Map<K, List<T>> groupBy(Iterable<? extends T> items,
                                                 Function<? super T, ? extends K> keyFn) {
        Map<K, List<T>> result = new LinkedHashMap<>();
        for (T item : items) {
            result.computeIfAbsent(keyFn.apply(item), k -> new ArrayList<>()).add(item);
        }
        return result;
    }
}
