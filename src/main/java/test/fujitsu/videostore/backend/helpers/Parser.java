package test.fujitsu.videostore.backend.helpers;

import java.util.List;

public interface Parser<T> {
    List<T> getAll();
    void saveAll(List<T> data);
    void instantiateParser();
}
