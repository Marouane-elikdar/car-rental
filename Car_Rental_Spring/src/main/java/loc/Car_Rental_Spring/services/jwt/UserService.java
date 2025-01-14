package loc.Car_Rental_Spring.services.jwt;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    // Pas besoin de définir userDetailsService() ici
    // La méthode loadUserByUsername est héritée de UserDetailsService
}