package store.bookstoreapp.mapper;

import java.math.BigDecimal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import store.bookstoreapp.config.MapperConfig;
import store.bookstoreapp.model.CartItem;
import store.bookstoreapp.model.OrderItem;

@Mapper(config = MapperConfig.class)
public interface CartItemOrderItemMapper {

    @Mapping(source = "cartItem.book", target = "book")
    @Mapping(source = "cartItem.quantity", target = "quantity")
    @Mapping(target = "price", expression = "java(calculatePrice(cartItem))")
    OrderItem cartItemToOrderMapper(CartItem cartItem);

    default BigDecimal calculatePrice(CartItem cartItem) {
        return cartItem.getBook().getPrice().multiply(new BigDecimal(cartItem.getQuantity()));
    }
}
