package store.bookstoreapp.repository.cartitem;

import org.springframework.data.jpa.repository.JpaRepository;
import store.bookstoreapp.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
