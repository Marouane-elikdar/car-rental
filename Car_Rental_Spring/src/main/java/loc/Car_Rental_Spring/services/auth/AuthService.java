package loc.Car_Rental_Spring.services.auth;

import loc.Car_Rental_Spring.dto.SignupRequest;
import loc.Car_Rental_Spring.dto.UserDto;

public interface AuthService {

    UserDto createCustomer(SignupRequest signupRequest);

    boolean hasCustomerWithEmail(String email);
}
