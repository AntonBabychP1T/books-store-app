package store.bookstoreapp.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import store.bookstoreapp.dto.order.OrderDto;
import store.bookstoreapp.dto.orderitem.OrderItemDto;
import store.bookstoreapp.model.Order;

public interface OrderService {
    OrderDto placeOrder(String address, String email);

    List<OrderDto> getAllOrders(String email, Pageable pageable);

    OrderDto updateOrderStatus(Long id, Order.Status status);

    List<OrderItemDto> getOrderItems(Long id, String email);

    OrderItemDto getOrderItem(Long orderId, Long orderItemId, String email);
}
