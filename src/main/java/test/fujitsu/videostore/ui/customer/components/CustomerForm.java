package test.fujitsu.videostore.ui.customer.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.value.ValueChangeMode;
import test.fujitsu.videostore.backend.domain.Customer;
import test.fujitsu.videostore.ui.base.BaseForm;
import test.fujitsu.videostore.ui.base.BaseFormImpl;
import test.fujitsu.videostore.ui.customer.CustomerListLogic;
import test.fujitsu.videostore.ui.helpers.Helper;

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
                .withConverter(new StringToIntegerConverter("Invalid bonus points format"))
                .bind("points");
        Helper.AddBinderStatusChangeListener(tempBinder, save);

        return tempBinder;
    }

    private void addListenerToSaveButton(){
        save.addClickListener(event -> {
            if (entity != null) {
                // TODO: Perform validations here. Need to validate that customer name is filled, bonus points have correct integer representation.
                // TODO: Validation that customer with same name is not present already in database.

                binder.writeBeanIfValid(entity);
                viewLogic.saveEntity(entity);
            }
        });
    }

    @Override
    public void editEntity(Customer customer) {
        if (customer == null) {
            customer = new Customer();
        }
        entity = customer;
        binder.readBean(customer);

        // TODO: Customer deletion button should be inactive if it’s new customer creation or customer have active rent’s. If customer deleted, then all his already inactive rent’s should be deleted also.
        delete.setEnabled(true);
    }
}
