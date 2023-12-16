package store.bookstoreapp.service;

import store.bookstoreapp.dto.cartitem.CartItemDto;
import store.bookstoreapp.dto.cartitem.CartItemRequestDto;
import store.bookstoreapp.dto.shoppingcart.ShoppingCartDto;

public interface ShoppingCartService {
    ShoppingCartDto addBookToShoppingCart(CartItemRequestDto requestDto, String email);

    ShoppingCartDto getShoppingCart(String email);

    void deleteItemFromShoppingCart(Long id, String email);

    CartItemDto updateQuantity(Long id, int quantity, String email);
}
