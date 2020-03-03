package test.fujitsu.videostore.ui.base;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;

import java.util.ArrayList;

public abstract class BaseTableDisplayImpl<DomainType, GridType extends Grid<DomainType>> extends HorizontalLayout
        implements BaseTableDisplay<DomainType> {

    protected GridType gridType;
    protected ListDataProvider<DomainType> dataProvider;
    protected Button newEntityButton;

    public BaseTableDisplayImpl(ListDataProvider<DomainType> dataProvider){
        this.dataProvider = dataProvider;
    }

    public void showSaveNotification(String msg) {
        Notification.show(msg);
    }

    public void setNewEntityEnabled(boolean enabled) {
        newEntityButton.setEnabled(enabled);
    }

    public void clearSelection() {
        gridType.getSelectionModel().deselectAll();
    }

    public void selectRow(DomainType row) {
        gridType.getSelectionModel().select(row);
    }

    public void addEntity(DomainType entity) {
        dataProvider.getItems().add(entity);
        gridType.getDataProvider().refreshAll();
    }

    public void updateEntity(DomainType entity) {
        dataProvider.refreshItem(entity);
    }

    public void removeEntity(DomainType entity) {
        dataProvider.getItems().remove(entity);
        dataProvider.refreshAll();
    }

}
