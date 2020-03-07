package test.fujitsu.videostore.backend.database.tables.movies;

import test.fujitsu.videostore.backend.database.tables.BaseParser;
import test.fujitsu.videostore.backend.domain.Movie;
import test.fujitsu.videostore.backend.helpers.DatabaseObject;
import test.fujitsu.videostore.backend.helpers.Parser;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MovieParser extends BaseParser implements Parser<Movie> {

    private DatabaseObject databaseObject;

    public MovieParser(String filePath){
        super(filePath);
        this.filePath = filePath;
    }

    @Override
    public List<Movie> getAll() {
        try {
            databaseObject = objectMapper.readValue(new File(filePath), DatabaseObject.class);
            return databaseObject.getMovie();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveAll(List<Movie> data) {
        getAll();
        databaseObject.setMovie(data);
        try {
            objectMapper.writeValue(new File(filePath), databaseObject);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
