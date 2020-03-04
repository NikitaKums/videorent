package test.fujitsu.videostore.ui.order.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.ValidationResult;
import test.fujitsu.videostore.backend.domain.Customer;
import test.fujitsu.videostore.backend.domain.RentOrder;
import test.fujitsu.videostore.ui.base.BaseForm;
import test.fujitsu.videostore.ui.base.BaseFormImpl;
import test.fujitsu.videostore.ui.database.CurrentDatabase;
import test.fujitsu.videostore.ui.helpers.Helper;
import test.fujitsu.videostore.ui.order.OrderListLogic;

public class OrderForm extends BaseFormImpl<RentOrder, OrderListLogic> implements BaseForm<RentOrder> {

    private VerticalLayout content;

    private ComboBox<Customer> customerComboBox;
    private DatePicker orderDate;
    private OrderedVideos orderedVideos;

    private Button returnButton;

    private Binder<RentOrder> binder;

    public OrderForm(OrderListLogic orderListLogic) {
        super(new RentOrder(), orderListLogic);
        setId("edit-form");
        setSizeFull();

        content = Helper.CreateVerticalLayout();
        content.setId("order-list-form-container");
        content.setSizeUndefined();
        content.setMargin(false);

        add(content);

        customerComboBox = Helper.CreateComboBox("customer", "Customer", "100%", true);
        customerComboBox.setItems(CurrentDatabase.get().getCustomerTable().getAll());
        customerComboBox.setItemLabelGenerator(Customer::getName);
        content.add(customerComboBox);

        orderDate = Helper.CreateDatePicker("order-date", "Order date", "100%", true, false);
        content.add(orderDate);

        orderedVideos = new OrderedVideos();
        content.add(orderedVideos);

        createSaveButton();
        createCancelButton();
        createDeleteButton();
        returnButton = createReturnButton();

        addListenerToSaveButton();
        binder = createBinder();

        content.add(save, returnButton, delete, cancel);
    }

    private Binder<RentOrder> createBinder(){
        Binder<RentOrder> tempBinder = new Binder<>(RentOrder.class);
        tempBinder.forField(customerComboBox)
                .asRequired()
                .bind("customer");
        tempBinder.forField(orderDate)
                .bind("orderDate");
        tempBinder.forField(orderedVideos)
                .withValidator(items -> items != null && items.size() > 0, "Add at least one movie")
                .bind("items");

        return tempBinder;
    }

    private Button createReturnButton(){
        Button button = Helper.CreateButtonWithTextAndWidth("return", "Return movies", "100%");
        button.addClickListener(event -> {
            ReturnMovieWindow returnMovieWindow = new ReturnMovieWindow(entity, viewLogic.getOrderToReceiptService(), viewLogic.getRepository(), () -> viewLogic.editEntity(entity));
            returnMovieWindow.open();
        });

        return button;
    }

    private void addListenerToSaveButton(){
        save.addClickListener(event -> {
            BinderValidationStatus<RentOrder> validationStatus = binder.validate();
            if (validationStatus.hasErrors()) {
                ValidationResult firstError = validationStatus.getValidationErrors().iterator().next();
                Notification.show(firstError.getErrorMessage(), 5000, Notification.Position.MIDDLE);
                return;
            }

            // TODO: Validate that user have enough bonus points
            binder.writeBeanIfValid(entity);
            new ReceiptWindow(viewLogic.getOrderToReceiptService().convertRentOrderToReceipt(entity).print(), entity.isNewObject(), () -> viewLogic.saveEntity(entity));
        });
    }

    @Override
    public void editEntity(RentOrder order) {
        boolean isNew = order.isNewObject();
        if (isNew) {
            order = new RentOrder();
            orderedVideos.setReadOnly(false);
            delete.setVisible(false);
            returnButton.setVisible(false);
        } else {
            orderedVideos.setReadOnly(true);
            delete.setVisible(true);
            returnButton.setVisible(true);
        }

        setSaveButtonCaption(!isNew);
        entity = order;
        save.setEnabled(true);
        binder.readBean(entity);
        binder.setReadOnly(!isNew);
        orderDate.setVisible(!isNew);
        orderDate.setReadOnly(true);

        // TODO: returnButton should be not enabled if all movies were returned from this order
        returnButton.setEnabled(true);

        // TODO: Delete button should be disabled during new order creation or if order there is not all movies returned.
        delete.setEnabled(true);
    }

    private void setSaveButtonCaption(boolean isReadOnly) {
        save.setText(isReadOnly ? "View receipt" : "Review and Print receipt");
    }

    public RentOrder getCurrentOrder() {
        return entity;
    }
}
