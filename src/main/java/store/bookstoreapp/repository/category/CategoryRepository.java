package store.bookstoreapp.repository.category;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import store.bookstoreapp.model.Category;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    Optional<Category> findById(Long id);
}
