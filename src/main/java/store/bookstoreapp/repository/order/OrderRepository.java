package store.bookstoreapp.repository.order;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import store.bookstoreapp.model.Order;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> getAllByUserId(Long id, Pageable pageable);

    Order getOrderById(Long id);
}
