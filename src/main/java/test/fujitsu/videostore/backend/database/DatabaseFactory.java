package test.fujitsu.videostore.backend.database;

import test.fujitsu.videostore.backend.domain.Customer;
import test.fujitsu.videostore.backend.domain.Movie;
import test.fujitsu.videostore.backend.domain.RentOrder;

/**
 * Database Factory.
 * <p>
 */
public class DatabaseFactory {

    /**
     * Creates database "connection"/opens database from path.
     * <p>
     * Two example files, /db-examples/database.json and /db-examples/database.yaml.
     * Hint: MovieType.databaseId == type field in database files.
     *
     *
     * @param filePath file path to database
     * @return database proxy for different tables
     */
    public static Database from(String filePath) {
        return new Database() {
            @Override
            public DBTableRepository<Movie> getMovieTable() {
                return RepositoryInstance.GetMovieDBTableRepository(filePath);
            }

            @Override
            public DBTableRepository<Customer> getCustomerTable() {
                return RepositoryInstance.GetCustomerDBTableRepository(filePath);
            }

            @Override
            public DBTableRepository<RentOrder> getOrderTable() {
                return RepositoryInstance.GetRentOrderDBTableRepository(filePath);
            }
        };
    }
}
