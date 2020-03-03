package fujitsu.videostore.backend.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import test.fujitsu.videostore.backend.database.DBTableRepository;
import test.fujitsu.videostore.backend.database.DTO.RentOrderDTO;
import test.fujitsu.videostore.backend.database.Database;
import test.fujitsu.videostore.backend.database.DatabaseFactory;
import test.fujitsu.videostore.backend.database.tables.rentorders.RentOrderMapper;
import test.fujitsu.videostore.backend.domain.Customer;
import test.fujitsu.videostore.backend.domain.Movie;
import test.fujitsu.videostore.backend.domain.MovieType;
import test.fujitsu.videostore.backend.domain.RentOrder;
import test.fujitsu.videostore.backend.helpers.DatabaseObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DatabaseTests {

    private static final String DATABASE_PATH = "src/test/resources/testsDatabase.json";

    private static Database database;
    private DBTableRepository<Movie> movieTableRepository;
    private DBTableRepository<Customer> customerTableRepository;
    private DBTableRepository<RentOrder> rentOrderTableRepository;

    private static List<Movie> originalMovieList;
    private static List<Customer> originalCustomerList;
    private static List<RentOrder> originalRentOrderList;

    @BeforeClass
    public static void setup(){
        database = DatabaseFactory.from(DATABASE_PATH);
        originalMovieList = database.getMovieTable().getAll();
        originalRentOrderList = database.getOrderTable().getAll();
        originalCustomerList = database.getCustomerTable().getAll();
    }

    @Before
    public void setupRepositories(){
        movieTableRepository = database.getMovieTable();
        customerTableRepository = database.getCustomerTable();
        rentOrderTableRepository = database.getOrderTable();
    }

    @AfterClass
    public static void cleanup() throws IOException {
        DatabaseObject databaseObject = new DatabaseObject();
        RentOrderMapper rentOrderMapper = new RentOrderMapper();
        List<RentOrderDTO> rentOrderDTOS = new ArrayList<>();

        databaseObject.setMovie(originalMovieList);
        databaseObject.setCustomer(originalCustomerList);

        for (RentOrder rentOrder: originalRentOrderList) {
            rentOrderDTOS.add(rentOrderMapper.mapFromRentOrder(rentOrder));
        }
        databaseObject.setOrder(rentOrderDTOS);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATABASE_PATH), databaseObject);
    }

    @Test
    public void canGetMoviesListFromDatabase(){
        assertTrue(movieTableRepository.getAll().size() > 0);
    }

    @Test
    public void canGetRentOrdersListFromDatabase(){
        assertTrue(rentOrderTableRepository.getAll().size() > 0);

    }

    @Test
    public void canGetCustomersListFromDatabase(){
        assertTrue(customerTableRepository.getAll().size() > 0);
    }

    @Test
    public void canFindMovieById(){
        assertEquals("Movie 2", movieTableRepository.findById(2).getName());
    }

    @Test
    public void canFindRentOrderById(){
        assertEquals(1, rentOrderTableRepository.findById(1).getId());
    }

    @Test
    public void canFindCustomerById(){
        assertEquals("Mikk Saar", customerTableRepository.findById(5).getName());
    }

    @Test
    public void canCreateMovie(){
        Movie newMovie = createMovieObject();
        Movie createdMovie = movieTableRepository.createOrUpdate(newMovie);
        assertEquals(newMovie.getName(), movieTableRepository.findById(createdMovie.getId()).getName());
    }

    @Test
    public void canCreateRentOrder(){
        RentOrder newRentOrder = createRentOrderObject();
        RentOrder createdRentOrder = rentOrderTableRepository.createOrUpdate(newRentOrder);
        assertEquals(createdRentOrder.getId(), rentOrderTableRepository.findById(createdRentOrder.getId()).getId());
    }

    @Test
    public void canCreateCustomer(){
        Customer newCustomer = createCustomerObject();
        Customer createdCustomer = customerTableRepository.createOrUpdate(newCustomer);
        assertEquals(newCustomer.getName(), customerTableRepository.findById(createdCustomer.getId()).getName());
    }

    @Test
    public void canRemoveMovie(){
        Movie movie = movieTableRepository.createOrUpdate(createMovieObject());
        movieTableRepository.remove(movie);
        assertNull(movieTableRepository.findById(movie.getId()));

    }

    @Test
    public void canRemoveRentOrder(){
        RentOrder rentOrder = rentOrderTableRepository.createOrUpdate(createRentOrderObject());
        rentOrderTableRepository.remove(rentOrder);
        assertNull(rentOrderTableRepository.findById(rentOrder.getId()));
    }

    @Test
    public void canRemoveCustomer(){
        Customer customer = customerTableRepository.createOrUpdate(createCustomerObject());
        customerTableRepository.remove(customer);
        assertNull(customerTableRepository.findById(customer.getId()));
    }

    private Movie createMovieObject(){
        Movie movie = new Movie();
        movie.setName("Test movie");
        movie.setType(MovieType.NEW);
        movie.setStockCount(1);

        return movie;
    }

    private RentOrder createRentOrderObject(){
        RentOrder rentOrder = new RentOrder();
        rentOrder.setCustomer(createCustomerObject());

        return rentOrder;
    }

    private Customer createCustomerObject(){
        Customer customer = new Customer();
        customer.setName("Test test");
        customer.setPoints(1);

        return customer;
    }
}
