package store.bookstoreapp.repository.book;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import store.bookstoreapp.model.Book;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    Optional<Book> findBookById(Long id);

    List<Book> findAllByCategoriesId(Long categoryId, Pageable pageable);
}
