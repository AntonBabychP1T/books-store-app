package store.bookstoreapp.repository.role;

import org.springframework.data.jpa.repository.JpaRepository;
import store.bookstoreapp.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
