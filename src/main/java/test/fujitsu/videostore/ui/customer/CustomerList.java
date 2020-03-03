package test.fujitsu.videostore.ui.customer;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import test.fujitsu.videostore.backend.domain.Customer;
import test.fujitsu.videostore.ui.MainLayout;
import test.fujitsu.videostore.ui.base.BaseTableDisplayImpl;
import test.fujitsu.videostore.ui.customer.components.CustomerForm;
import test.fujitsu.videostore.ui.customer.components.CustomerGrid;
import test.fujitsu.videostore.ui.helpers.Helper;

import java.util.ArrayList;
import java.util.List;

@Route(value = CustomerList.VIEW_NAME, layout = MainLayout.class)
public class CustomerList extends BaseTableDisplayImpl<Customer, CustomerGrid> implements HasUrlParameter<String> {

    public static final String VIEW_NAME = "CustomerList";
    private CustomerGrid grid;
    private CustomerForm form;
    private TextField filter;

    private CustomerListLogic viewLogic = new CustomerListLogic(this);
    private Button newCustomer;

    public CustomerList() {
        super(new ListDataProvider<>(new ArrayList<>()));
        setId(VIEW_NAME);
        setSizeFull();

        HorizontalLayout topLayout = createTopBar();
        initializeGrid();
        form = new CustomerForm(viewLogic);

        add(Helper.CreateVerticalLayout(topLayout, grid));
        add(form);

        viewLogic.init();
    }

    private HorizontalLayout createTopBar() {
        createFilterTextField();
        createNewMovieButton();
        return Helper.CreateHorizontalLayout(newCustomer, filter);
    }

    private void initializeGrid(){
        grid = new CustomerGrid();
        grid.asSingleSelect().addValueChangeListener(
                event -> viewLogic.rowSelected(event.getValue()));
        grid.setDataProvider(dataProvider);
        gridType = grid;
    }

    private void createNewMovieButton(){
        newCustomer = Helper.CreateButtonWithText("New Customer");
        newCustomer.addClickListener(click -> viewLogic.newCustomer());
        newEntityButton = newCustomer;
    }

    private void createFilterTextField(){
        filter = Helper.CreateTextFieldWithPlaceholder("Filter by customer name");
        filter.addValueChangeListener(event -> {
            // TODO: Implement filtering by customer name
        });
    }

    @Override
    public void editEntity(Customer entity) {
        showForm(entity != null);
        form.editCustomer(entity);
    }

    @Override
    public void showForm(boolean show) {
        form.setVisible(show);
    }

    @Override
    public void setEntities(List<Customer> entities) {
        dataProvider.getItems().clear();
        dataProvider.getItems().addAll(entities);
        dataProvider.refreshAll();
    }

    @Override
    public void setParameter(BeforeEvent event,
                             @OptionalParameter String parameter) {
        viewLogic.enter(parameter);
    }
}
