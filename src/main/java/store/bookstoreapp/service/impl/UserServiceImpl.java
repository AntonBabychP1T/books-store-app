package store.bookstoreapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.bookstoreapp.dto.user.UserRegistrationRequestDto;
import store.bookstoreapp.dto.user.UserResponseDto;
import store.bookstoreapp.exception.RegistrationException;
import store.bookstoreapp.mapper.UserMapper;
import store.bookstoreapp.repository.user.UserRepository;
import store.bookstoreapp.service.UserService;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("This email already registered");
        }
        return userMapper.toDto(userRepository.save(userMapper.toModel(requestDto)));
    }
}
