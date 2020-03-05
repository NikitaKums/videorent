package fujitsu.videostore.backend.database;

import org.junit.Before;
import org.junit.Test;
import test.fujitsu.videostore.backend.database.DBTableRepository;
import test.fujitsu.videostore.backend.database.Database;
import test.fujitsu.videostore.backend.database.DatabaseFactory;
import test.fujitsu.videostore.backend.domain.Customer;
import test.fujitsu.videostore.backend.domain.Movie;
import test.fujitsu.videostore.backend.domain.MovieType;
import test.fujitsu.videostore.backend.domain.RentOrder;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

public abstract class DatabaseTestsBase {
    private static String databasePath;

    private static Database database;
    private DBTableRepository<Movie> movieTableRepository;
    private DBTableRepository<Customer> customerTableRepository;
    private DBTableRepository<RentOrder> rentOrderTableRepository;

    public abstract String getDatabasePath();

    @Before
    public void setupRepositories(){
        databasePath = getDatabasePath();
        database = DatabaseFactory.from(databasePath);
        movieTableRepository = database.getMovieTable();
        customerTableRepository = database.getCustomerTable();
        rentOrderTableRepository = database.getOrderTable();
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
        assertEquals("Movie 1", movieTableRepository.findById(1).getName());
    }

    @Test
    public void canFindRentOrderById(){
        assertEquals(1, rentOrderTableRepository.findById(1).getId());
    }

    @Test
    public void canFindCustomerById(){
        assertEquals("Kristian Vehmas", customerTableRepository.findById(1).getName());
    }

    @Test
    public void canCreateMovie(){
        Movie newMovie = createMovieObject();
        Movie createdMovie = movieTableRepository.createOrUpdate(newMovie);
        Movie fetchedMovie = movieTableRepository.findById(createdMovie.getId());
        movieTableRepository.remove(fetchedMovie);
        assertEquals(newMovie.getName(), fetchedMovie.getName());
    }

    @Test
    public void canCreateRentOrder(){
        RentOrder newRentOrder = createRentOrderObject();
        RentOrder createdRentOrder = rentOrderTableRepository.createOrUpdate(newRentOrder);
        RentOrder fetchedRentOrder = rentOrderTableRepository.findById(createdRentOrder.getId());
        rentOrderTableRepository.remove(fetchedRentOrder);
        assertEquals(createdRentOrder.getId(), fetchedRentOrder.getId());
    }

    @Test
    public void canCreateCustomer(){
        Customer newCustomer = createCustomerObject();
        Customer createdCustomer = customerTableRepository.createOrUpdate(newCustomer);
        Customer fetchedCustomer = customerTableRepository.findById(createdCustomer.getId());
        customerTableRepository.remove(fetchedCustomer);
        assertEquals(newCustomer.getName(), fetchedCustomer.getName());
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
