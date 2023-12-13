package store.bookstoreapp.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import store.bookstoreapp.dto.book.BookDto;
import store.bookstoreapp.dto.book.CreateBookRequestDto;
import store.bookstoreapp.search.BookSearchParameters;

public interface BookService {

    BookDto save(CreateBookRequestDto requestDto);

    BookDto findBookById(Long id);

    List<BookDto> findAll(Pageable pageable);

    BookDto updateBook(Long id, CreateBookRequestDto requestDto);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParameters searchParameters,
                         Pageable pageable);
}
