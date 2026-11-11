import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * 6-ЗЖ: жалпыланған репозиторий. Шектеу: T extends Screening (нұсқаның абстрактілі класы).
 * Iterable<T> іске асырылған, сондықтан for-each арқылы айналып өтуге болады.
 */
public class Repository<T extends Screening> implements Iterable<T> {
    private final List<T> items = new ArrayList<>();

    public void add(T item) {
        Objects.requireNonNull(item, "Объект көрсетілмеген");
        if (findById(item.getId()).isPresent()) {
            throw new IllegalArgumentException("Бұл идентификаторлы сеанс бар: " + item.getId());
        }
        items.add(item);
    }

    public boolean remove(String id) {
        return items.removeIf(item -> item.getId().equals(id));
    }

    public Optional<T> findById(String id) {
        return items.stream().filter(item -> item.getId().equals(id)).findFirst();
    }

    public List<T> getAll() {
        return Collections.unmodifiableList(items);
    }

    public List<T> findAll(Predicate<? super T> predicate) {
        return items.stream().filter(predicate).toList();
    }

    public int size() {
        return items.size();
    }

    @Override
    public Iterator<T> iterator() {
        return getAll().iterator();
    }
}
