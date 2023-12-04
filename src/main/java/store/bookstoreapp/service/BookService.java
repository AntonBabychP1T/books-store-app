package store.bookstoreapp.service;

import java.util.List;
import store.bookstoreapp.dto.BookDto;
import store.bookstoreapp.dto.CreateBookRequestDto;
import store.bookstoreapp.search.BookSearchParameters;

public interface BookService {

    BookDto save(CreateBookRequestDto requestDto);

    BookDto findBookById(Long id);

    List<BookDto> findAll();

    BookDto updateBook(Long id, CreateBookRequestDto requestDto);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParameters searchParameters);
}
