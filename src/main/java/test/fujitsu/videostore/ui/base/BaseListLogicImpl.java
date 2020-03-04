package test.fujitsu.videostore.ui.base;

import test.fujitsu.videostore.backend.database.DBTableRepository;

public abstract class BaseListLogicImpl<DomainType, ListT extends BaseListView<DomainType>> implements BaseListLogic<DomainType> {

    protected ListT view;
    protected DBTableRepository<DomainType> dBTableRepository;

    public BaseListLogicImpl(ListT view){
        this.view = view;
    }

    // init


    @Override
    public void cancel() {
        setFragmentParameter("");
        view.clearSelection();
    }

    // set fragment parameter


    @Override
    public void enter(String id) {
        if (id != null && !id.isEmpty()) {
            if (id.equals("new")) {
                newEntity();
            } else {
                int pid = Integer.parseInt(id);
                DomainType entity = findEntity(pid);
                view.selectRow(entity);
            }
        } else {
            view.showForm(false);
        }
    }

    @Override
    public DomainType findEntity(int id) {
        return dBTableRepository.findById(id);
    }

    // save customer

    // delete

    // edit

    // new


    @Override
    public void rowSelected(DomainType entity) {
        if (entity != null){
            editEntity(entity);
        }
    }
}
