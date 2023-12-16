package store.bookstoreapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import store.bookstoreapp.dto.cartitem.CartItemDto;
import store.bookstoreapp.dto.cartitem.CartItemRequestDto;
import store.bookstoreapp.dto.cartitem.QuantityDto;
import store.bookstoreapp.dto.shoppingcart.ShoppingCartDto;
import store.bookstoreapp.service.ShoppingCartService;

@Tag(name = "Shopping cart management", description = "Endpoints for managing shopping cart")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PostMapping
    @Operation(summary = "Add book to shopping cart",
            description = "Create new cart item with book and quantity")
    @Parameter(name = "bookId", description = "id of the book",
            required = true, example = "1")
    @Parameter(name = "quantity", description = "quantity of this book",
            required = true, example = "5")
    public ShoppingCartDto addBookToShoppingCart(
            @RequestBody @Valid CartItemRequestDto requestDto,
            Authentication authentication) {
        return shoppingCartService.addBookToShoppingCart(requestDto, authentication.getName());
    }

    @GetMapping
    @Operation(summary = "Get user shopping cart",
            description = "Get shopping cart id with cart items inside")
    public ShoppingCartDto getShoppingCart(Authentication authentication) {
        return shoppingCartService.getShoppingCart(authentication.getName());
    }

    @DeleteMapping("/cart-items/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete cart item", description = "Delete cart item from shopping cart")
    public void deleteItemFromShoppingCart(@PathVariable Long id, Authentication authentication) {
        shoppingCartService.deleteItemFromShoppingCart(id, authentication.getName());
    }

    @PutMapping("/cart-items/{id}")
    @Operation(summary = "Update quantity to some cart item",
            description = "Update quantity of book in specific cart item")
    public CartItemDto updateQuantity(
            @PathVariable Long id,
            @RequestBody @Valid QuantityDto quantity,
            Authentication authentication
    ) {
        return shoppingCartService.updateQuantity(
                id,
                quantity.quantity(),
                authentication.getName()
        );
    }
}
