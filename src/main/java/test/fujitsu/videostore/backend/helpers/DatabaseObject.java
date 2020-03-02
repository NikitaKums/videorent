package test.fujitsu.videostore.backend.helpers;

import lombok.Getter;
import lombok.Setter;
import test.fujitsu.videostore.backend.database.DTO.RentOrderDTO;
import test.fujitsu.videostore.backend.domain.Customer;
import test.fujitsu.videostore.backend.domain.Movie;

import java.util.List;

@Getter
@Setter
public class DatabaseObject {
    private List<Movie> movie;
    private List<Customer> customer;
    private List<RentOrderDTO> order;
}
