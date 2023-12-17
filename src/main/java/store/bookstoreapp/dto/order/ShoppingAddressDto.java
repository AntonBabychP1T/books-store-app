package store.bookstoreapp.dto.order;

import jakarta.validation.constraints.NotNull;

public record ShoppingAddressDto(@NotNull String shippingAddress) {
}
