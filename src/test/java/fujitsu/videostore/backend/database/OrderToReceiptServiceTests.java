package fujitsu.videostore.backend.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.*;
import test.fujitsu.videostore.backend.database.DBTableRepository;
import test.fujitsu.videostore.backend.database.Database;
import test.fujitsu.videostore.backend.database.DatabaseFactory;
import test.fujitsu.videostore.backend.database.RepositoryInstance;
import test.fujitsu.videostore.backend.domain.*;
import test.fujitsu.videostore.backend.helpers.DatabaseObject;
import test.fujitsu.videostore.backend.reciept.OrderToReceiptService;
import test.fujitsu.videostore.backend.reciept.PrintableOrderReceipt;
import test.fujitsu.videostore.backend.reciept.PrintableReturnReceipt;
import test.fujitsu.videostore.backend.reciept.ReceiptCalculations;
import test.fujitsu.videostore.ui.database.CurrentDatabase;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OrderToReceiptServiceTests {
    private static String DATABASE_PATH = "src/test/resources/testsDatabase.json";

    private DBTableRepository<Movie> movieTableRepository;
    private DBTableRepository<Customer> customerTableRepository;
    private static DatabaseObject initialTestDb;
    private static ObjectMapper objectMapper = new ObjectMapper();

    private OrderToReceiptService orderToReceiptService = new OrderToReceiptService();

    @BeforeClass
    public static void setupDb() throws IOException {
        // initialize tables
        Database database = DatabaseFactory.from(DATABASE_PATH);
        database.getCustomerTable();
        database.getMovieTable();
        database.getOrderTable();

        objectMapper.findAndRegisterModules();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        initialTestDb = objectMapper.readValue(new File(DATABASE_PATH), DatabaseObject.class);
    }

    @Before
    public void setup(){
        movieTableRepository = RepositoryInstance.GetMovieDBTableRepository();
        customerTableRepository = RepositoryInstance.GetCustomerDBTableRepository();
    }

    // reset json file for next tests
    @After
    public void cleanup() throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATABASE_PATH), initialTestDb);
    }

    @Test
    public void shouldDecreaseMovieStockOnRent(){
        int initialStock = movieTableRepository.findById(1).getStockCount();
        orderToReceiptService.convertRentOrderToReceipt(createRentOrder(createItemWithOldMovie()));
        int stockAfterReturn = movieTableRepository.findById(1).getStockCount();

        assertEquals(initialStock - 1 , stockAfterReturn);
    }

    @Test
    public void shouldIncreaseMovieStockOnReturn(){
        int initialStock = movieTableRepository.findById(1).getStockCount();
        orderToReceiptService.convertReturnOrderToReceipt(createReturnOrder(createItemWithOldMovie()));
        int stockAfterReturn = movieTableRepository.findById(1).getStockCount();

        assertEquals(initialStock + 1 , stockAfterReturn);
    }

    @Test
    public void shouldAddOneBonusPointForOldRental(){
        int initialPoints = customerTableRepository.findById(1).getPoints();
        orderToReceiptService.convertRentOrderToReceipt(createRentOrder(createItemWithOldMovie()));
        int newPoints = customerTableRepository.findById(1).getPoints();

        assertEquals(initialPoints + 1 , newPoints);
    }

    @Test
    public void shouldAddOneBonusPointForRegularRental(){
        int initialPoints = customerTableRepository.findById(1).getPoints();
        RentOrder rentOrder = createRentOrder(createItemWithRegularMovie());
        orderToReceiptService.convertRentOrderToReceipt(rentOrder);
        int newPoints = customerTableRepository.findById(1).getPoints();

        assertEquals(initialPoints + 1 , newPoints);
    }

    @Test
    public void shouldAddTwoBonusPointsForNewRental(){
        int initialPoints = customerTableRepository.findById(1).getPoints();
        orderToReceiptService.convertRentOrderToReceipt(createRentOrder(createItemWithNewMovie()));
        int newPoints = customerTableRepository.findById(1).getPoints();

        assertEquals(initialPoints + 2 , newPoints);
    }

    @Test
    public void shouldRemove25BonusPointsForNewReleasePay(){
        int initialPoints = customerTableRepository.findById(1).getPoints();
        RentOrder rentOrder = createRentOrder(createItemWithNewMovie());
        rentOrder.getItems().forEach(item -> item.setPaidByBonus(true));
        orderToReceiptService.convertRentOrderToReceipt(rentOrder);
        int newPoints = customerTableRepository.findById(1).getPoints();

        // + 2 points because new rental
        assertEquals(initialPoints - 25 + 2 , newPoints);
    }

    @Test
    public void shouldChargeCorrectAmountForNewRelease(){
        RentOrder rentOrder = createRentOrder(createItemWithNewMovie());
        rentOrder.setOrderDate(LocalDate.now().minusDays(5));
        rentOrder.getItems().get(0).setDays(5);
        rentOrder.getItems().get(0).setReturnedDay(LocalDate.now());

        PrintableOrderReceipt receipt = orderToReceiptService.convertRentOrderToReceipt(rentOrder);

        assertEquals(BigDecimal.valueOf(5 * 4), receipt.getTotalPrice());
    }

    @Test
    public void shouldChargeCorrectAmountForOldFilm(){
        RentOrder rentOrder = createRentOrder(createItemWithOldMovie());
        rentOrder.setOrderDate(LocalDate.now().minusDays(6));
        rentOrder.getItems().get(0).setDays(6);
        rentOrder.getItems().get(0).setReturnedDay(LocalDate.now());

        PrintableOrderReceipt receipt = orderToReceiptService.convertRentOrderToReceipt(rentOrder);

        assertEquals(BigDecimal.valueOf(6), receipt.getTotalPrice());
    }

    @Test
    public void shouldChargeCorrectAmountForRegularMovie(){
        RentOrder rentOrder = createRentOrder(createItemWithRegularMovie());
        rentOrder.setOrderDate(LocalDate.now().minusDays(5));
        rentOrder.getItems().get(0).setDays(5);
        rentOrder.getItems().get(0).setReturnedDay(LocalDate.now());

        PrintableOrderReceipt receipt = orderToReceiptService.convertRentOrderToReceipt(rentOrder);

        assertEquals(BigDecimal.valueOf(9), receipt.getTotalPrice());
    }

    @Test
    public void shouldChargeCorrectAmountForLateReturn(){
        ReturnOrder returnOrder = createReturnOrder(createItemWithRegularMovie());
        returnOrder.getRentOrder().setOrderDate(LocalDate.now().minusDays(10)); // order placed 10 days ago
        returnOrder.getItems().get(0).setDays(5); // ordered for 5 days

        PrintableReturnReceipt receipt = orderToReceiptService.convertReturnOrderToReceipt(returnOrder);

        assertEquals(BigDecimal.valueOf(9), receipt.getTotalCharge());
    }

    private RentOrder createRentOrder(RentOrder.Item item){
        RentOrder rentOrder = new RentOrder();
        rentOrder.setCustomer(customerTableRepository.findById(1));
        rentOrder.setOrderDate(LocalDate.now());
        List<RentOrder.Item> items = new ArrayList<>();
        items.add(item);
        rentOrder.setItems(items);
        return rentOrder;
    }

    private ReturnOrder createReturnOrder(RentOrder.Item item){
        ReturnOrder returnOrder = new ReturnOrder();
        returnOrder.setRentOrder(createRentOrder(item));
        returnOrder.setReturnDate(LocalDate.now());
        List<RentOrder.Item> items = new ArrayList<>();
        items.add(item);
        returnOrder.setItems(items);
        return returnOrder;
    }

    private RentOrder.Item createItemWithOldMovie(){
        return createItem(1);
    }

    private RentOrder.Item createItemWithNewMovie(){
        return createItem(2);
    }

    private RentOrder.Item createItemWithRegularMovie(){
        return createItem(3);
    }

    private RentOrder.Item createItem(int movieId){
        RentOrder.Item item = new RentOrder.Item();
        item.setMovie(movieTableRepository.findById(movieId));
        item.setMovieType(movieTableRepository.findById(movieId).getType());
        item.setReturnedDay(LocalDate.now());
        item.setDays(1);
        item.setPaidByBonus(false);
        return item;
    }
}
