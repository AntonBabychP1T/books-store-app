package store.bookstoreapp.mapper;

import org.mapstruct.Mapper;
import store.bookstoreapp.config.MapperConfig;
import store.bookstoreapp.dto.user.UserRegistrationRequestDto;
import store.bookstoreapp.dto.user.UserResponseDto;
import store.bookstoreapp.model.User;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toModel(UserRegistrationRequestDto requestDto);

    UserResponseDto toDto(User user);
}
