package fujitsu.videostore.backend.database;

public class DatabaseTestsYaml extends DatabaseTestsBase {

    private static final String DATABASE_PATH = "src/test/resources/testsDatabase.yaml";

    @Override
    public String getDatabasePath() {
        return DATABASE_PATH;
    }
}
