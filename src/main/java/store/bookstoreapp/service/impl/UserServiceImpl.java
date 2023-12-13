package store.bookstoreapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import store.bookstoreapp.dto.user.UserRegistrationRequestDto;
import store.bookstoreapp.dto.user.UserResponseDto;
import store.bookstoreapp.exception.RegistrationException;
import store.bookstoreapp.mapper.UserMapper;
import store.bookstoreapp.model.User;
import store.bookstoreapp.repository.user.UserRepository;
import store.bookstoreapp.service.UserService;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("This email already registered");
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        return userMapper.toDto(userRepository.save(user));
    }
}
