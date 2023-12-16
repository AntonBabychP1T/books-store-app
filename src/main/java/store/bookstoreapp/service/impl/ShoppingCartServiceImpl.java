package store.bookstoreapp.service.impl;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.bookstoreapp.dto.cartitem.CartItemDto;
import store.bookstoreapp.dto.cartitem.CartItemRequestDto;
import store.bookstoreapp.dto.shoppingcart.ShoppingCartDto;
import store.bookstoreapp.exception.EntityNotFoundException;
import store.bookstoreapp.mapper.BookMapper;
import store.bookstoreapp.mapper.CartItemMapper;
import store.bookstoreapp.mapper.ShoppingCartMapper;
import store.bookstoreapp.model.CartItem;
import store.bookstoreapp.model.ShoppingCart;
import store.bookstoreapp.repository.cartitem.CartItemRepository;
import store.bookstoreapp.repository.shoppingcart.ShoppingCartRepository;
import store.bookstoreapp.repository.user.UserRepository;
import store.bookstoreapp.service.BookService;
import store.bookstoreapp.service.ShoppingCartService;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final BookService bookService;
    private final UserRepository userRepository;
    private final BookMapper bookMapper;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    @Override
    public ShoppingCartDto addBookToShoppingCart(CartItemRequestDto requestDto, String email) {
        ShoppingCart shoppingCart = getShoppingCartByEmail(email);
        Optional<CartItem> cartItemInShoppingCart =
                findCartItemInShoppingCart(shoppingCart, requestDto.bookId());
        CartItem cartItem;
        if (cartItemInShoppingCart.isPresent()) {
            cartItem = cartItemInShoppingCart.get();
            cartItem.setQuantity(cartItem.getQuantity() + requestDto.quantity());
        } else {
            cartItem = createCartItem(requestDto, shoppingCart);
        }
        cartItemRepository.save(cartItem);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto getShoppingCart(String email) {
        ShoppingCart shoppingCart = getShoppingCartByEmail(email);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public void deleteItemFromShoppingCart(Long id, String email) {
        cartItemRepository.deleteById(id);
    }

    @Override
    public CartItemDto updateQuantity(Long id, int quantity, String email) {
        ShoppingCart shoppingCart = getShoppingCartByEmail(email);
        CartItem cartItem = shoppingCart.getCartItems().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find cart item with id: " + id)
                );
        cartItem.setQuantity(quantity);
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    private ShoppingCart getShoppingCartByEmail(String email) {
        return shoppingCartRepository
                .getShoppingCartById(userRepository.findByEmail(email).orElseThrow(
                                () -> new EntityNotFoundException(
                                        "Can't find shopping cart to email" + email))
                        .getId());
    }

    private CartItem createCartItem(CartItemRequestDto requestDto, ShoppingCart shoppingCart) {
        CartItem cartItem = new CartItem();
        cartItem.setBook(bookMapper.toModel(bookService.findBookById(requestDto.bookId())));
        cartItem.setQuantity(requestDto.quantity());
        cartItem.setShoppingCart(shoppingCart);
        shoppingCart.getCartItems().add(cartItem);
        return cartItem;
    }

    private Optional<CartItem> findCartItemInShoppingCart(ShoppingCart shoppingCart, Long id) {
        return shoppingCart.getCartItems().stream()
                .filter(cartItem -> cartItem.getBook().getId().equals(id))
                .findFirst();

    }
}
