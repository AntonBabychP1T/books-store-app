package store.bookstoreapp.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;
import store.bookstoreapp.dto.book.BookDtoWithoutCategoryIds;
import store.bookstoreapp.dto.category.CategoryDto;
import store.bookstoreapp.dto.category.CategoryRequestDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {

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
                    connection,
                    new ClassPathResource("database/categories/add-three-category-in-db.sql")
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
                    connection,
                    new ClassPathResource("database/categories/delete-all-from-categories.sql")
            );
        }
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/categories/delete-default-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Verify createCategory() method works")
    public void createCategory_ValidCategory_ReturnCategoryDto() throws Exception {
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Valid Category");
        requestDto.setDescription("Valid description");
        CategoryDto expected = new CategoryDto(4L,"Valid Category","Valid description");
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CategoryDto.class);

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.id());
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @WithMockUser(username = "user", roles = "USER")
    @Test
    @DisplayName("getAll() categories")
    public void getAll_ThreeValidCategoryInDb_ShouldReturnAllCategories() throws Exception {
        CategoryDto firstDto = new CategoryDto(1L, "First name", "Description");
        CategoryDto secondDto = new CategoryDto(2L, "Second name", "Description");
        CategoryDto thirdDto = new CategoryDto(3L, "Third name", "Description");
        List<CategoryDto> expected = new ArrayList<>();
        expected.add(firstDto);
        expected.add(secondDto);
        expected.add(thirdDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(),
                CategoryDto[].class);

        Assertions.assertEquals(3, actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @WithMockUser(username = "user", roles = "USER")
    @Test
    @DisplayName("Verify getCategoryById() with endpoint /{id}")
    public void getCategoryById_ValidId_ReturnCategoryDto() throws Exception {
        CategoryDto expected = new CategoryDto(1L, "First name", "Description");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/categories/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CategoryDto.class);

        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/categories/add-default-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/categories/delete-default-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Verify deleteCategory() method works")
    public void deleteCategory_ValidIdAndCategoryInDb_DeleteCategoryFromDb() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/categories/{id}", 4L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Verify updateCategory() method works")
    @Sql(
            scripts = "classpath:database/categories/add-default-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/categories/delete-default-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void updateCategory_ValidRequestDtoAndId_UpdatedCategoryInDb() throws Exception {
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("New Valid Category");
        requestDto.setDescription("New Valid description");
        CategoryDto expected = new CategoryDto(
                4L,
                "New Valid Category",
                "New Valid description"
        );
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/categories/{id}",4L)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CategoryDto.class);

        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @WithMockUser(username = "user", roles = "USER")
    @Test
    @Sql(scripts = {
            "classpath:database/categories/add-default-category.sql",
            "classpath:database/books/add-default-book.sql",
            "classpath:database/categories/set-category-to-default-book.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/categories/clear-books-category-table.sql",
            "classpath:database/categories/delete-default-category.sql",
            "classpath:database/books/delete-default-book.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Verify getBookCategoryById() method works")
    public void getBookByCategoryId_ValidId_BooksDtosWithoutCategoryIds() throws Exception {
        BookDtoWithoutCategoryIds bookDto = new BookDtoWithoutCategoryIds();
        bookDto.setId(4L);
        bookDto.setTitle("Valid Title");
        bookDto.setAuthor("Valid Author");
        bookDto.setIsbn("ValidISBN");
        bookDto.setPrice(BigDecimal.valueOf(99.99));
        bookDto.setDescription("description");
        bookDto.setCoverImage("coverimage");
        List<BookDtoWithoutCategoryIds> expected = List.of(bookDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/categories/{id}/books", 4L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDtoWithoutCategoryIds[] actual = objectMapper.readValue(result
                .getResponse().getContentAsByteArray(), BookDtoWithoutCategoryIds[].class);

        Assertions.assertArrayEquals(expected.toArray(new BookDtoWithoutCategoryIds[0]), actual);
    }
}
