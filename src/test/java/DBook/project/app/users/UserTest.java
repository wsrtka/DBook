package DBook.project.app.users;

import DBook.project.app.DBookApplication;
import DBook.project.app.book.Book;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;
import org.neo4j.driver.summary.ResultSummary;

public class UserTest {

    private User user = new User("Piotr", "Załęski", "pzal@falelo.pl");
    private DBookApplication dbApp = new DBookApplication();

    @Test
    public void dbAddTest(){

        try(Session s = dbApp.getDriver().session(SessionConfig.builder().withDefaultAccessMode(AccessMode.WRITE).build())) {
            s.writeTransaction(user::addToDB);
        }

    }

    @Test
    public void dbGetTest(){

        try(Session s = dbApp.getDriver().session(SessionConfig.builder().withDefaultAccessMode(AccessMode.WRITE).build())) {
            User u = s.readTransaction(tx -> {
                Result res = this.user.getFromDB(tx);
                if(res.hasNext()){
                    return this.user.mapResult(res.next());
                }
                else{
                    return null;
                }
            });
            System.out.println(u.getName());
        }

    }

    @Test
    public void dbUpdateTest(){

//        wszystkie pola i tak są ostateczne
        try(Session s = dbApp.getDriver().session(SessionConfig.builder().withDefaultAccessMode(AccessMode.WRITE).build())) {
            s.writeTransaction(user::update);
        }

    }

    @Test
    public void dbRemoveTest(){

        try(Session s = dbApp.getDriver().session(SessionConfig.builder().withDefaultAccessMode(AccessMode.WRITE).build())) {
            s.writeTransaction(user::removeFromDB);
        }

    }

}

