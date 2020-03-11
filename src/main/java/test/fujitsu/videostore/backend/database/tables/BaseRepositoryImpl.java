package test.fujitsu.videostore.backend.database.tables;

import java.util.List;

public abstract class BaseRepositoryImpl<T> implements BaseRepository<T> {

    protected List<T> list;
    private BaseParser<T> baseParser;

    public BaseRepositoryImpl(BaseParser<T> baseParser){
        this.baseParser = baseParser;
        this.list = this.baseParser.getAll();
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
        baseParser.saveAll(list);
    }
}
