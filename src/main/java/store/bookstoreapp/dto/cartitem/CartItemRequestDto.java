package store.bookstoreapp.dto.cartitem;

import jakarta.validation.constraints.NotNull;

public record CartItemRequestDto(
        @NotNull
        Long bookId,
        @NotNull
        Integer quantity) {
}
