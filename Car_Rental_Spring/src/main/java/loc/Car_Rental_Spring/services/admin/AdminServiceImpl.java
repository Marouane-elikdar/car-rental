package loc.Car_Rental_Spring.services.admin;

import loc.Car_Rental_Spring.dto.BookACarDto;
import loc.Car_Rental_Spring.dto.CarDto;
import loc.Car_Rental_Spring.dto.CarDtoListDto;
import loc.Car_Rental_Spring.dto.SearchCarDto;
import loc.Car_Rental_Spring.entity.BookACar;
import loc.Car_Rental_Spring.entity.Car;
import loc.Car_Rental_Spring.enums.BookACarStatus;
import loc.Car_Rental_Spring.repository.BookACarRepository;
import loc.Car_Rental_Spring.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {
    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);
    private final CarRepository carRepository;
    private final BookACarRepository bookACarRepository;

    @Override
    public boolean postCar(CarDto carDto) throws IOException {
        try {
            Car car = new Car();
            car.setBrand(carDto.getBrand());
            car.setColor(carDto.getColor());
            car.setName(carDto.getName());
            car.setPrice(new BigDecimal(carDto.getPrice())); // Conversion de String à BigDecimal
            car.setTransmission(carDto.getTransmission());
            car.setYear(carDto.getYear());
            car.setType(carDto.getType());
            car.setDescription(carDto.getDescription());
            car.setImage(carDto.getImage().getBytes()); // Convertir l'image en bytes
            carRepository.save(car);
            return true;
        } catch (Exception e) {
            logger.error("Error while posting car", e); // Log l'erreur
            return false;
        }
    }

    @Override
    public List<CarDto> getAllCars() {
        return carRepository.findAll().stream()
                .map(Car::getCardto) // Utilisez la méthode getCardto de Car
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    @Override
    public CarDto getCarById(Long id) {
        Optional<Car> optionalCar = carRepository.findById(id);
        return optionalCar.map(Car::getCardto).orElse(null); // Retourne null si la voiture n'existe pas
    }

    @Override
    public boolean updateCar(Long carId, CarDto carDto) throws IOException {
        Optional<Car> optionalCar = carRepository.findById(carId);
        if (optionalCar.isPresent()) {
            Car existingCar = optionalCar.get();
            if (carDto.getImage() != null) {
                existingCar.setImage(carDto.getImage().getBytes()); // Mettre à jour l'image si elle est fournie
            }
            existingCar.setName(carDto.getName());
            existingCar.setPrice(new BigDecimal(carDto.getPrice())); // Conversion de String à BigDecimal
            existingCar.setYear(carDto.getYear());
            existingCar.setType(carDto.getType());
            existingCar.setDescription(carDto.getDescription());
            existingCar.setTransmission(carDto.getTransmission());
            existingCar.setColor(carDto.getColor());
            existingCar.setBrand(carDto.getBrand());
            carRepository.save(existingCar);
            return true;
        } else {
            return false; // Retourne false si la voiture n'existe pas
        }
    }

    @Override
    public List<BookACarDto> getBookings() {
        return bookACarRepository.findAll().stream()
                .map(BookACar::getBookACarDto) // Utilisez la méthode getBookACarDto de BookACar
                .collect(Collectors.toList());
    }

    @Override
    public boolean changeBookingStatus(Long bookingId, String status) {
        Optional<BookACar> optionalBookACar = bookACarRepository.findById(bookingId);
        if (optionalBookACar.isPresent()) {
            BookACar existingBookACar = optionalBookACar.get();
            if (Objects.equals(status, "Approved")) {
                existingBookACar.setBookACarStatus(BookACarStatus.APPROVED);
            } else {
                existingBookACar.setBookACarStatus(BookACarStatus.REJECTED);
            }
            bookACarRepository.save(existingBookACar);
            return true;
        }
        return false; // Retourne false si la réservation n'existe pas
    }

    @Override
    public CarDtoListDto searchCar(SearchCarDto searchCarDto) {
        // Créez un objet Car avec les critères de recherche
        Car car = new Car();
        if (searchCarDto.getBrand() != null) car.setBrand(searchCarDto.getBrand());
        if (searchCarDto.getType() != null) car.setType(searchCarDto.getType());
        if (searchCarDto.getTransmission() != null) car.setTransmission(searchCarDto.getTransmission());
        if (searchCarDto.getColor() != null) car.setColor(searchCarDto.getColor());

        // Configurez ExampleMatcher pour une recherche flexible
        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll()
                .withMatcher("brand", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("type", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("transmission", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("color", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        Example<Car> carExample = Example.of(car, exampleMatcher);
        List<Car> carList = carRepository.findAll(carExample);
        CarDtoListDto carDtoListDto = new CarDtoListDto();
        carDtoListDto.setCarDtoList(carList.stream()
                .map(Car::getCardto)
                .collect(Collectors.toList()));

        return carDtoListDto;
    }
}