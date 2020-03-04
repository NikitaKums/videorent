package test.fujitsu.videostore.ui.order;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
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
    private Button newOrder;

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
        createNewMovieButton();
        return Helper.CreateHorizontalLayout(newOrder, filter);
    }

    private void initializeGrid(){
        grid = new OrderGrid();
        grid.asSingleSelect().addValueChangeListener(
                event -> viewLogic.rowSelected(event.getValue()));
        grid.setDataProvider(dataProvider);
    }

    private void createNewMovieButton(){
        newOrder = Helper.CreateButtonWithText("New Order");
        newOrder.addClickListener(click -> viewLogic.newEntity());
        newEntityButton = newOrder;
    }

    private void createFilterTextField(){
        filter = Helper.CreateTextFieldWithPlaceholder("Filter by ID or Customer name");
        filter.addValueChangeListener(event -> {
            //TODO: Implement filtering by id and customer name
        });
    }

    @Override
    public void editEntity(RentOrder entity) {
        showForm(entity != null);
        form.editOrder(entity);
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
