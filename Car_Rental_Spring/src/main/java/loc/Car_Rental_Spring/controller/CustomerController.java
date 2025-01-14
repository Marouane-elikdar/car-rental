package loc.Car_Rental_Spring.controller;

import loc.Car_Rental_Spring.dto.BookACarDto;
import loc.Car_Rental_Spring.dto.CarDto;
import loc.Car_Rental_Spring.dto.SearchCarDto;
import loc.Car_Rental_Spring.services.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor

public class CustomerController {
    private final CustomerService customerService;



    @GetMapping("/cars")
    public ResponseEntity<List<CarDto>> getAllCars() {
        List<CarDto> carDtoList = customerService.getAllCars();
        return ResponseEntity.ok(carDtoList);
    }
    @PostMapping("/car/book/{carId}")
    public ResponseEntity<Void> bookACar(@RequestBody BookACarDto bookAcarDto) {
        boolean success = customerService.bookACar(bookAcarDto);
        if (success) return ResponseEntity.status(HttpStatus.CREATED).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
@GetMapping("/car/{carId}")
    public ResponseEntity<CarDto> getCarById(@PathVariable Long carId) {
        CarDto carDto = customerService.getCarById(carId);
        if (carDto == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(carDto);
    }
@GetMapping("/car/bookings/{userId}")
    public ResponseEntity<List<BookACarDto>> getBookings(@PathVariable Long userId) {
        return ResponseEntity.ok(customerService.getBookingsByUserId(userId));
    }

    @PostMapping("/car/search")
    public ResponseEntity<?> searchCar(@RequestBody SearchCarDto searchCarDto) {
        return ResponseEntity.ok(customerService.searchCar(searchCarDto));
    }






}
