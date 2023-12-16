package store.bookstoreapp.dto.shoppingcart;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import store.bookstoreapp.dto.cartitem.CartItemDto;

@Getter
@Setter
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private List<CartItemDto> cartItems;
}
