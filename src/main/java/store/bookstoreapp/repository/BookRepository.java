package store.bookstoreapp.repository;

import java.util.List;
import java.util.Optional;
import store.bookstoreapp.model.Book;

public interface BookRepository {

    Book save(Book book);

    Optional<Book> findBookById(Long id);

    List<Book> findAll();
}
