package store.bookstoreapp.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import store.bookstoreapp.model.Book;
import store.bookstoreapp.repository.book.BookRepository;

@DataJpaTest
@Sql(
        scripts = "classpath:database/books/add-default-book.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@Sql(
        scripts = "classpath:database/books/delete-default-book.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Find book by id")
    public void findBookById_ValidId_ReturnValidBook() {
        Book book = new Book();
        book.setId(4L);
        book.setAuthor("Valid Author");
        book.setTitle("Valid Title");
        book.setPrice(BigDecimal.valueOf(99.99));
        book.setIsbn("ValidISBN");
        book.setDescription("description");
        book.setCoverImage("coverimage");
        Book bookById = bookRepository.findBookById(4L).get();

        assertThat(bookById).isEqualTo(book);
    }
}
