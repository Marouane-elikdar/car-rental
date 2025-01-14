package loc.Car_Rental_Spring.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CarDto {
    private long id;
    private String brand;
    private String color;
    private String name;
    private String type;
    private String transmission;
    private String description;
    private String price;
    private String year;
    private MultipartFile image;
    private byte[] returnedImage;
}
