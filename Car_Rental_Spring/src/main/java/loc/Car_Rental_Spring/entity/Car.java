package loc.Car_Rental_Spring.entity;

import jakarta.persistence.*;
import loc.Car_Rental_Spring.dto.CarDto;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String brand;
    private String color;
    private String name;
    private String type;
    private String transmission;
    private String description;
    private BigDecimal price; // Changement ici
    private String year;
    @Column(columnDefinition = "longblob")
    private byte[] image;

    public CarDto getCardto() {
        CarDto carDto = new CarDto();
        carDto.setId(id);
        carDto.setBrand(brand);
        carDto.setColor(color);
        carDto.setName(name);
        carDto.setType(type);
        carDto.setTransmission(transmission);
        carDto.setDescription(description);
        carDto.setPrice(price.toString()); // Convertir BigDecimal en String pour le DTO
        carDto.setYear(year);
        carDto.setReturnedImage(image);
        return carDto;
    }
}