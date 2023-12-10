package store.bookstoreapp.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import store.bookstoreapp.validation.FieldMatch;

@Getter
@Setter
@FieldMatch(
        firstField = "password",
        secondField = "repeatPassword",
        message = "The password fields must match"
)
public class UserRegistrationRequestDto {
    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email format")
    private String email;
    @NotNull
    @Size(min = 8,max = 24, message = "Password must be between 8 and 24 characters")
    private String password;
    @NotNull
    @Size(min = 8, max = 24, message = "Repeat Password must be between 8 and 24 characters")
    private String repeatPassword;
    @NotNull(message = "First name cannot be null")
    private String firstName;
    @NotNull(message = "Last name cannot be null")
    private String lastName;
    private String shippingAddress;
}
