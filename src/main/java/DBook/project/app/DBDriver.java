package DBook.project.app;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

public class DBDriver implements AutoCloseable{

    private static Driver driver;

    public DBDriver(String uri, String user, String password){
        if(driver == null) {
            driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
        }
    }

    public Driver getDriver(){
        return driver;
    }

    @Override
    public void close() throws Exception {
        driver.close();
    }
}
