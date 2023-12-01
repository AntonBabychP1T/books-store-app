package store.bookstoreapp.repository;

import java.util.List;
import store.bookstoreapp.model.Book;

public interface BookRepository {

    Book save(Book book);

    List<Book> findAll();
}
