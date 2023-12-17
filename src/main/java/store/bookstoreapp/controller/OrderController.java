package store.bookstoreapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.bookstoreapp.dto.order.OrderDto;
import store.bookstoreapp.dto.order.OrderStatusDto;
import store.bookstoreapp.dto.order.ShoppingAddressDto;
import store.bookstoreapp.dto.orderitem.OrderItemDto;
import store.bookstoreapp.service.OrderService;

@Tag(name = "Order management", description = "Endpoints for managing orders")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Make order",
            description = "Move goods from shopping cart to order")
    @Parameter(name = "shippingAddress", description = "Address for shipping")
    public OrderDto placeOrder(
            @RequestBody @Valid ShoppingAddressDto addressDto,
            Authentication authentication
    ) {
        return orderService.placeOrder(addressDto.shippingAddress(), authentication.getName());
    }

    @GetMapping
    @Operation(summary = "get all orders for some user",
            description = "Get list of the orders for specific user")
    public List<OrderDto> getAllOrders(Authentication authentication, Pageable pageable) {
        return orderService.getAllOrders(authentication.getName(), pageable);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping(value = "/{id}")
    @Operation(summary = "Update status for order",
            description = "Set new status for specific order")
    @Parameter(name = "id", description = "Id of order")
    @Parameter(name = "status",
            description = "One of the existing status:"
                    + " 'COMPLETED', 'PENDING', 'DELIVERED', 'PROCESSING'")
    public OrderDto updateOrderStatus(
            @PathVariable Long id,
            @RequestBody OrderStatusDto statusDto
    ) {
        return orderService.updateOrderStatus(id, statusDto.status());
    }

    @GetMapping(value = "/{orderId}/items")
    @Operation(summary = "get all items in some order",
            description = "get list of order items")
    public List<OrderItemDto> getOrderItems(@PathVariable Long orderId,
                                            Authentication authentication) {
        return orderService.getOrderItems(orderId, authentication.getName());
    }

    @GetMapping(value = "/{orderId}/items/{orderItemId}")
    @Operation(summary = "get specific item in some order",
            description = "get one items for id in order")
    public OrderItemDto getOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long orderItemId,
            Authentication authentication
    ) {
        return orderService.getOrderItem(orderId, orderItemId, authentication.getName());
    }
}
