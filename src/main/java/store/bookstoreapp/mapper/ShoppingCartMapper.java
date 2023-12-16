package store.bookstoreapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import store.bookstoreapp.config.MapperConfig;
import store.bookstoreapp.dto.shoppingcart.ShoppingCartDto;
import store.bookstoreapp.model.ShoppingCart;

@Mapper(config = MapperConfig.class, uses = {CartItemMapper.class})
public interface ShoppingCartMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "cartItems", target = "cartItems")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    @Mapping(target = "user", ignore = true)
    ShoppingCart toModel(ShoppingCartDto shoppingCartDto);

}
