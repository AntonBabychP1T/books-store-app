package store.bookstoreapp.dto.order;

import store.bookstoreapp.model.Order;

public record OrderStatusDto(Order.Status status) {
}
