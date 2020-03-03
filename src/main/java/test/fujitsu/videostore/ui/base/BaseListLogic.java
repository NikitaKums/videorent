package test.fujitsu.videostore.ui.base;

public interface BaseListLogic<T> {
    void init();
    void cancel();
    void enter(String id);
    T findEntity(int id);
    void save(T entity);
    void delete(T entity);
    void edit(T entity);
    void createNew();
    void rowSelected(T entity);
}
