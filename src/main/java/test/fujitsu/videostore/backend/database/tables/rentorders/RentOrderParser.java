package test.fujitsu.videostore.backend.database.tables.rentorders;

import test.fujitsu.videostore.backend.database.DTO.RentOrderDTO;
import test.fujitsu.videostore.backend.database.tables.BaseParser;
import test.fujitsu.videostore.backend.domain.RentOrder;
import test.fujitsu.videostore.backend.helpers.DatabaseObject;
import test.fujitsu.videostore.backend.helpers.Parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RentOrderParser extends BaseParser implements Parser<RentOrder> {

    private DatabaseObject databaseObject;
    private RentOrderMapper rentOrderMapper;

    public RentOrderParser(String filePath){
        super(filePath);
        this.filePath = filePath;
        this.rentOrderMapper = new RentOrderMapper();
    }

    @Override
    public List<RentOrder> getAll() {
        try {
            databaseObject = objectMapper.readValue(new File(filePath), DatabaseObject.class);
            List<RentOrder> rentOrders = new ArrayList<>();

            rentOrderMapper.setCustomers(databaseObject.getCustomer());
            rentOrderMapper.setMovies(databaseObject.getMovie());

            for (RentOrderDTO rentOrderDto : databaseObject.getOrder()) {
                rentOrders.add(rentOrderMapper.mapToRentOrder(rentOrderDto));
            }

            return rentOrders;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveAll(List<RentOrder> data) {
        List<RentOrderDTO> rentOrderDTOs = new ArrayList<>();

        for (RentOrder rentOrder : data) {
            rentOrderDTOs.add(rentOrderMapper.mapFromRentOrder(rentOrder));
        }
        getAll();
        databaseObject.setOrder(rentOrderDTOs);
        try {
            objectMapper.writeValue(new File(filePath), databaseObject);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
