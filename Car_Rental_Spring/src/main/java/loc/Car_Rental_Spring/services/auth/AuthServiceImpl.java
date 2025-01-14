package loc.Car_Rental_Spring.services.auth;

import jakarta.annotation.PostConstruct;
import loc.Car_Rental_Spring.dto.SignupRequest;
import loc.Car_Rental_Spring.dto.UserDto;
import loc.Car_Rental_Spring.entity.User;
import loc.Car_Rental_Spring.enums.UserRole;
import loc.Car_Rental_Spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

    @PostConstruct

    public void createAdminAccount(){
        User adminAccount = userRepository.findByUserRole(UserRole.ADMIN);
        if (adminAccount == null){
            User newAdminAccount = new User();
            newAdminAccount.setName("Admin");
            newAdminAccount.setEmail("admin@admin.com");
            newAdminAccount.setPassword(new BCryptPasswordEncoder().encode("admin"));
            newAdminAccount.setUserRole(UserRole.ADMIN);
            userRepository.save(newAdminAccount);
            System.out.println("Admin account created");
        }
    }

    @Override
    public UserDto createCustomer(SignupRequest signupRequest) {
        User user = new User();
        user.setName(signupRequest.getName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        user.setUserRole(UserRole.CUSTOMER);
        User createdUser = userRepository.save(user);
        UserDto userDto = new UserDto();
        userDto.setId(createdUser.getId());

    return userDto;
    }

    @Override
    public boolean hasCustomerWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }
}
