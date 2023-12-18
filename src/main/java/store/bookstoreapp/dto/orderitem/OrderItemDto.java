package store.bookstoreapp.dto.orderitem;

public record OrderItemDto(
        Long id,
        Long bookId,
        Integer quantity
) {
}
