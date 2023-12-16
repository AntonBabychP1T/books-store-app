package store.bookstoreapp.repository.shoppingcart;

import org.springframework.data.jpa.repository.JpaRepository;
import store.bookstoreapp.model.ShoppingCart;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    ShoppingCart getShoppingCartById(Long id);
}
