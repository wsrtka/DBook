package DBook.project.app;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

public class DBDriver implements AutoCloseable{

    private static DBDriver dbdriver;
    private static Driver driver;

    private DBDriver(String uri, String user, String password){
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    public Driver getDriver(String uri, String user, String password){
        if(driver == null){
            dbdriver = new DBDriver(uri, user, password);
        }
        return driver;
    }

    @Override
    public void close() throws Exception {
        driver.close();
    }
}
