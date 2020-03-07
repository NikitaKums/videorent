package test.fujitsu.videostore.backend.database;

import test.fujitsu.videostore.backend.database.tables.customers.CustomerTableRepositoryImpl;
import test.fujitsu.videostore.backend.database.tables.movies.MovieTableRepositoryImpl;
import test.fujitsu.videostore.backend.database.tables.rentorders.RentOrderRepositoryImpl;
import test.fujitsu.videostore.backend.domain.Customer;
import test.fujitsu.videostore.backend.domain.Movie;
import test.fujitsu.videostore.backend.domain.RentOrder;

public class RepositoryInstance {

    private static DBTableRepository<Movie> movieDBTableRepository;
    private static DBTableRepository<Customer> customerDBTableRepository;
    private static DBTableRepository<RentOrder> rentOrderDBTableRepository;

    public static DBTableRepository<Movie> GetMovieDBTableRepository(String filePath){
        if (movieDBTableRepository == null) movieDBTableRepository = new MovieTableRepositoryImpl(filePath);
        return GetMovieDBTableRepository();
    }

    public static DBTableRepository<Customer> GetCustomerDBTableRepository(String filePath){
        if (customerDBTableRepository == null) customerDBTableRepository = new CustomerTableRepositoryImpl(filePath);
        return GetCustomerDBTableRepository();
    }

    public static DBTableRepository<RentOrder> GetRentOrderDBTableRepository(String filePath){
        if (rentOrderDBTableRepository == null) rentOrderDBTableRepository = new RentOrderRepositoryImpl(filePath);
        return GetRentOrderDBTableRepository();
    }

    public static DBTableRepository<Movie> GetMovieDBTableRepository(){
        return movieDBTableRepository;
    }

    public static DBTableRepository<Customer> GetCustomerDBTableRepository(){
        return customerDBTableRepository;
    }

    public static DBTableRepository<RentOrder> GetRentOrderDBTableRepository(){
        return rentOrderDBTableRepository;
    }
}
