package loc.Car_Rental_Spring.dto;

import loc.Car_Rental_Spring.enums.BookACarStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BookACarDto {
    private Long id;
    private Date fromdate;
    private Date todate;
    private Long days;
    private BigDecimal price;
    private BookACarStatus bookACarStatus;
    private Long cardId;
    private Long userId;
}