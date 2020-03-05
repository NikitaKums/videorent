package fujitsu.videostore.backend.database;

public class DatabaseTestsJson extends DatabaseTestsBase {

    private static final String DATABASE_PATH = "src/test/resources/testsDatabase.json";

    @Override
    public String getDatabasePath() {
        return DATABASE_PATH;
    }
}
