package store.bookstoreapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.bookstoreapp.dto.user.UserRegistrationRequestDto;
import store.bookstoreapp.dto.user.UserResponseDto;
import store.bookstoreapp.exception.RegistrationException;
import store.bookstoreapp.service.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/auth")
public class AuthenticationController {
    private final UserService userService;

    @PostMapping
    public UserResponseDto register(
            @RequestBody @Valid UserRegistrationRequestDto requestDto
    ) throws RegistrationException {
        return userService.register(requestDto);
    }
}
