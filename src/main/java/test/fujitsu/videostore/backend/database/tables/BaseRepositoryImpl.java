package test.fujitsu.videostore.backend.database.tables;

import test.fujitsu.videostore.backend.helpers.Parser;

import java.util.List;

public abstract class BaseRepositoryImpl<T> implements BaseRepository<T> {

    protected List<T> list;
    private Parser<T> parser;

    public BaseRepositoryImpl(Parser<T> parser){
        this.parser = parser;
        this.list = this.parser.getAll();
    }

    @Override
    public List<T> getAll() {
        return list;
    }

    @Override
    public boolean remove(T object) {
        boolean result = list.remove(object);
        saveChanges();
        return result;
    }

    @Override
    public void saveChanges() {
        parser.saveAll(list);
    }
}
