package store.bookstoreapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import store.bookstoreapp.config.MapperConfig;
import store.bookstoreapp.dto.cartitem.CartItemDto;
import store.bookstoreapp.model.CartItem;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    CartItemDto toDto(CartItem cartItem);
}
