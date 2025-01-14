package loc.Car_Rental_Spring.controller;

import loc.Car_Rental_Spring.dto.BookACarDto;
import loc.Car_Rental_Spring.dto.CarDto;
import loc.Car_Rental_Spring.dto.SearchCarDto;
import loc.Car_Rental_Spring.entity.Car;
import loc.Car_Rental_Spring.services.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;
@PostMapping("/car")
    public ResponseEntity<?> postCar(@ModelAttribute CarDto carDto) throws IOException {
        boolean success =adminService.postCar(carDto);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @GetMapping("/cars")
    public ResponseEntity<?> getAllCars() {
    return ResponseEntity.ok(adminService.getAllCars());
    }
    @DeleteMapping("/car/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable long id)  {
    adminService.deleteCar(id);
    return ResponseEntity.ok(null);
    }
    @GetMapping("/cars/¨{id}")
    public ResponseEntity<CarDto> getCarById(@PathVariable long id) {
    CarDto carDto = adminService.getCarById(id);
    return ResponseEntity.ok(carDto);
    }

    @PutMapping("/cars/¨{carId}")
    public ResponseEntity<Void> updateCar(@PathVariable Long carId,@ModelAttribute CarDto carDto) throws IOException {
      try {
          boolean success = adminService.updateCar(carId, carDto);
          if (success) return ResponseEntity.status(HttpStatus.OK).build();
          return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
      } catch (Exception e) {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
      }
    }


    @GetMapping("/car/bookings")
    public ResponseEntity<List<BookACarDto>> getBookings(){
    return ResponseEntity.ok(adminService.getBookings());
    }



    @GetMapping("/car/booking/{bookingId}/{status}")
    public ResponseEntity<?> changeBookings(@PathVariable long bookingId, @PathVariable String status) {
               boolean success=adminService.changeBookingStatus(bookingId,status);
               if (success) return ResponseEntity.ok().build();
               return ResponseEntity.notFound().build();
    }
    @PostMapping("/car/search")
    public ResponseEntity<?> searchCar(@RequestBody SearchCarDto searchCarDto) {
    return ResponseEntity.ok(adminService.searchCar(searchCarDto));
    }



}
