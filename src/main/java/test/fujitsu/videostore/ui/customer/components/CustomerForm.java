package test.fujitsu.videostore.ui.customer.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.value.ValueChangeMode;
import test.fujitsu.videostore.backend.database.DBTableRepository;
import test.fujitsu.videostore.backend.domain.Customer;
import test.fujitsu.videostore.backend.domain.RentOrder;
import test.fujitsu.videostore.backend.domain.RentOrder.Item;
import test.fujitsu.videostore.ui.base.BaseForm;
import test.fujitsu.videostore.ui.base.BaseFormImpl;
import test.fujitsu.videostore.ui.customer.CustomerListLogic;
import test.fujitsu.videostore.ui.database.CurrentDatabase;
import test.fujitsu.videostore.ui.helpers.Helper;

import java.util.List;

/**
 * Customer edit/creation form
 */
public class CustomerForm extends BaseFormImpl<Customer, CustomerListLogic> implements BaseForm<Customer> {

    private VerticalLayout content;

    private TextField name;
    private TextField points;
    private Binder<Customer> binder;

    public CustomerForm(CustomerListLogic customerListLogic) {
        super(new Customer(), customerListLogic);
        setId("edit-form");

        content = Helper.CreateVerticalLayout();
        content.setSizeUndefined();
        add(content);

        name = Helper.CreateTextField("customer-name", "Customer name", "100%", true,  ValueChangeMode.EAGER);
        content.add(name);

        points = Helper.CreateTextField("bonus-points", "Bonus points", "100%", true, ValueChangeMode.EAGER);
        content.add(points);

        createSaveButton();
        createCancelButton();
        createDeleteButton();

        addListenerToSaveButton();
        binder = createBinder();

        content.add(save, delete, cancel);
    }

    private Binder<Customer> createBinder(){
        Binder<Customer> tempBinder = new Binder<>(Customer.class);
        tempBinder.forField(name)
                .bind("name");
        tempBinder.forField(points)
                .withValidator(item -> item != null && !item.trim().isEmpty() && !item.contains(","), "Invalid bonus points format")
                .withConverter(new StringToIntegerConverter("Invalid bonus points format"))
                .bind("points");
        Helper.AddBinderStatusChangeListener(tempBinder, save);

        return tempBinder;
    }

    private void addListenerToSaveButton(){
        save.addClickListener(event -> {
            if (entity != null) {
                binder.writeBeanIfValid(entity);

                if (entity.getName() == null || entity.getName().trim().isEmpty()){
                    Notification.show("Please fill customer name field.");
                    return;
                }
                if (entity.getPoints() < 0 ) {
                    Notification.show("Please enter positive points amount");
                    return;
                }
                if (doesCustomerNameExists(entity.getId(), entity.getName())){
                    Notification.show("Customer with given name already exists");
                    return;
                }

                viewLogic.saveEntity(entity);
            }
        });
    }

    @Override
    public void editEntity(Customer customer) {
        if (customer == null) {
            customer = new Customer();
        }
        if (customer.getId() == -1){
            delete.setEnabled(false);
        }
        else {
            delete.setEnabled(!doesCustomerHaveActiveRent(customer.getId()));
        }
        entity = customer;
        binder.readBean(customer);
    }

    // As stated that database interfaces should not be changed, i am not going to change Database/DBTableRepository<Customer> to CustomerTableRepository.
    // Thus i am unable to define methods in CustomerTableRepository interface to access them via CurrentDatabase.get().getCustomerTable().
    // Which means that i have to find if customer has active rents through getOrderTable().getAll() iteration.
    // e.g. doesCustomerHaveActiveRent(int id) in CustomerTableRepository and access it via
    // CurrentDatabase.get().getCustomerTable().doesCustomerHaveActiveRent(customerId);
    private boolean doesCustomerHaveActiveRent(int customerId){
        DBTableRepository<RentOrder> orderTable = CurrentDatabase.get().getOrderTable();
        for (RentOrder rentOrder: orderTable.getAll()) {
            if (rentOrder.getCustomer().getId() == customerId){
                List<Item> items = rentOrder.getItems();
                if (items != null){
                    for (Item item: items) {
                        if (item.getReturnedDay() == null){
                            // not returned yet, so active rent
                            return true;
                        }
                    }
                }
            }
        }
        return false; // no active rents
    }

    private boolean doesCustomerNameExists(int id, String customerName){
        return CurrentDatabase.get().getCustomerTable().getAll().stream().anyMatch(item -> item.getName().equals(customerName) && item.getId() != id);
    }
}
