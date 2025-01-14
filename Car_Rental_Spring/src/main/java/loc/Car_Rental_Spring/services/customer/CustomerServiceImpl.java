package loc.Car_Rental_Spring.services.customer;

import loc.Car_Rental_Spring.dto.BookACarDto;
import loc.Car_Rental_Spring.dto.CarDto;
import loc.Car_Rental_Spring.dto.CarDtoListDto;
import loc.Car_Rental_Spring.dto.SearchCarDto;
import loc.Car_Rental_Spring.entity.BookACar;
import loc.Car_Rental_Spring.entity.Car;
import loc.Car_Rental_Spring.entity.User;
import loc.Car_Rental_Spring.enums.BookACarStatus;
import loc.Car_Rental_Spring.repository.BookACarRepository;
import loc.Car_Rental_Spring.repository.CarRepository;
import loc.Car_Rental_Spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final BookACarRepository bookACarRepository;

    @Override
    public List<CarDto> getAllCars() {
        return carRepository.findAll().stream().map(Car::getCardto).collect(Collectors.toList());
    }

    @Override
    public boolean bookACar(BookACarDto bookACarDto) {
        Optional<Car> optionalCar = carRepository.findById(bookACarDto.getCardId());
        Optional<User> optionalUser = userRepository.findById(bookACarDto.getUserId());
        if (optionalCar.isPresent() && optionalUser.isPresent()) {
            Car existingCar = optionalCar.get();
            BookACar bookACar = new BookACar();
            bookACar.setCar(existingCar);
            bookACar.setUser(optionalUser.get());
            bookACar.setBookACarStatus(BookACarStatus.PENDING);

            // Calcul de la différence en jours
            long diffInMilliSeconds = bookACarDto.getTodate().getTime() - bookACarDto.getFromdate().getTime();
            long days = TimeUnit.MILLISECONDS.toDays(diffInMilliSeconds);
            bookACar.setDays(days);

            // Calcul du prix total avec BigDecimal
            BigDecimal pricePerDay = existingCar.getPrice(); // Utilisation directe de BigDecimal
            BigDecimal totalPrice = pricePerDay.multiply(BigDecimal.valueOf(days));
            bookACar.setPrice(totalPrice);

            // Enregistrer la réservation
            bookACarRepository.save(bookACar);
            return true;
        }
        return false;
    }

    @Override
    public CarDto getCarById(Long carId) {
        Optional<Car> optionalCar = carRepository.findById(carId);
        return optionalCar.map(Car::getCardto).orElse(null);
    }

    @Override
    public List<BookACarDto> getBookingsByUserId(Long userId) {
        return bookACarRepository.findAllByUserId(userId)
                .stream()
                .map(BookACar::getBookACarDto) // Utilisation de la méthode getBookACarDto
                .collect(Collectors.toList());
    }

    @Override
    public CarDtoListDto searchCar(SearchCarDto searchCarDto) {
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