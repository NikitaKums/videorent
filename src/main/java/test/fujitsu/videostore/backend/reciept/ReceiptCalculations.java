package test.fujitsu.videostore.backend.reciept;

import test.fujitsu.videostore.backend.database.DBTableRepository;
import test.fujitsu.videostore.backend.database.RepositoryInstance;
import test.fujitsu.videostore.backend.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class ReceiptCalculations {

    private static final int PREMIUM_PRICE = 4;
    private static final int BASIC_PRICE = 3;

    private DBTableRepository<Movie> movieDBTableRepository = RepositoryInstance.GetMovieDBTableRepository();
    private DBTableRepository<Customer> customerDBTableRepository = RepositoryInstance.GetCustomerDBTableRepository();

    private BigDecimal totalPriceToPay = new BigDecimal(0);
    private BigDecimal totalExtraCharge = new BigDecimal(0);
    private int totalBonusPointsToPay = 0;
    private int totalBonusPointsForRenting = 0;

    public List<PrintableOrderReceipt.Item> calculatePaymentsForOrderReceipt (RentOrder order){
        List<PrintableOrderReceipt.Item> itemList = new ArrayList<>();

        for (RentOrder.Item orderItem : order.getItems()) {
            PrintableOrderReceipt.Item item = new PrintableOrderReceipt.Item();
            item.setDays(orderItem.getDays());
            item.setMovieName(orderItem.getMovie().getName());
            item.setMovieType(orderItem.getMovieType());

            // update movie stock
            Movie movie = orderItem.getMovie();
            orderItem.setMovie(updateMovieStock(movie, movie.getStockCount() - 1));
            totalBonusPointsForRenting += getBonusPointsForRental(orderItem.getMovieType());

            int days = item.getDays();
            if (orderItem.isPaidByBonus()) {
                totalBonusPointsToPay += days * 25;
                item.setPaidBonus(days * 25);
            } else {
                BigDecimal toPay = getRentalPrice(orderItem.getMovieType(), days);
                item.setPaidMoney(toPay);
                totalPriceToPay = totalPriceToPay.add(toPay);
            }
            itemList.add(item);
        }

        Customer customer = order.getCustomer();
        // add points for renting
        order.setCustomer(updateCustomerPoints(customer, customer.getPoints() + totalBonusPointsForRenting));

        customer = order.getCustomer();
        // remove points for renting
        order.setCustomer(updateCustomerPoints(customer, customer.getPoints() - totalBonusPointsToPay));

        return itemList;
    }

    public List<PrintableReturnReceipt.Item> calculatePaymentsForReturnReceipt(ReturnOrder order){
        List<PrintableReturnReceipt.Item> returnedItems = new ArrayList<>();
        if (order.getItems() != null) {
            for (RentOrder.Item rentedItem : order.getItems()) {
                PrintableReturnReceipt.Item item = new PrintableReturnReceipt.Item();
                item.setMovieName(rentedItem.getMovie().getName());
                item.setMovieType(rentedItem.getMovieType());

                item.setExtraDays(getLateDays(order.getRentOrder().getOrderDate(), LocalDate.now(), rentedItem.getDays()));

                // extra days pay
                BigDecimal extraPay = getRentalPrice(rentedItem.getMovieType(), item.getExtraDays());
                totalExtraCharge = totalExtraCharge.add(extraPay);
                item.setExtraPrice(extraPay);

                // update movie stock + 1
                Movie movie = rentedItem.getMovie();
                updateMovieStock(movie, movie.getStockCount() + 1);

                returnedItems.add(item);
            }
        }

        return returnedItems;
    }

    public BigDecimal getTotalPriceToPay() {
        return totalPriceToPay;
    }

    public BigDecimal getTotalExtraCharge() {
        return totalExtraCharge;
    }

    private int getLateDays(LocalDate orderedAt, LocalDate returnedAt, int rentDays){
        int actualRentDays = (int) orderedAt.until(returnedAt, ChronoUnit.DAYS);
        int daysOverdue = actualRentDays - rentDays;
        return Math.max(daysOverdue, 0);
    }

    private Movie updateMovieStock(Movie movie, int newStock){
        Movie movieById = movieDBTableRepository.findById(movie.getId());
        movieById.setStockCount(newStock);
        return movieDBTableRepository.createOrUpdate(movieById);
    }

    private Customer updateCustomerPoints(Customer customer, int newPointsAmount){
        Customer customerById = customerDBTableRepository.findById(customer.getId());
        customerById.setPoints(newPointsAmount);
        return customerDBTableRepository.createOrUpdate(customer);
    }

    private int getBonusPointsForRental(MovieType movieType){
        if (movieType.getDatabaseId() == 1) {
            return 2;
        }
        return 1;
    }

    private BigDecimal getRentalPrice(MovieType movieType, int days){
        if (days == 0){
            return BigDecimal.valueOf(0);
        }
        switch (movieType.getDatabaseId()){
            case 1: // new release
                return BigDecimal.valueOf(PREMIUM_PRICE * days);
            case 2: // regular rental
                return BigDecimal.valueOf(days <= 3 ? BASIC_PRICE : BASIC_PRICE + BASIC_PRICE * (days - 3));
            case 3: // old film
                return BigDecimal.valueOf(days <= 5 ? BASIC_PRICE : BASIC_PRICE + BASIC_PRICE * (days - 5));
            default:
                throw new RuntimeException("No such movie type present");
        }
    }
}
