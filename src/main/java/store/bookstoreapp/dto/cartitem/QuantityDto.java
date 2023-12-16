package store.bookstoreapp.dto.cartitem;

import jakarta.validation.constraints.NotNull;

public record QuantityDto(@NotNull Integer quantity) {
}
