package loc.Car_Rental_Spring.repository;

import loc.Car_Rental_Spring.entity.User;
import loc.Car_Rental_Spring.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findFirstByEmail(String email);

    User findByUserRole(UserRole userRole);
}
