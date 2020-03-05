package test.fujitsu.videostore.backend.database.tables.rentorders;

import lombok.Setter;
import test.fujitsu.videostore.backend.database.DTO.RentOrderDTO;
import test.fujitsu.videostore.backend.domain.Customer;
import test.fujitsu.videostore.backend.domain.Movie;
import test.fujitsu.videostore.backend.domain.RentOrder;

import java.util.ArrayList;
import java.util.List;

@Setter
public class RentOrderMapper {

    private List<Movie> movies;
    private List<Customer> customers;

    // from DTO to rentOrder
    public RentOrder mapToRentOrder(RentOrderDTO rentOrderDTO){

        RentOrder rentOrder = new RentOrder();
        rentOrder.setId(rentOrderDTO.getId());
        rentOrder.setOrderDate(rentOrderDTO.getOrderDate());

        List<RentOrder.Item> items = new ArrayList<>();
        if (rentOrderDTO.getItems() != null){
            for (RentOrderDTO.ItemDTO itemDTO : rentOrderDTO.getItems()) {
                RentOrder.Item item = new RentOrder.Item();
                item.setDays(itemDTO.getDays());
                item.setMovieType(itemDTO.getType());
                item.setPaidByBonus(itemDTO.isPaidByBonus());
                item.setReturnedDay(itemDTO.getReturnedDay());
                item.setMovie((movies.stream().filter(movie -> movie.getId() == itemDTO.getMovie()).findFirst()).orElse(null));
                items.add(item);
            }
        }

        rentOrder.setItems(items);
        rentOrder.setCustomer((customers.stream().filter(customer -> customer.getId() == rentOrderDTO.getCustomer()).findFirst()).orElse(null));

        return rentOrder;
    }

    // from rentOrder to DTO
    public RentOrderDTO mapFromRentOrder(RentOrder rentOrder){

        RentOrderDTO rentOrderDTO = new RentOrderDTO();
        rentOrderDTO.setId(rentOrder.getId());
        rentOrderDTO.setOrderDate(rentOrder.getOrderDate());

        List<RentOrderDTO.ItemDTO> itemsDTO = new ArrayList<>();
        if (rentOrder.getItems() != null){
            for (RentOrder.Item item : rentOrder.getItems()) {
                RentOrderDTO.ItemDTO itemDTO = new RentOrderDTO.ItemDTO();
                itemDTO.setDays(item.getDays());
                if (item.getMovie() != null){
                    itemDTO.setMovie(item.getMovie().getId());
                }
                itemDTO.setType(item.getMovieType());
                itemDTO.setPaidByBonus(item.isPaidByBonus());
                itemDTO.setReturnedDay(item.getReturnedDay());
                itemsDTO.add(itemDTO);
            }
        }

        rentOrderDTO.setItems(itemsDTO);
        rentOrderDTO.setCustomer(rentOrder.getCustomer().getId());

        return rentOrderDTO;
    }
}
