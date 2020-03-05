package test.fujitsu.videostore.ui.customer;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import test.fujitsu.videostore.backend.domain.Customer;
import test.fujitsu.videostore.ui.MainLayout;
import test.fujitsu.videostore.ui.base.BaseListViewImpl;
import test.fujitsu.videostore.ui.customer.components.CustomerForm;
import test.fujitsu.videostore.ui.customer.components.CustomerGrid;
import test.fujitsu.videostore.ui.helpers.Helper;

import java.util.ArrayList;
import java.util.List;

@Route(value = CustomerList.VIEW_NAME, layout = MainLayout.class)
public class CustomerList extends BaseListViewImpl<Customer, CustomerGrid, CustomerForm> implements HasUrlParameter<String> {

    public static final String VIEW_NAME = "CustomerList";

    private CustomerListLogic viewLogic = new CustomerListLogic(this);

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
        createNewCustomerButton();
        return Helper.CreateHorizontalLayout(newEntityButton, filter);
    }

    private void initializeGrid(){
        grid = new CustomerGrid();
        grid.asSingleSelect().addValueChangeListener(
                event -> viewLogic.rowSelected(event.getValue()));
        grid.setDataProvider(dataProvider);
    }

    private void createNewCustomerButton(){
        newEntityButton = Helper.CreateButtonWithText("new-item", "New Customer");
        newEntityButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newEntityButton.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        newEntityButton.addClickListener(click -> viewLogic.newEntity());
    }

    private void createFilterTextField(){
        filter = Helper.CreateTextFieldWithPlaceholder("filter", "Filter by customer name", ValueChangeMode.EAGER);
        filter.addValueChangeListener(event -> {
            clearFilters();
            String value = event.getValue();
            if (!Helper.IsStringEmptyOrWhitespace(value)) {
                dataProvider.addFilter(item -> item.getName().toLowerCase().contains(value.toLowerCase()));
            } else {
                clearFilters();
            }
        });
    }

    @Override
    public void editEntity(Customer entity) {
        showForm(entity != null);
        form.editEntity(entity);
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
