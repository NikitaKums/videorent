package test.fujitsu.videostore.ui.order;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import test.fujitsu.videostore.backend.domain.RentOrder;
import test.fujitsu.videostore.ui.MainLayout;
import test.fujitsu.videostore.ui.base.BaseListViewImpl;
import test.fujitsu.videostore.ui.helpers.Helper;
import test.fujitsu.videostore.ui.order.components.OrderForm;
import test.fujitsu.videostore.ui.order.components.OrderGrid;

import java.util.ArrayList;
import java.util.List;

@Route(value = OrderList.VIEW_NAME, layout = MainLayout.class)
public class OrderList extends BaseListViewImpl<RentOrder, OrderGrid, OrderForm> implements HasUrlParameter<String>{

    static final String VIEW_NAME = "OrderList";

    private OrderListLogic viewLogic = new OrderListLogic(this);

    public OrderList() {
        super(new ListDataProvider<>(new ArrayList<>()));
        setId(VIEW_NAME);
        setSizeFull();

        HorizontalLayout topLayout = createTopBar();
        initializeGrid();
        form = new OrderForm(viewLogic);

        VerticalLayout barAndGridLayout = Helper.CreateVerticalLayout(topLayout, grid);

        add(barAndGridLayout);
        add(form);
        setFlexGrow(0, barAndGridLayout);
        setFlexGrow(1, form);

        viewLogic.init();
    }

    private HorizontalLayout createTopBar() {
        createFilterTextField();
        createNewOrderButton();
        return Helper.CreateHorizontalLayout(newEntityButton, filter);
    }

    private void initializeGrid(){
        grid = new OrderGrid();
        grid.asSingleSelect().addValueChangeListener(
                event -> viewLogic.rowSelected(event.getValue()));
        grid.setDataProvider(dataProvider);
    }

    private void createNewOrderButton(){
        newEntityButton = Helper.CreateButtonWithText("new-item", "New Order");
        newEntityButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newEntityButton.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        newEntityButton.addClickListener(click -> viewLogic.newEntity());
    }

    private void createFilterTextField(){
        filter = Helper.CreateTextFieldWithPlaceholder("filter", "Filter by ID or Customer name", ValueChangeMode.EAGER);
        filter.addValueChangeListener(event -> {
            //TODO: Implement filtering by id and customer name
        });
    }

    @Override
    public void editEntity(RentOrder entity) {
        showForm(entity != null);
        form.editEntity(entity);
    }

    @Override
    public void showForm(boolean show) {
        form.setVisible(show);
    }

    @Override
    public void setEntities(List<RentOrder> entities) {
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
