package test.fujitsu.videostore.backend.database.tables.customers;

import test.fujitsu.videostore.backend.database.RepositoryInstance;
import test.fujitsu.videostore.backend.database.tables.BaseRepositoryImpl;
import test.fujitsu.videostore.backend.domain.Customer;
import test.fujitsu.videostore.backend.domain.RentOrder;

import java.util.*;

public class CustomerTableRepositoryImpl extends BaseRepositoryImpl<Customer> implements CustomerTableRepository {

    public CustomerTableRepositoryImpl(String filePath) {
        super(new CustomerParser(filePath));
    }

    @Override
    public Customer findById(int id) {
        Optional<Customer> customer = list.stream().filter(customer1 -> customer1.getId() == id).findFirst();
        return customer.orElse(null);
    }

    @Override
    public Customer createOrUpdate(Customer object) {
        if (object == null) {
            return null;
        }

        if (object.getId() == -1) {
            object.setId(generateNextId());
            list.add(object);

            saveChanges();
            return object;
        }

        Customer customer = findById(object.getId());

        customer.setName(object.getName());
        customer.setPoints(object.getPoints());

        saveChanges();
        return customer;
    }

    @Override
    public boolean remove(Customer object) {
        removeRentOrderWithCustomer(object);
        boolean result = list.remove(object);
        saveChanges();
        return result;
    }

    @Override
    public int generateNextId() {
        int id = Collections.max(list, Comparator.comparingInt(Customer::getId)).getId();
        if (id == -1) {
            id = 0;
        }
        return id + 1;
    }

    // this kind of approach because of file database
    private void removeRentOrderWithCustomer(Customer customer){
        List<RentOrder> rentOrdersToDelete = new ArrayList<>();
        RepositoryInstance.GetRentOrderDBTableRepository().getAll().forEach(item -> {
            if (item.getCustomer().getId() == customer.getId()){ // cannot delete customer who has active rents
                rentOrdersToDelete.add(item);
            }
        });
        rentOrdersToDelete.forEach(item -> RepositoryInstance.GetRentOrderDBTableRepository().remove(item));
    }
}
