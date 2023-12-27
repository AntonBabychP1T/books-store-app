package store.bookstoreapp.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;
import store.bookstoreapp.dto.cartitem.CartItemDto;
import store.bookstoreapp.dto.cartitem.CartItemRequestDto;
import store.bookstoreapp.dto.cartitem.QuantityDto;
import store.bookstoreapp.dto.shoppingcart.ShoppingCartDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShoppingCartControllerTest {

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
                    new ClassPathResource("database/users/create-default-user-and-shoppingcart.sql")
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
                    connection, new ClassPathResource("database/users/delete-all-users.sql")
            );
        }
    }

    @WithMockUser(username = "test@email.com", roles = "USER")
    @Test
    @Sql(
            scripts = {
                    "classpath:database/books/add-default-book.sql",
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {
                    "classpath:database/shoppingcart/delete-all-from-cartitem.sql",
                    "classpath:database/books/delete-default-book.sql"
            },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Verify addBookToShoppingCart() method works")
    public void addBookToShoppingCart_ValidRequestDto_ReturnShoppingCartDto() throws Exception {
        CartItemRequestDto requestDto = new CartItemRequestDto(4L, 2);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        ShoppingCartDto expected = new ShoppingCartDto();
        expected.setUserId(1L);
        CartItemDto cartItemDto = new CartItemDto(1L, 4L, "Valid Title", 2);
        expected.setCartItems(List.of(cartItemDto));

        MvcResult result = mockMvc.perform(post("/api/cart")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        ShoppingCartDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), ShoppingCartDto.class);

        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @WithMockUser(username = "test@email.com", roles = "USER")
    @Test
    @DisplayName("Verify getShoppingCart() method works")
    public void getShippingCart_ValidEmail_ReturnShoppingCart() throws Exception {
        ShoppingCartDto expected = new ShoppingCartDto();
        expected.setUserId(1L);
        expected.setCartItems(Collections.emptyList());

        MvcResult result = mockMvc.perform(get("/api/cart")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        ShoppingCartDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), ShoppingCartDto.class);

        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @WithMockUser(username = "test@email.com", roles = "USER")
    @Test
    @Sql(
            scripts = {
                    "classpath:database/books/add-default-book.sql",
                    "classpath:database/shoppingcart/create-cart-item.sql",

            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {
                    "classpath:database/shoppingcart/delete-all-from-cartitem.sql",
                    "classpath:database/books/delete-default-book.sql",
            },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Verify deleteItemFromShoppingCart() method works")
    public void deleteItemFromShoppingCart_ValidCardItemInDb_StatusNoContent() throws Exception {
        mockMvc.perform(delete("/api/cart/cart-items/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username = "test@email.com", roles = "USER")
    @Test
    @Sql(
            scripts = {
                    "classpath:database/books/add-default-book.sql",
                    "classpath:database/shoppingcart/create-cart-item.sql",

            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {
                    "classpath:database/shoppingcart/delete-all-from-cartitem.sql",
                    "classpath:database/books/delete-default-book.sql",
            },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Verify updateQuantity() method works")
    public void updateQuantity_CartItemInDb_NewQuantityInCartItem() throws Exception {
        QuantityDto quantityDto = new QuantityDto(4);
        String jsonRequest = objectMapper.writeValueAsString(quantityDto);
        ShoppingCartDto expected = new ShoppingCartDto();
        expected.setUserId(1L);
        CartItemDto cartItemDto = new CartItemDto(1L, 4L, "Valid Title", 4);
        expected.setCartItems(List.of(cartItemDto));

        MvcResult result = mockMvc.perform(put("/api/cart/cart-items/{id}", 1L)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), ShoppingCartDto.class);

        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual);
    }
}


