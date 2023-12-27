package store.bookstoreapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import store.bookstoreapp.dto.user.UserRegistrationRequestDto;
import store.bookstoreapp.dto.user.UserResponseDto;
import store.bookstoreapp.mapper.UserMapper;
import store.bookstoreapp.model.ShoppingCart;
import store.bookstoreapp.model.User;
import store.bookstoreapp.repository.shoppingcart.ShoppingCartRepository;
import store.bookstoreapp.repository.user.UserRepository;
import store.bookstoreapp.service.impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private static final String VALID_EMAIL = "test@email.com";
    private static final String VALID_PASSWORD = "Password";
    private static final String VALID_FIRST_NAME = "First Name";
    private static final String VALID_LAST_NAME = "Last Name";
    private static final String VALID_ADDRESS = "Address";
    private static final Long VALID_ID = 1L;
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    private User createValidUser() {
        User user = new User();
        user.setId(VALID_ID);
        user.setEmail(VALID_EMAIL);
        user.setPassword(VALID_PASSWORD);
        user.setRoles(Collections.emptySet());
        user.setLastName(VALID_LAST_NAME);
        user.setFirstName(VALID_FIRST_NAME);
        user.setShippingAddress(VALID_FIRST_NAME);
        user.setRoles(Collections.emptySet());
        return user;
    }

    private UserRegistrationRequestDto createValiduserRegistrationRequestDto() {
        UserRegistrationRequestDto userRegistrationRequestDto = new UserRegistrationRequestDto();
        userRegistrationRequestDto.setEmail(VALID_EMAIL);
        userRegistrationRequestDto.setPassword(VALID_PASSWORD);
        userRegistrationRequestDto.setRepeatPassword(VALID_PASSWORD);
        userRegistrationRequestDto.setFirstName(VALID_FIRST_NAME);
        userRegistrationRequestDto.setLastName(VALID_LAST_NAME);
        userRegistrationRequestDto.setShippingAddress(VALID_ADDRESS);
        return userRegistrationRequestDto;
    }

    private UserResponseDto createValidUserResponseDto() {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setEmail(VALID_EMAIL);
        userResponseDto.setFirstName(VALID_FIRST_NAME);
        userResponseDto.setLastName(VALID_LAST_NAME);
        userResponseDto.setId(VALID_ID);
        userResponseDto.setShippingAddress(VALID_ADDRESS);
        return userResponseDto;
    }

    @Test
    @DisplayName("Verify registerNewShoppingCart() method works")
    public void registerNewShoppingCart_ValidShoppingCart_CallSave() {
        User user = createValidUser();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);

        userService.registerNewShoppingCart(user);

        verify(shoppingCartRepository).save(shoppingCart);
    }

    @Test
    @DisplayName("Verify register() method works")
    public void register_ValidUserRegistrationRequestDto_ReturnUserResponseDto() {
        UserRegistrationRequestDto requestDto = createValiduserRegistrationRequestDto();
        User user = createValidUser();
        UserResponseDto expected = createValidUserResponseDto();
        when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toModel(requestDto)).thenReturn(user);
        when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("HashedPassword");
        when(userMapper.toDto(user)).thenReturn(expected);

        UserResponseDto actual = userService.register(requestDto);

        assertEquals(expected, actual);
    }
}
