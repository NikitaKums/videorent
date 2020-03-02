package test.fujitsu.videostore.backend.database.tables;

import test.fujitsu.videostore.backend.database.DBTableRepository;

import java.util.List;

public interface BaseRepository<T> {
    List<T> getAll();
    boolean remove(T object);
    void saveChanges();
}
