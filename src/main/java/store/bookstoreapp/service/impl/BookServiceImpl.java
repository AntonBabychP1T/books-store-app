package store.bookstoreapp.service.impl;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import store.bookstoreapp.dto.BookDto;
import store.bookstoreapp.dto.CreateBookRequestDto;
import store.bookstoreapp.exception.EntityNotFoundException;
import store.bookstoreapp.mapper.BookMapper;
import store.bookstoreapp.model.Book;
import store.bookstoreapp.repository.book.BookRepository;
import store.bookstoreapp.repository.book.BookSpecificationBuilder;
import store.bookstoreapp.search.BookSearchParameters;
import store.bookstoreapp.service.BookService;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        return bookMapper.toDto(bookRepository.save(bookMapper.toModel(requestDto)));
    }

    @Override
    public BookDto findBookById(Long id) {
        Optional<Book> bookById = bookRepository.findBookById(id);
        if (bookById.isPresent()) {
            return bookMapper.toDto(bookById.get());
        }
        throw new EntityNotFoundException("Can't find book with id: " + id);
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).stream().map(bookMapper::toDto).toList();
    }

    @Override
    public BookDto updateBook(Long id, CreateBookRequestDto requestDto) {
        Book bookToUpdate = bookRepository.findBookById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book with id: " + id));
        bookMapper.updateBookFromDto(requestDto, bookToUpdate);
        return bookMapper.toDto(bookRepository.save(bookToUpdate));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDto> search(BookSearchParameters searchParameters, Pageable pageable) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(searchParameters);
        return bookRepository.findAll(bookSpecification,pageable).stream()
                .map(bookMapper::toDto)
                .toList();
    }
}
