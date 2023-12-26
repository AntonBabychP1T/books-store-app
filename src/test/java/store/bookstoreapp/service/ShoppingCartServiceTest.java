package store.bookstoreapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.bookstoreapp.dto.book.BookDto;
import store.bookstoreapp.dto.cartitem.CartItemDto;
import store.bookstoreapp.dto.cartitem.CartItemRequestDto;
import store.bookstoreapp.dto.shoppingcart.ShoppingCartDto;
import store.bookstoreapp.mapper.BookMapper;
import store.bookstoreapp.mapper.CartItemMapper;
import store.bookstoreapp.mapper.ShoppingCartMapper;
import store.bookstoreapp.model.Book;
import store.bookstoreapp.model.CartItem;
import store.bookstoreapp.model.ShoppingCart;
import store.bookstoreapp.model.User;
import store.bookstoreapp.repository.cartitem.CartItemRepository;
import store.bookstoreapp.repository.shoppingcart.ShoppingCartRepository;
import store.bookstoreapp.repository.user.UserRepository;
import store.bookstoreapp.service.impl.ShoppingCartServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {
    private static final Long VALID_ID = 1L;
    private static final User DEFAULT_USER = createValidUser();
    private static final Book VALID_BOOK = createValidBook();
    private static final String VALID_EMAIL = "user@example.com";

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private ShoppingCartMapper shoppingCartMapper;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private BookService bookService;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartItemMapper cartItemMapper;

    private ShoppingCart validShoppingCart;
    private CartItem validCartItem;
    private CartItemDto validCartItemDto;
    private ShoppingCartDto validShoppingCartDto;

    @BeforeAll
    static void beforeAll() {
    }

    private static User createValidUser() {
        User user = new User();
        user.setId(VALID_ID);
        user.setEmail(VALID_EMAIL);
        user.setPassword("Password");
        user.setRoles(Collections.emptySet());
        user.setLastName("Last Name");
        user.setFirstName("First Name");
        user.setShippingAddress("Address");
        return user;
    }

    private static Book createValidBook() {
        Book book = new Book();
        book.setId(VALID_ID);
        book.setAuthor("Valid Author");
        book.setTitle("Valid Title");
        book.setPrice(BigDecimal.valueOf(9.99));
        book.setIsbn("ValidISNB");
        book.setDescription("Valid description");
        book.setCoverImage("Valid cover image");
        book.setCategories(Collections.emptySet());
        return book;
    }

    @BeforeEach
    void setUp() {
        validShoppingCart = createValidShoppingCart();
        validCartItem = createValidCartItem();
        validCartItemDto = createValidCartItemDto();
        validShoppingCartDto = createValidShoppingCartDto();
    }

    private ShoppingCartDto createValidShoppingCartDto() {
        ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
        shoppingCartDto.setUserId(VALID_ID);
        shoppingCartDto.setId(VALID_ID);
        shoppingCartDto.setCartItems(List.of(validCartItemDto));
        return shoppingCartDto;
    }

    private CartItem createValidCartItem() {
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(2);
        cartItem.setShoppingCart(validShoppingCart);
        cartItem.setBook(VALID_BOOK);
        cartItem.setId(VALID_ID);
        return cartItem;
    }

    private CartItemDto createValidCartItemDto() {
        return new CartItemDto(VALID_ID, VALID_ID, VALID_BOOK.getTitle(), 1);
    }

    private BookDto createValidBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setId(VALID_ID);
        bookDto.setAuthor("Valid Author");
        bookDto.setTitle("Valid Title");
        bookDto.setPrice(BigDecimal.valueOf(9.99));
        bookDto.setIsbn("ValidISNB");
        bookDto.setDescription("Valid description");
        bookDto.setCoverImage("Valid cover image");
        bookDto.setCategoryIds(Collections.emptySet());
        return bookDto;
    }

    private ShoppingCart createValidShoppingCart() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(VALID_ID);
        shoppingCart.setUser(DEFAULT_USER);
        shoppingCart.setCartItems(new HashSet<>());
        return shoppingCart;
    }

    @Test
    @DisplayName("Verify addBookToShoppingCart() method works")
    public void addBookToShoppingCart_ValidRequestDto_ReturnShoppingCartDto() {
        User user = new User();
        user.setId(VALID_ID);
        ShoppingCart shoppingCart = createValidShoppingCart();
        shoppingCart.setUser(user);
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));
        when(shoppingCartRepository.getShoppingCartById(VALID_ID)).thenReturn(shoppingCart);
        BookDto bookDto = createValidBookDto();
        when(bookService.findBookById(VALID_ID)).thenReturn(bookDto);
        Book book = createValidBook();
        when(bookMapper.toModel(bookDto)).thenReturn(book);
        CartItem cartItem = createValidCartItem();
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(shoppingCart);
        ShoppingCartDto expectedShoppingCartDto = createValidShoppingCartDto();
        when(shoppingCartMapper.toDto(any(ShoppingCart.class))).thenReturn(expectedShoppingCartDto);
        CartItemRequestDto requestDto = new CartItemRequestDto(VALID_ID, 1);

        ShoppingCartDto result = shoppingCartService.addBookToShoppingCart(requestDto, VALID_EMAIL);

        assertEquals(expectedShoppingCartDto, result);
    }

    @Test
    @DisplayName("Verify getShoppingCart() method works")
    public void getShoppingCart_ValidEmail_ReturnShoppingCartDto() {
        when(userRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(DEFAULT_USER));
        when(shoppingCartRepository.getShoppingCartById(DEFAULT_USER.getId()))
                .thenReturn(validShoppingCart);
        when(shoppingCartMapper.toDto(validShoppingCart)).thenReturn(validShoppingCartDto);

        ShoppingCartDto actual = shoppingCartService.getShoppingCart(VALID_EMAIL);

        assertEquals(actual, validShoppingCartDto);
    }

    @Test
    @DisplayName("Verify deleteItemFromShoppingCart() method works")
    public void deleteItemFromShoppingCart_ValidIdAndEmail_CallDeleteById() {
        shoppingCartService.deleteItemFromShoppingCart(VALID_ID, VALID_EMAIL);

        verify(cartItemRepository).deleteById(VALID_ID);
    }

    @Test
    @DisplayName("Verify updateQuantity() method updates the quantity of a cart item")
    public void updateQuantity_ValidIdAndQuantity_UpdatesCartItem() {
        Integer newQuantity = 3;
        CartItemDto cartItemDto = createValidCartItemDto();
        CartItemDto expectedCartItemDto
                = new CartItemDto(
                cartItemDto.id(),
                cartItemDto.bookId(),
                cartItemDto.bookTitle(),
                newQuantity);
        when(userRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(DEFAULT_USER));
        when(shoppingCartRepository.getShoppingCartById(DEFAULT_USER.getId()))
                .thenReturn(validShoppingCart);
        validShoppingCart.getCartItems().add(validCartItem);
        when(cartItemRepository.save(validCartItem)).thenReturn(validCartItem);
        when(cartItemMapper.toDto(validCartItem)).thenReturn(expectedCartItemDto);

        CartItemDto actualCartItemDto = shoppingCartService
                .updateQuantity(VALID_ID, newQuantity, VALID_EMAIL);

        assertEquals(expectedCartItemDto, actualCartItemDto);
        assertEquals(newQuantity, actualCartItemDto.quantity());
        verify(cartItemRepository).save(validCartItem);
    }
}
