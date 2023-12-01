package store.bookstoreapp.service;

import java.util.List;
import store.bookstoreapp.model.Book;

public interface BookService {

    Book save(Book book);

    List<Book> findAll();
}
