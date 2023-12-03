package store.bookstoreapp.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import store.bookstoreapp.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findBookById(Long id);
}
