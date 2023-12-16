package store.bookstoreapp.service;

import store.bookstoreapp.dto.user.UserRegistrationRequestDto;
import store.bookstoreapp.dto.user.UserResponseDto;
import store.bookstoreapp.model.User;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto);

    void registerNewShoppingCart(User user);
}
