package store.bookstoreapp.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import store.bookstoreapp.dto.order.OrderDto;
import store.bookstoreapp.dto.orderitem.OrderItemDto;
import store.bookstoreapp.exception.EntityNotFoundException;
import store.bookstoreapp.mapper.CartItemOrderItemMapper;
import store.bookstoreapp.mapper.OrderItemMapper;
import store.bookstoreapp.mapper.OrderMapper;
import store.bookstoreapp.model.Order;
import store.bookstoreapp.model.OrderItem;
import store.bookstoreapp.model.ShoppingCart;
import store.bookstoreapp.model.User;
import store.bookstoreapp.repository.order.OrderRepository;
import store.bookstoreapp.repository.orderitem.OrderItemRepository;
import store.bookstoreapp.repository.shoppingcart.ShoppingCartRepository;
import store.bookstoreapp.repository.user.UserRepository;
import store.bookstoreapp.service.OrderService;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final CartItemOrderItemMapper cartItemOrderItemMapper;

    @Override
    public OrderDto placeOrder(String address, String email) {
        User user = getUserByEmail(email);
        ShoppingCart shoppingCart = getShoppingCart(user);
        Order order = createOrder(address, user);
        List<OrderItem> orderItems = shoppingCart.getCartItems().stream()
                .map(cartItemOrderItemMapper::cartItemToOrderMapper)
                .map(item -> {
                    item.setOrder(order);
                    return item;
                })
                .toList();
        order.setOrderItems(new HashSet<>(orderItems));
        order.setTotal(calculateTotal(orderItems));
        orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);
        clearShoppingCart(shoppingCart);
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderDto> getAllOrders(String email, Pageable pageable) {
        return orderRepository.getAllByUserId(getUserByEmail(email).getId(), pageable).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public OrderDto updateOrderStatus(Long id, Order.Status status) {
        Order order = orderRepository.getOrderById(id);
        order.setStatus(status);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderItemDto> getOrderItems(Long id, String email) {
        return getOrderById(id, email).getOrderItems().stream()
                .map(orderItemMapper::toDto).toList();
    }

    @Override
    public OrderItemDto getOrderItem(Long orderId, Long orderItemId, String email) {
        Set<OrderItem> orderItems = getOrderById(orderId, email).getOrderItems();
        return orderItemMapper.toDto(orderItems.stream()
                .filter(oi -> oi.getId().equals(orderItemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find order item with id " + orderItemId)));
    }

    private ShoppingCart getShoppingCart(User user) {
        ShoppingCart shoppingCart = shoppingCartRepository.getShoppingCartById(user.getId());
        if (shoppingCart.getCartItems().isEmpty()) {
            throw new RuntimeException("No goods in shopping cart with id: " + user.getId());
        }
        return shoppingCart;
    }

    private Order createOrder(String address, User user) {
        Order order = new Order();
        order.setUser(user);
        order.setStatus(Order.Status.PENDING);
        order.setShippingAddress(address);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    private BigDecimal calculateTotal(List<OrderItem> orderItems) {
        if (orderItems == null) {
            throw new IllegalArgumentException("orderItems cannot be null");
        }
        return orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void clearShoppingCart(ShoppingCart shoppingCart) {
        shoppingCart.getCartItems().clear();
        shoppingCartRepository.save(shoppingCart);
    }

    private Order getOrderById(Long orderId, String email) {
        Order order = orderRepository.getOrderById(orderId);
        if (!order.getUser().getId().equals(getUserByEmail(email).getId())) {
            throw new EntityNotFoundException("Not found order with id:" + order.getId());
        }
        return order;
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Can't find user with email " + email));
    }
}
