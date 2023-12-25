package store.bookstoreapp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import store.bookstoreapp.dto.book.BookDto;
import store.bookstoreapp.dto.book.BookDtoWithoutCategoryIds;
import store.bookstoreapp.dto.book.CreateBookRequestDto;
import store.bookstoreapp.exception.EntityNotFoundException;
import store.bookstoreapp.mapper.BookMapper;
import store.bookstoreapp.model.Book;
import store.bookstoreapp.model.Category;
import store.bookstoreapp.repository.book.BookRepository;
import store.bookstoreapp.repository.book.BookSpecificationBuilder;
import store.bookstoreapp.search.BookSearchParameters;
import store.bookstoreapp.service.impl.BookServiceImpl;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    private static final Long INVALID_ID = 100L;
    private static final Long VALID_ID = 1L;
    private static final BigDecimal VALID_PRICE = BigDecimal.valueOf(9.99);

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;

    private Book validBook;
    private BookDto validBookDto;
    private CreateBookRequestDto validCreateBookRequestDto;

    @BeforeEach
    void setUp() {
        validBook = createValidBook();
        validBookDto = createValidBookDto();
        validCreateBookRequestDto = createValidCreateBookRequestDto();
    }

    private Book createValidBook() {
        Book book = new Book();
        book.setId(VALID_ID);
        book.setAuthor("Valid Author");
        book.setTitle("Valid Title");
        book.setPrice(VALID_PRICE);
        book.setIsbn("ValidISNB");
        book.setDescription("Valid description");
        book.setCoverImage("Valid cover image");
        return book;
    }

    private BookDto createValidBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setAuthor(validBook.getAuthor());
        bookDto.setId(validBook.getId());
        bookDto.setIsbn(validBook.getIsbn());
        bookDto.setTitle(validBook.getTitle());
        bookDto.setCoverImage(validBook.getCoverImage());
        bookDto.setDescription(validBook.getDescription());
        return bookDto;
    }

    private CreateBookRequestDto createValidCreateBookRequestDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setAuthor(validBook.getAuthor());
        requestDto.setTitle(validBook.getTitle());
        requestDto.setPrice(validBook.getPrice());
        requestDto.setIsbn(validBook.getIsbn());
        requestDto.setDescription(validBook.getDescription());
        requestDto.setCoverImage(validBook.getCoverImage());
        return requestDto;
    }

    @Test
    @DisplayName("save() should return BookDto for valid request")
    public void save_WithValidRequest_ShouldReturnBookDto() {
        when(bookMapper.toModel(validCreateBookRequestDto)).thenReturn(validBook);
        when(bookRepository.save(validBook)).thenReturn(validBook);
        when(bookMapper.toDto(validBook)).thenReturn(validBookDto);

        BookDto savedBook = bookService.save(validCreateBookRequestDto);

        assertThat(savedBook).isEqualTo(validBookDto);
    }

    @Test
    @DisplayName("findAll() should return all books")
    public void findAll_WithValidPageable_ShouldReturnAllBooks() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = List.of(validBook);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());
        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(validBook)).thenReturn(validBookDto);

        List<BookDto> bookDtos = bookService.findAll(pageable);

        assertThat(bookDtos).hasSize(1);
        assertThat(bookDtos.get(0)).isEqualTo(validBookDto);
    }

    @Test
    @DisplayName("findBookById() should return BookDto for valid ID")
    public void findBookById_WithValidId_ShouldReturnBookDto() {
        when(bookRepository.findBookById(VALID_ID)).thenReturn(Optional.of(validBook));
        when(bookMapper.toDto(validBook)).thenReturn(validBookDto);

        BookDto bookById = bookService.findBookById(VALID_ID);

        assertThat(bookById).isEqualTo(validBookDto);
    }

    @Test
    @DisplayName("findBookById() should throw EntityNotFoundException for invalid ID")
    public void findBookById_WithInvalidId_ShouldThrowException() {
        when(bookRepository.findBookById(INVALID_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.findBookById(INVALID_ID)
        );

        assertEquals("Can't find book with id: " + INVALID_ID, exception.getMessage());
    }

    @Test
    @DisplayName("updateBook() should return updated BookDto for valid ID")
    public void updateBook_WithValidIdAndCreateBookRequestDto_ShouldReturnUpdatedBookDto() {
        when(bookRepository.findBookById(VALID_ID)).thenReturn(Optional.of(validBook));
        when(bookRepository.save(validBook)).thenReturn(validBook);
        when(bookMapper.toDto(validBook)).thenReturn(validBookDto);

        BookDto updatedBook = bookService.updateBook(VALID_ID, validCreateBookRequestDto);

        verify(bookMapper).updateBookFromDto(validCreateBookRequestDto, validBook);
        assertThat(updatedBook).isEqualTo(validBookDto);
    }

    @Test
    @DisplayName("updateBook() should throw EntityNotFoundException for invalid ID")
    public void updateBook_WithInvalidId_ShouldThrowException() {
        when(bookRepository.findBookById(INVALID_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.updateBook(INVALID_ID, validCreateBookRequestDto)
        );

        assertEquals("Can't find book with id: " + INVALID_ID, exception.getMessage());
    }

    @Test
    @DisplayName("deleteById() should invoke repository deletion for valid ID")
    public void deleteById_WithValidId_ShouldInvokeDeletion() {
        bookService.deleteById(VALID_ID);

        verify(bookRepository).deleteById(VALID_ID);
    }

    @Test
    @DisplayName("search should return a list of BookDtos based on valid search parameters")
    public void search_WithValidBookSearchParameters_ShouldReturnBooksDtosList() {
        BookSearchParameters searchParameters = new BookSearchParameters(
                new String[]{"Valid Title"},
                new String[]{"Valid Author"},
                new String[]{}
        );
        Pageable pageable = PageRequest.of(0, 10);
        Specification<Book> bookSpecification = mock(Specification.class);

        when(bookSpecificationBuilder.build(searchParameters)).thenReturn(bookSpecification);
        when(bookRepository.findAll(bookSpecification, pageable))
                .thenReturn(new PageImpl<>(List.of(validBook)));
        when(bookMapper.toDto(validBook)).thenReturn(validBookDto);

        List<BookDto> resultsDtos = bookService.search(searchParameters, pageable);

        assertThat(resultsDtos).containsExactly(validBookDto);
    }

    @Test
    @DisplayName("getBookByCategoryId should return a list of BookDtosWithoutCategoryIds "
            + "for valid ID and pageable")
    public void getBookByCategoryId_WithValidIdAndValidPageable_ShouldReturnBookDtosWithoutIds() {
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setName("New Category");
        category.setDescription("New description");
        validBook.setCategories(Set.of(category));
        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds
                = createBookDtoWithoutCategoryIds(validBook);
        Pageable pageable = PageRequest.of(0, 10);
        when(bookRepository.findAllByCategoriesId(categoryId, pageable))
                .thenReturn(List.of(validBook));
        when(bookMapper.toDtoWithoutCategories(validBook))
                .thenReturn(bookDtoWithoutCategoryIds);

        List<BookDtoWithoutCategoryIds> bookByCategoryId
                = bookService.getBookByCategoryId(categoryId, pageable);

        assertThat(bookByCategoryId).containsExactly(bookDtoWithoutCategoryIds);
    }

    private BookDtoWithoutCategoryIds createBookDtoWithoutCategoryIds(Book book) {
        BookDtoWithoutCategoryIds dto = new BookDtoWithoutCategoryIds();
        dto.setAuthor(book.getAuthor());
        dto.setIsbn(book.getIsbn());
        dto.setCoverImage(book.getCoverImage());
        dto.setPrice(book.getPrice());
        dto.setDescription(book.getDescription());
        return dto;
    }
}
