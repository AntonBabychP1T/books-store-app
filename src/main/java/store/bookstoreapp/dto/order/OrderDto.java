package store.bookstoreapp.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import store.bookstoreapp.dto.orderitem.OrderItemDto;
import store.bookstoreapp.model.Order;

@Getter
@Setter
public class OrderDto {
    private Long id;
    private Long userId;
    private List<OrderItemDto> orderItems;
    private LocalDateTime orderDate;
    private BigDecimal total;
    private Order.Status status;
}
