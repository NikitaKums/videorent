package test.fujitsu.videostore.backend.reciept;

import test.fujitsu.videostore.backend.domain.RentOrder;
import test.fujitsu.videostore.backend.domain.ReturnOrder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple receipt creation service
 * <p>
 * Note! All calculations should be in another place. Here we just setting already calculated data. Feel free to refactor.
 */
public class OrderToReceiptService {

    /**
     * Converts rent order to printable receipt
     *
     * @param order rent object
     * @return Printable receipt object
     */
    public PrintableOrderReceipt convertRentOrderToReceipt(RentOrder order) {
        PrintableOrderReceipt printableOrderReceipt = new PrintableOrderReceipt();
        ReceiptCalculations receiptCalculations = new ReceiptCalculations();

        printableOrderReceipt.setOrderId(order.isNewObject() ? "new" : Integer.toString(order.getId()));
        printableOrderReceipt.setOrderDate(order.getOrderDate());
        printableOrderReceipt.setCustomerName(order.getCustomer().getName());

        printableOrderReceipt.setOrderItems(receiptCalculations.calculatePaymentsForOrderReceipt(order));

        printableOrderReceipt.setTotalPrice(receiptCalculations.getTotalPriceToPay());
        printableOrderReceipt.setRemainingBonusPoints(order.getCustomer().getPoints());

        return printableOrderReceipt;
    }

    /**
     * Converts return order to printable receipt
     *
     * @param order return object
     * @return Printable receipt object
     */
    public PrintableReturnReceipt convertReturnOrderToReceipt(ReturnOrder order) {
        PrintableReturnReceipt receipt = new PrintableReturnReceipt();
        ReceiptCalculations receiptCalculations = new ReceiptCalculations();

        receipt.setOrderId(Integer.toString(order.getRentOrder().getId()));
        receipt.setCustomerName(order.getRentOrder().getCustomer().getName());
        receipt.setRentDate(order.getRentOrder().getOrderDate());
        receipt.setReturnDate(order.getReturnDate());

        receipt.setReturnedItems(receiptCalculations.calculatePaymentsForReturnReceipt(order));

        receipt.setTotalCharge(receiptCalculations.getTotalExtraCharge());

        return receipt;
    }

}
