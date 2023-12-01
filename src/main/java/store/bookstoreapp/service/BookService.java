package store.bookstoreapp.service;

import java.util.List;
import store.bookstoreapp.dto.BookDto;
import store.bookstoreapp.dto.CreateBookRequestDto;

public interface BookService {

    BookDto save(CreateBookRequestDto requestDto);

    BookDto findBookById(Long id);

    List<BookDto> findAll();
}
