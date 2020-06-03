package DBook.project.app;

import org.junit.jupiter.api.Test;
import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;

public class IdGeneratorTest {

    private IdGenerator idGen;
    private DBookApplication dbApp = new DBookApplication();

    @Test
    public void idGenTest(){

        try(Session s = dbApp.getDriver().session(SessionConfig.builder().withDefaultAccessMode(AccessMode.WRITE).build())) {
            s.writeTransaction(
                    tx -> {
                        this.idGen = new IdGenerator("User", tx);
                        return 0;
                    }
            );
        }

    }

}
