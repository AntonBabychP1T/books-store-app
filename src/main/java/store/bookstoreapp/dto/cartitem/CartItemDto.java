package store.bookstoreapp.dto.cartitem;

public record CartItemDto(
        Long id,
        Long bookId,
        String bookTitle,
        Integer quantity
) {
}
