package test.fujitsu.videostore.ui.customer;

import com.vaadin.flow.component.UI;
import test.fujitsu.videostore.backend.database.DBTableRepository;
import test.fujitsu.videostore.backend.domain.Customer;
import test.fujitsu.videostore.ui.base.BaseListLogicImpl;
import test.fujitsu.videostore.ui.database.CurrentDatabase;

public class CustomerListLogic extends BaseListLogicImpl<Customer, CustomerList> {

    public CustomerListLogic(CustomerList customerList) {
        super(customerList);
    }

    @Override
    public void init() {
        if (CurrentDatabase.get() == null) {
            return;
        }

        dBTableRepository = CurrentDatabase.get().getCustomerTable();

        view.setNewEntityEnabled(true);
        view.setEntities(dBTableRepository.getAll());
    }

    @Override
    public void setFragmentParameter(String movieId) {
        String fragmentParameter;
        if (movieId == null || movieId.isEmpty()) {
            fragmentParameter = "";
        } else {
            fragmentParameter = movieId;
        }

        UI.getCurrent().navigate(CustomerList.class, fragmentParameter);
    }

    @Override
    public void saveEntity(Customer customer) {
        boolean isNew = customer.isNewObject();

        Customer updatedObject = dBTableRepository.createOrUpdate(customer);

        if (isNew) {
            view.addEntity(updatedObject);
        } else {
            view.updateEntity(customer);
        }

        view.clearSelection();
        setFragmentParameter("");
        view.showSaveNotification(customer.getName() + (isNew ? " created" : " updated"));
    }

    @Override
    public void deleteEntity(Customer customer) {
        dBTableRepository.remove(customer);

        view.clearSelection();
        view.removeEntity(customer);
        setFragmentParameter("");
        view.showSaveNotification(customer.getName() + " removed");
    }

    @Override
    public void editEntity(Customer customer) {
        if (customer == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(customer.getId() + "");
        }
        view.editEntity(customer);
    }

    @Override
    public void newEntity() {
        setFragmentParameter("new");
        view.clearSelection();
        view.editEntity(new Customer());
    }
}
