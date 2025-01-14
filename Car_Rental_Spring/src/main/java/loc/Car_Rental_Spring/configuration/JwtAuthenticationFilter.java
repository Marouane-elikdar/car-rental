package loc.Car_Rental_Spring.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import loc.Car_Rental_Spring.services.jwt.UserService;

import loc.Car_Rental_Spring.utils.JWTUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final UserService userService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extrait le JWT de l'en-tête Authorization
        jwt = authHeader.substring(7);
        userEmail = jwtUtil.extractUserName(jwt);

        // Vérifiez si l'email est valide et si l'utilisateur n'est pas déjà authentifié
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Charge les détails de l'utilisateur à partir de la base de données
            UserDetails userDetails = userService.loadUserByUsername(userEmail);

            // Vérifiez si le token est valide
            if (jwtUtil.isTokenValid(jwt, userDetails)) {
                // Crée un contexte de sécurité et définit l'authentification
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }
        }

        // Passe la requête au filtre suivant dans la chaîne
        filterChain.doFilter(request, response);
    }
}