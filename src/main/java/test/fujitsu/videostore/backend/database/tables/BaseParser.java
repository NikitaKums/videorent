package test.fujitsu.videostore.backend.database.tables;

import java.util.List;

public interface BaseParser<T> {
    List<T> getAll();
    void saveAll(List<T> data);
    void instantiateParser();
}
