package test.fujitsu.videostore.backend.database.tables.customers;

import test.fujitsu.videostore.backend.database.tables.BaseParser;
import test.fujitsu.videostore.backend.database.tables.BaseParserImpl;
import test.fujitsu.videostore.backend.domain.Customer;
import test.fujitsu.videostore.backend.helpers.DatabaseObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CustomerParser extends BaseParserImpl<Customer> {

    private DatabaseObject databaseObject;

    public CustomerParser(String filePath){
        super(filePath);
        this.filePath = filePath;
    }

    @Override
    public List<Customer> getAll() {
        try {
            databaseObject = objectMapper.readValue(new File(filePath), DatabaseObject.class);
            return databaseObject.getCustomer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }    }

    @Override
    public void saveAll(List<Customer> data) {
        getAll();
        databaseObject.setCustomer(data);
        try {
            objectMapper.writeValue(new File(filePath), databaseObject);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
