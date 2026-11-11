import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

class GenericUtilsTest {
    @Test
    void maxReturnsLargestElement() {
        assertEquals(9, GenericUtils.max(List.of(3, 9, 1, 7)));
        assertEquals("pear", GenericUtils.max(List.of("apple", "pear", "fig")));
    }

    @Test
    void maxOfEmptyCollectionThrows() {
        assertThrows(NoSuchElementException.class, () -> GenericUtils.max(List.<Integer>of()));
    }

    @Test
    void groupByGroupsByKey() {
        Map<Integer, List<String>> groups = GenericUtils.groupBy(List.of("a", "bb", "cc", "d"), String::length);
        assertEquals(2, groups.size());
        assertEquals(List.of("a", "d"), groups.get(1));
        assertEquals(List.of("bb", "cc"), groups.get(2));
    }
}
