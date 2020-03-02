package test.fujitsu.videostore.backend.database;

import test.fujitsu.videostore.backend.database.tables.customers.CustomerTableRepositoryImpl;
import test.fujitsu.videostore.backend.database.tables.movies.MovieTableRepository;
import test.fujitsu.videostore.backend.database.tables.movies.MovieTableRepositoryImpl;
import test.fujitsu.videostore.backend.database.tables.rentorders.RentOrderRepositoryImpl;
import test.fujitsu.videostore.backend.domain.Customer;
import test.fujitsu.videostore.backend.domain.Movie;
import test.fujitsu.videostore.backend.domain.MovieType;
import test.fujitsu.videostore.backend.domain.RentOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Database Factory.
 * <p>
 * TODO: Should be re-implemented with your file database. Current implementation is just demo for UI testing.
 */
public class DatabaseFactory {

    /**
     * Creates database "connection"/opens database from path.
     * <p>
     * TODO: Implement database parsing, fetching, creation, modification, removing from JSON or YAML file database.
     * Two example files, /db-examples/database.json and /db-examples/database.yaml.
     * Hint: MovieType.databaseId == type field in database files.
     *
     * TODO: Current way of creating next ID is incorrect, make better implementation.
     *
     * @param filePath file path to database
     * @return database proxy for different tables
     */
    public static Database from(String filePath) {
        return new Database() {
            @Override
            public DBTableRepository<Movie> getMovieTable() {
                return new MovieTableRepositoryImpl(filePath);
            }

            @Override
            public DBTableRepository<Customer> getCustomerTable() {
                return new CustomerTableRepositoryImpl(filePath);
            }

            @Override
            public DBTableRepository<RentOrder> getOrderTable() {
                return new RentOrderRepositoryImpl(filePath);
            }
        };
    }
}
