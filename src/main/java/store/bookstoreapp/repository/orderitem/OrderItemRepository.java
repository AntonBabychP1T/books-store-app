package store.bookstoreapp.repository.orderitem;

import org.springframework.data.jpa.repository.JpaRepository;
import store.bookstoreapp.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
