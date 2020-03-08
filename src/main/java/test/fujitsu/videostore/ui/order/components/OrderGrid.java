package test.fujitsu.videostore.ui.order.components;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import test.fujitsu.videostore.backend.domain.RentOrder;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class OrderGrid extends Grid<RentOrder> {

    public OrderGrid() {
        setSizeFull();
        setId("data-grid");

        addColumn(RentOrder::getId)
                .setSortable(true)
                .setHeader("Order ID")
                .setId("order-id");
        addColumn(order -> order.getCustomer().getName())
                .setFlexGrow(20)
                .setSortable(true)
                .setHeader("Customer name")
                .setId("customer-name");
        addColumn(new LocalDateRenderer<>(RentOrder::getOrderDate, "dd-MM-yyyy"))
                .setFlexGrow(5)
                .setSortable(true)
                .setHeader("Order date")
                .setId("order-date");
        final String availabilityTemplate = "<iron-icon icon=\"vaadin:circle\" class-name=\"[[item.statusClass]]\"></iron-icon>";
        addColumn(TemplateRenderer.<RentOrder>of(availabilityTemplate)
                .withProperty("statusClass", this::getMovieReturnedFlag))
                .setHeader("Status")
                .setFlexGrow(3)
                .setId("availability");
    }

    private String getMovieReturnedFlag(RentOrder order){
        if (order.getItems() == null){
            return ""; // just caution check
        }

        int totalMovies = order.getItems().size();
        int lateMovies = 0;
        int onTimeMovies = 0;

        for (RentOrder.Item item : order.getItems()){
            int lateDays = getLateDays(order.getOrderDate(), LocalDate.now(), item.getDays());
            if (lateDays == 0){
                onTimeMovies += 1;
            } else {
                lateMovies += 1;
            }
        }

        if (totalMovies == onTimeMovies){
            // all returned
            return "Ok";
        }
        else if (totalMovies == lateMovies){
            // all late
            return "Horrible";
        }
        // some late, some on time
        return "SoSo";
    }

    private int getLateDays(LocalDate orderedAt, LocalDate returnedAt, int rentDays){
        int actualRentDays = (int) orderedAt.until(returnedAt, ChronoUnit.DAYS);
        int daysOverdue = actualRentDays - rentDays;
        return Math.max(daysOverdue, 0);
    }
}
