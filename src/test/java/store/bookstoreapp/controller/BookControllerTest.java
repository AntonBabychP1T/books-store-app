package store.bookstoreapp.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;
import store.bookstoreapp.dto.book.BookDto;
import store.bookstoreapp.dto.book.CreateBookRequestDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {

    private static final String VALID_TITLE = "Valid Title";
    private static final String VALID_AUTHOR = "Valid Author";
    private static final String VALID_ISBN = "ValidISNB";
    private static final BigDecimal VALID_PRICE = BigDecimal.valueOf(9.99);
    private static final String VALID_DESCRIPTION = "Valid description";
    private static final String VALID_COVER_IMAGE = "Valid cover image";

    private static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection, new ClassPathResource("database/books/add-three-books.sql")
            );
        }
    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource
    ) throws SQLException {
        teardown(dataSource);
    }

    static void teardown(DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection, new ClassPathResource("database/books/delete-all-from-books.sql")
            );
        }
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/books/delete-default-book.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Create a new book")
    public void createBook_ValidRequestDto_ReturnBookDto() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setAuthor(VALID_AUTHOR);
        requestDto.setTitle(VALID_TITLE);
        requestDto.setPrice(VALID_PRICE);
        requestDto.setIsbn(VALID_ISBN);
        requestDto.setDescription(VALID_DESCRIPTION);
        requestDto.setCoverImage(VALID_COVER_IMAGE);

        BookDto expected = new BookDto();
        expected.setTitle(requestDto.getTitle());
        expected.setAuthor(requestDto.getAuthor());
        expected.setPrice(requestDto.getPrice());
        expected.setIsbn(requestDto.getIsbn());
        expected.setCoverImage(requestDto.getCoverImage());
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/api/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BookDto.class);

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");

    }

    @WithMockUser(username = "user", roles = "USER")
    @Test
    @DisplayName("Get all books")
    public void getAll_ValidBooksInDb_ShouldReturnAllBooks() throws Exception {
        BookDto firstBook = new BookDto();
        firstBook.setId(1L);
        firstBook.setTitle("First title");
        firstBook.setAuthor("First Author");
        firstBook.setPrice(BigDecimal.valueOf(9.99));
        firstBook.setIsbn("1ISBN01");
        firstBook.setDescription("description");
        firstBook.setCoverImage("coverimage");
        BookDto secondBook = new BookDto();
        secondBook.setId(2L);
        secondBook.setAuthor("Second Author");
        secondBook.setTitle("Second title");
        secondBook.setPrice(BigDecimal.valueOf(19.99));
        secondBook.setIsbn("2ISBN02");
        secondBook.setDescription("description");
        secondBook.setCoverImage("coverimage");
        BookDto thirdBook = new BookDto();
        thirdBook.setId(3L);
        thirdBook.setAuthor("Third Author");
        thirdBook.setTitle("Third title");
        thirdBook.setPrice(BigDecimal.valueOf(19.99));
        thirdBook.setIsbn("3ISBN03");
        thirdBook.setDescription("description");
        thirdBook.setCoverImage("coverimage");
        List<BookDto> expected = new ArrayList<>();
        expected.add(firstBook);
        expected.add(secondBook);
        expected.add(thirdBook);

        MvcResult result = mockMvc.perform(get("/api/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(),
                BookDto[].class);
        Assertions.assertEquals(3, actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @WithMockUser(username = "user", roles = "USER")
    @Test
    @DisplayName("Verify getBookById with endpoint /{id}")
    public void getBookById_ValidId_ReturnBookDto() throws Exception {
        BookDto expected = new BookDto();
        expected.setId(1L);
        expected.setTitle("First title");
        expected.setPrice(BigDecimal.valueOf(9.99));
        expected.setIsbn("1ISBN01");
        expected.setDescription("description");
        expected.setCoverImage("coverimage");

        MvcResult result = mockMvc.perform(get("/api/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BookDto.class);

        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/books/add-default-book.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/books/delete-default-book.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Verify delete() method works")
    public void delete_ValidId_ReturnNoContentStatus() throws Exception {

        ResultActions resultActions = mockMvc.perform(delete("/api/books/{id}",4L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
