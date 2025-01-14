package loc.Car_Rental_Spring.controller;

import loc.Car_Rental_Spring.dto.AuthenticationRequest;
import loc.Car_Rental_Spring.dto.AuthenticationResponse;
import loc.Car_Rental_Spring.dto.SignupRequest;
import loc.Car_Rental_Spring.dto.UserDto;
import loc.Car_Rental_Spring.entity.User;
import loc.Car_Rental_Spring.repository.UserRepository;
import loc.Car_Rental_Spring.services.auth.AuthService;
import loc.Car_Rental_Spring.services.jwt.UserService;
import loc.Car_Rental_Spring.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;


@PostMapping("/signup")
    public ResponseEntity<?> signupCustomer(@RequestBody SignupRequest signupRequest) {
        if(authService.hasCustomerWithEmail(signupRequest.getEmail()))
            return new ResponseEntity<>("Email already exists", HttpStatus.NOT_ACCEPTABLE);
        UserDto createdCustomerDto = authService.createCustomer(signupRequest);
        if (createdCustomerDto == null) return new ResponseEntity<>
                  ("Customer not created , come again later", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(createdCustomerDto, HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws
            BadCredentialsException,
            DisabledException,
            UsernameNotFoundException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect username or password.");
        }
        final UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getEmail());
        Optional<User> optionalUser = userRepository.findFirstByEmail(userDetails.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        if (optionalUser.isPresent()) {
            authenticationResponse.setJwt(jwt);
            authenticationResponse.setUserId(optionalUser.get().getId());
            authenticationResponse.setUserRole(optionalUser.get().getUserRole());
        }
        return authenticationResponse;
    }
}
