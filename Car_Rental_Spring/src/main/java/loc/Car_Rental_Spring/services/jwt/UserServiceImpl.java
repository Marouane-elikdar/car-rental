package loc.Car_Rental_Spring.services.jwt;

import loc.Car_Rental_Spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService { // Implémente UserService
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findFirstByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}