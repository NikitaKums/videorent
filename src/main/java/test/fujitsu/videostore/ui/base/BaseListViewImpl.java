package test.fujitsu.videostore.ui.base;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;

public abstract class BaseListViewImpl<DomainType, GridType extends Grid<DomainType>, FormType extends BaseForm<DomainType>> extends HorizontalLayout
        implements BaseListView<DomainType> {

    protected GridType grid;
    protected FormType form;
    protected TextField filter;
    protected ListDataProvider<DomainType> dataProvider;
    protected Button newEntityButton;

    public BaseListViewImpl(ListDataProvider<DomainType> dataProvider){
        this.dataProvider = dataProvider;
    }

    public void showSaveNotification(String msg) {
        Notification.show(msg);
    }

    public void setNewEntityEnabled(boolean enabled) {
        newEntityButton.setEnabled(enabled);
    }

    public void clearSelection() {
        grid.getSelectionModel().deselectAll();
    }

    public void selectRow(DomainType row) {
        grid.getSelectionModel().select(row);
    }

    public void addEntity(DomainType entity) {
        dataProvider.getItems().add(entity);
        grid.getDataProvider().refreshAll();
    }

    public void updateEntity(DomainType entity) {
        dataProvider.refreshItem(entity);
    }

    public void removeEntity(DomainType entity) {
        dataProvider.getItems().remove(entity);
        dataProvider.refreshAll();
    }

    protected void clearFilters(){
        dataProvider.clearFilters();
    }

}
