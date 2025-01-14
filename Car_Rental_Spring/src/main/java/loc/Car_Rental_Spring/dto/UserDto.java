package loc.Car_Rental_Spring.dto;

import loc.Car_Rental_Spring.enums.UserRole;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private UserRole userRole;
}
