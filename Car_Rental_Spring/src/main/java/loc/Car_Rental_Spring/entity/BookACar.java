package loc.Car_Rental_Spring.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import loc.Car_Rental_Spring.dto.BookACarDto;
import loc.Car_Rental_Spring.enums.BookACarStatus;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
public class BookACar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date fromdate;
    private Date todate;
    private Long days;
    private BigDecimal price;
    private BookACarStatus bookACarStatus;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "car_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Car car;

    public BookACarDto getBookACarDto() {
        BookACarDto bookACarDto = new BookACarDto();
        bookACarDto.setId(this.id);
        bookACarDto.setFromdate(this.fromdate);
        bookACarDto.setTodate(this.todate);
        bookACarDto.setDays(this.days);
        bookACarDto.setPrice(this.price);
        bookACarDto.setBookACarStatus(this.bookACarStatus);
        bookACarDto.setCardId(this.car != null ? this.car.getId() : null); // Vérifiez que car n'est pas null
        bookACarDto.setUserId(this.user != null ? this.user.getId() : null); // Vérifiez que user n'est pas null
        return bookACarDto;
    }
}