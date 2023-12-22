package store.bookstoreapp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    private static final Long VALID_ID = 1L;
    private static final Long NOT_VALID_ID = 100L;
    private static final String VALID_TITLE = "Valid Title";
    private static final String VALID_AUTHOR = "Valid Author";
    private static final String VALID_ISBN = "ValidISNB";
    private static final BigDecimal VALID_PRICE = BigDecimal.valueOf(9.99);
    private static final String VALID_DESCRIPTION = "Valid description";
    private static final String VALID_COVER_IMAGE = "Valid cover image";

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;

    @Test
    @DisplayName("Verify save() method works")
    public void save_ValidCreateBookRequestDto_ReturnBookDto() {
        //Given
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setAuthor(VALID_AUTHOR);
        requestDto.setTitle(VALID_TITLE);
        requestDto.setPrice(VALID_PRICE);
        requestDto.setIsbn(VALID_ISBN);
        requestDto.setDescription(VALID_DESCRIPTION);
        requestDto.setCoverImage(VALID_COVER_IMAGE);

        Book book = new Book();
        book.setAuthor(requestDto.getAuthor());
        book.setTitle(requestDto.getTitle());
        book.setPrice(requestDto.getPrice());
        book.setIsbn(requestDto.getIsbn());
        book.setDescription(requestDto.getDescription());
        book.setCoverImage(requestDto.getCoverImage());

        BookDto bookDto = new BookDto();
        bookDto.setAuthor(book.getAuthor());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setId(VALID_ID);
        bookDto.setPrice(book.getPrice());
        bookDto.setTitle(book.getTitle());
        bookDto.setCoverImage(book.getCoverImage());
        bookDto.setDescription(book.getDescription());
        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        //when
        BookDto savedBook = bookService.save(requestDto);

        //then
        assertThat(savedBook).isEqualTo(bookDto);
    }

    @Test
    @DisplayName("Verify findAll() method works")
    public void findAll_ValidPageable_ReturnsAllBooks() {
        //given
        Book book = new Book();
        book.setId(VALID_ID);
        book.setAuthor(VALID_AUTHOR);
        book.setTitle(VALID_TITLE);
        book.setPrice(VALID_PRICE);
        book.setIsbn(VALID_ISBN);
        book.setDescription(VALID_DESCRIPTION);
        book.setCoverImage(VALID_COVER_IMAGE);

        BookDto bookDto = new BookDto();
        bookDto.setAuthor(book.getAuthor());
        bookDto.setId(book.getId());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setTitle(book.getTitle());
        bookDto.setCoverImage(book.getCoverImage());
        bookDto.setDescription(book.getDescription());

        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = List.of(book);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        //when
        List<BookDto> bookDtos = bookService.findAll(pageable);

        //then

        assertThat(bookDtos).hasSize(1);
        assertThat(bookDtos.get(0)).isEqualTo(bookDto);

    }

    @Test
    @DisplayName("Verify findBookById() method works")
    public void findBookById_ValidId_ReturnBookDto() {
        Book book = new Book();
        book.setId(VALID_ID);
        book.setAuthor(VALID_AUTHOR);
        book.setTitle(VALID_TITLE);
        book.setPrice(VALID_PRICE);
        book.setIsbn(VALID_ISBN);
        book.setDescription(VALID_DESCRIPTION);
        book.setCoverImage(VALID_COVER_IMAGE);

        BookDto bookDto = new BookDto();
        bookDto.setAuthor(book.getAuthor());
        bookDto.setId(book.getId());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setTitle(book.getTitle());
        bookDto.setCoverImage(book.getCoverImage());
        bookDto.setDescription(book.getDescription());

        when(bookMapper.toDto(book)).thenReturn(bookDto);
        when(bookRepository.findBookById(VALID_ID)).thenReturn(Optional.of(book));

        //when
        BookDto bookById = bookService.findBookById(VALID_ID);

        //then
        assertThat(bookById).isEqualTo(bookDto);
    }

    @Test
    @DisplayName("Verify findBookById() method works")
    public void findBookById_notValidId_ThrowException() {
        //given
        when(bookRepository.findBookById(NOT_VALID_ID)).thenReturn(Optional.empty());

        //when
        RuntimeException exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.findBookById(NOT_VALID_ID));
        //then
        String expectedMessage = "Can't find book with id: " + NOT_VALID_ID;
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Verify updateBook() method works")
    public void updateBook_ValidIdAndCreateBookRequestDto_ReturnBookDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setAuthor(VALID_AUTHOR);
        requestDto.setTitle(VALID_TITLE);
        requestDto.setPrice(VALID_PRICE);
        requestDto.setIsbn(VALID_ISBN);
        requestDto.setDescription(VALID_DESCRIPTION);
        requestDto.setCoverImage(VALID_COVER_IMAGE);

        Book bookToUpdate = new Book();
        bookToUpdate.setAuthor(requestDto.getAuthor());
        bookToUpdate.setTitle("New Valid Title");
        bookToUpdate.setPrice(requestDto.getPrice());
        bookToUpdate.setIsbn(requestDto.getIsbn());
        bookToUpdate.setDescription(requestDto.getDescription());
        bookToUpdate.setCoverImage(requestDto.getCoverImage());

        BookDto bookDto = new BookDto();
        bookDto.setAuthor(bookToUpdate.getAuthor());
        bookDto.setId(bookToUpdate.getId());
        bookDto.setIsbn(bookToUpdate.getIsbn());
        bookDto.setTitle(bookToUpdate.getTitle());
        bookDto.setCoverImage(bookToUpdate.getCoverImage());
        bookDto.setDescription(bookToUpdate.getDescription());

        when(bookRepository.findBookById(VALID_ID)).thenReturn(Optional.of(bookToUpdate));
        when(bookRepository.save(bookToUpdate)).thenReturn(bookToUpdate);
        when(bookMapper.toDto(bookToUpdate)).thenReturn(bookDto);

        //when
        BookDto updatedBook = bookService.updateBook(VALID_ID, requestDto);

        //then
        verify(bookMapper).updateBookFromDto(requestDto, bookToUpdate);
        assertThat(updatedBook).isEqualTo(bookDto);
    }

    @Test
    @DisplayName("Verify updateBook() method works")
    public void updateBook_notValidId_ThrowException() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setAuthor(VALID_AUTHOR);
        requestDto.setTitle(VALID_TITLE);
        requestDto.setPrice(VALID_PRICE);
        requestDto.setIsbn(VALID_ISBN);
        requestDto.setDescription(VALID_DESCRIPTION);
        requestDto.setCoverImage(VALID_COVER_IMAGE);
        //given
        when(bookRepository.findBookById(NOT_VALID_ID)).thenReturn(Optional.empty());

        //when
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.updateBook(NOT_VALID_ID, requestDto));
        //then
        String expectedMessage = "Can't find book with id: " + NOT_VALID_ID;
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Verify deleteById method works")
    public void deleteById_ValidId_NoValue() {
        bookService.deleteById(VALID_ID);
        verify(bookRepository).deleteById(VALID_ID);
    }

    @Test
    @DisplayName("Verify search method works ")
    public void search_ValidBookSearchParameters_BooksDtosList() {
        Book book = new Book();
        book.setId(VALID_ID);
        book.setAuthor(VALID_AUTHOR);
        book.setTitle(VALID_TITLE);
        book.setPrice(VALID_PRICE);
        book.setIsbn(VALID_ISBN);
        book.setDescription(VALID_DESCRIPTION);
        book.setCoverImage(VALID_COVER_IMAGE);

        BookDto bookDto = new BookDto();
        bookDto.setAuthor(book.getAuthor());
        bookDto.setId(book.getId());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setTitle(book.getTitle());
        bookDto.setCoverImage(book.getCoverImage());
        bookDto.setDescription(book.getDescription());
        BookSearchParameters searchParameters = new BookSearchParameters(
                new String[]{"Valid Title"},
                new String[]{"Valid Author"},
                new String[]{}
        );
        Pageable pageable = PageRequest.of(0, 10);
        Specification<Book> bookSpecification = mock(Specification.class);
        when(bookSpecificationBuilder.build(searchParameters)).thenReturn(bookSpecification);
        List<Book> books = List.of(book);
        List<BookDto> bookDtos = List.of(bookDto);
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        when(bookRepository.findAll(bookSpecification, pageable)).thenReturn(new PageImpl<>(books));

        List<BookDto> resultsDtos = bookService.search(searchParameters, pageable);
        assertThat(resultsDtos).isEqualTo(bookDtos);
    }

    @Test
    @DisplayName("Verify getBookByCategoryId() method works")
    public void getBookByCategoryId_ValidIdAndValidPageable_BookDtosWithoutIds() {
        Long categoryId = 1L;
        Category category = new Category();
        category.setName("New Category");
        category.setDescription("New description");
        category.setId(categoryId);
        Book book = new Book();
        book.setId(VALID_ID);
        book.setAuthor(VALID_AUTHOR);
        book.setTitle(VALID_TITLE);
        book.setPrice(VALID_PRICE);
        book.setIsbn(VALID_ISBN);
        book.setDescription(VALID_DESCRIPTION);
        book.setCoverImage(VALID_COVER_IMAGE);
        book.setCategories(Set.of(category));
        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds = new BookDtoWithoutCategoryIds();
        bookDtoWithoutCategoryIds.setAuthor(book.getAuthor());
        bookDtoWithoutCategoryIds.setIsbn(book.getIsbn());
        bookDtoWithoutCategoryIds.setCoverImage(book.getCoverImage());
        bookDtoWithoutCategoryIds.setPrice(book.getPrice());
        bookDtoWithoutCategoryIds.setDescription(book.getDescription());
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = List.of(book);
        List<BookDtoWithoutCategoryIds> booksDtoWithoutCategoryIds =
                List.of(bookDtoWithoutCategoryIds);

        when(bookRepository.findAllByCategoriesId(categoryId, pageable)).thenReturn(books);
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(bookDtoWithoutCategoryIds);

        List<BookDtoWithoutCategoryIds> bookByCategoryId = bookService
                .getBookByCategoryId(categoryId, pageable);

        assertThat(bookByCategoryId).isEqualTo(booksDtoWithoutCategoryIds);
    }
}

