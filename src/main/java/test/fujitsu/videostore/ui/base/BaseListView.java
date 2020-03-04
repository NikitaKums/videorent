package test.fujitsu.videostore.ui.base;

import java.util.List;

public interface BaseListView<DomainType> {

    void showSaveNotification(String msg);

    void setNewEntityEnabled(boolean enabled);

    void clearSelection();

    void selectRow(DomainType row);

    void addEntity(DomainType entity);

    void updateEntity(DomainType entity);

    void removeEntity(DomainType entity);

    void editEntity(DomainType entity);

    void showForm(boolean show);

    void setEntities(List<DomainType> entities);
}
