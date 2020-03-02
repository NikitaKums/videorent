package test.fujitsu.videostore.backend.database.tables.movies;

import test.fujitsu.videostore.backend.database.tables.BaseRepositoryImpl;
import test.fujitsu.videostore.backend.domain.Movie;

import java.util.Collections;
import java.util.Comparator;

public class MovieTableRepositoryImpl extends BaseRepositoryImpl<Movie> implements MovieTableRepository {

    public MovieTableRepositoryImpl(String filePath){
        super(new MovieParser(filePath));
    }

    @Override
    public Movie findById(int id) {
        return list.stream().filter(movie -> movie.getId() == id).findFirst().get();
    }

    @Override
    public Movie createOrUpdate(Movie object) {
        if (object == null) {
            return null;
        }

        if (object.getId() == -1) {
            object.setId(generateNextId());
            list.add(object);

            saveChanges();
            return object;
        }

        Movie movie = findById(object.getId());

        movie.setName(object.getName());
        movie.setStockCount(object.getStockCount());
        movie.setType(object.getType());

        saveChanges();
        return movie;
    }

    @Override
    public int generateNextId() {
        int id = Collections.max(list, Comparator.comparingInt(Movie::getId)).getId();
        if (id == -1){
            id = 0;
        }
        return id + 1;
    }
}
