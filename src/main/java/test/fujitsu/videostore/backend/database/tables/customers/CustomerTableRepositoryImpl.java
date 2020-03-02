package test.fujitsu.videostore.backend.database.tables.customers;

import test.fujitsu.videostore.backend.database.tables.BaseRepositoryImpl;
import test.fujitsu.videostore.backend.domain.Customer;

import java.util.Collections;
import java.util.Comparator;

public class CustomerTableRepositoryImpl extends BaseRepositoryImpl<Customer> implements CustomerTableRepository {

    public CustomerTableRepositoryImpl(String filePath) {
        super(new CustomerParser(filePath));
    }

    @Override
    public Customer findById(int id) {
        return list.stream().filter(customer -> customer.getId() == id).findFirst().get();
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
    public int generateNextId() {
        int id = Collections.max(list, Comparator.comparingInt(Customer::getId)).getId();
        if (id == -1) {
            id = 0;
        }
        return id + 1;
    }
}
