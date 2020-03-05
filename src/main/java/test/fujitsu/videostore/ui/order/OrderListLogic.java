package test.fujitsu.videostore.ui.order;

import com.vaadin.flow.component.UI;
import test.fujitsu.videostore.backend.database.DBTableRepository;
import test.fujitsu.videostore.backend.domain.RentOrder;
import test.fujitsu.videostore.backend.reciept.OrderToReceiptService;
import test.fujitsu.videostore.ui.base.BaseListLogicImpl;
import test.fujitsu.videostore.ui.database.CurrentDatabase;

public class OrderListLogic extends BaseListLogicImpl<RentOrder, OrderList> {

    private OrderToReceiptService orderToReceiptService;

    public OrderListLogic(OrderList orderList) {
        super(orderList);
        orderToReceiptService = new OrderToReceiptService();
    }

    @Override
    public void init() {
        if (CurrentDatabase.get() == null) {
            return;
        }

        dBTableRepository = CurrentDatabase.get().getOrderTable();
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

        UI.getCurrent().navigate(OrderList.class, fragmentParameter);
    }

    @Override
    public void saveEntity(RentOrder order) {
        boolean isNew = order.isNewObject();

        RentOrder updatedObject = dBTableRepository.createOrUpdate(order);

        if (isNew) {
            view.addEntity(updatedObject);
        } else {
            view.updateEntity(order);
        }

        view.clearSelection();
        setFragmentParameter("");
        view.showSaveNotification(order.getId() + (isNew ? " created" : " updated"));
    }

    @Override
    public void deleteEntity(RentOrder order) {
        dBTableRepository.remove(order);

        view.clearSelection();
        view.removeEntity(order);
        setFragmentParameter("");
        view.showSaveNotification(order.getId() + " removed");
    }

    @Override
    public void editEntity(RentOrder order) {
        if (order == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(order.getId() + "");
        }
        view.editEntity(order);
    }

    @Override
    public void newEntity() {
        view.clearSelection();
        setFragmentParameter("new");
        view.editEntity(new RentOrder());
    }


    public OrderToReceiptService getOrderToReceiptService() {
        return orderToReceiptService;
    }

    public DBTableRepository<RentOrder> getRepository() {
        return dBTableRepository;
    }
}
