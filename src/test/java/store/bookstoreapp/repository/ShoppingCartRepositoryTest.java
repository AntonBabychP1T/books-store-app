package store.bookstoreapp.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import store.bookstoreapp.model.ShoppingCart;
import store.bookstoreapp.model.User;
import store.bookstoreapp.repository.shoppingcart.ShoppingCartRepository;

@DataJpaTest
@Sql(
        scripts = {"classpath:database/users/delete-all-users.sql",
                "classpath:database/users/create-default-user-and-shoppingcart.sql",
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@Sql(
        scripts = "classpath:database/users/delete-all-users.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShoppingCartRepositoryTest {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    @DisplayName("Verify getShoppingCartById() method works")
    public void getShoppingCartById_ValidCartInDb_ReturnCart() {
        User user = new User();
        user.setId(1L);
        user.setPassword("Password");
        user.setEmail("test@email.com");
        user.setFirstName("Default");
        user.setLastName("User");
        user.setRoles(Collections.emptySet());
        user.setShippingAddress("DefaultAddress'");
        ShoppingCart expected = new ShoppingCart();
        expected.setCartItems(Collections.emptySet());
        expected.setUser(user);
        expected.setId(1L);

        ShoppingCart actual = shoppingCartRepository.getShoppingCartById(1L);

        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }
}
