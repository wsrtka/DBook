package DBook.project.app.users;

import DBook.project.app.DBookApplication;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;

public class EmployeeTest {

    private Employee employee = new Employee(new DBookApplication(), "Piotr Dariusz", "Świderski", "pds@falelo.pl");
    private DBookApplication dbApp = new DBookApplication();

    @Test
    public void addToDBTest(){

        try(Session s = dbApp.getDriver().session(SessionConfig.builder().withDefaultAccessMode(AccessMode.WRITE).build())) {
            s.writeTransaction(employee::addToDB);
        }

    }

}
