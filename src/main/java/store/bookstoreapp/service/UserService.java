package store.bookstoreapp.service;

import store.bookstoreapp.dto.user.UserRegistrationRequestDto;
import store.bookstoreapp.dto.user.UserResponseDto;
import store.bookstoreapp.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;
}
