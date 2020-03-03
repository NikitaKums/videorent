package test.fujitsu.videostore.backend.database.tables.rentorders;

import test.fujitsu.videostore.backend.database.tables.BaseRepositoryImpl;
import test.fujitsu.videostore.backend.domain.RentOrder;

import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;

public class RentOrderRepositoryImpl extends BaseRepositoryImpl<RentOrder> implements RentOrderRepository {

    public RentOrderRepositoryImpl(String filePath) {
        super(new RentOrderParser(filePath));
    }

    @Override
    public RentOrder findById(int id) {
        Optional<RentOrder> rentOrder = list.stream().filter(rentOrder1 -> rentOrder1.getId() == id).findFirst();
        return rentOrder.orElse(null);
    }

    @Override
    public RentOrder createOrUpdate(RentOrder object) {
        if (object == null) {
            return null;
        }

        if (object.getId() == -1) {
            object.setId(generateNextId());
            list.add(object);

            saveChanges();
            return object;
        }

        RentOrder rentOrder = findById(object.getId());

        rentOrder.setCustomer(object.getCustomer());
        rentOrder.setItems(object.getItems());
        rentOrder.setOrderDate(object.getOrderDate());

        saveChanges();
        return rentOrder;
    }

    @Override
    public int generateNextId() {
        int id = Collections.max(list, Comparator.comparingInt(RentOrder::getId)).getId();
        if (id == -1) {
            id = 0;
        }
        return id + 1;
    }
}
