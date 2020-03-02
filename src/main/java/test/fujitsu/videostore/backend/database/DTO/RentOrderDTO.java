package test.fujitsu.videostore.backend.database.DTO;

import lombok.Data;
import test.fujitsu.videostore.backend.domain.MovieType;

import java.time.LocalDate;
import java.util.List;

@Data
public class RentOrderDTO {

    private int id = -1;

    private int customer;

    private LocalDate orderDate = LocalDate.now();

    private List<ItemDTO> items;

    @Data
    public static class ItemDTO {

        private int movie;

        private MovieType type;

        private int days;

        private boolean paidByBonus;

        private LocalDate returnedDay;
    }
}
