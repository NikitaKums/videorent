package test.fujitsu.videostore.ui.base;

public interface BaseListLogic<T> {
    void init();
    void cancel();
    void setFragmentParameter(String id);
    void enter(String id);
    T findEntity(int id);
    void saveEntity(T entity);
    void deleteEntity(T entity);
    void editEntity(T entity);
    void newEntity();
    void rowSelected(T entity);
}
