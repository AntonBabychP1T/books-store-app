package store.bookstoreapp.security;

import store.bookstoreapp.dto.user.UserLoginRequestDto;
import store.bookstoreapp.dto.user.UserLoginResponseDto;

public interface AuthenticationService {
    UserLoginResponseDto authentication(UserLoginRequestDto requestDto);
}
