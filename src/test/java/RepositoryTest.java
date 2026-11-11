import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class RepositoryTest {
    private Screening screening(String id, String film, int hour) {
        return new RegularScreening(id, film, LocalDateTime.of(2026, 11, 7, hour, 0), "Hall", 1000);
    }

    @Test
    void addAndFindById() {
        Repository<Screening> repo = new Repository<>();
        repo.add(screening("A", "Film A", 10));
        assertTrue(repo.findById("A").isPresent());
        assertTrue(repo.findById("B").isEmpty());
    }

    @Test
    void duplicateIdThrows() {
        Repository<Screening> repo = new Repository<>();
        repo.add(screening("A", "Film A", 10));
        assertThrows(IllegalArgumentException.class, () -> repo.add(screening("A", "Film B", 12)));
    }

    @Test
    void removeDeletesItem() {
        Repository<Screening> repo = new Repository<>();
        repo.add(screening("A", "Film A", 10));
        assertTrue(repo.remove("A"));
        assertFalse(repo.remove("A"));
        assertEquals(0, repo.size());
    }

    @Test
    void findAllFiltersByPredicate() {
        Repository<Screening> repo = new Repository<>();
        repo.add(screening("A", "Film A", 10));
        repo.add(screening("B", "Film B", 20));
        repo.add(screening("C", "Film C", 22));
        assertEquals(2, repo.findAll(s -> s.getStartTime().getHour() >= 20).size());
    }

    @Test
    void repositoryIsIterable() {
        Repository<Screening> repo = new Repository<>();
        repo.add(screening("A", "Film A", 10));
        repo.add(screening("B", "Film B", 20));
        int count = 0;
        for (Screening ignored : repo) {
            count++;
        }
        assertEquals(2, count);
    }
}
