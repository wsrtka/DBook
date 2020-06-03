package DBook.project.app.book;

import DBook.project.app.DBookApplication;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;
import org.neo4j.driver.summary.ResultSummary;
import org.neo4j.driver.util.Pair;

import static org.junit.jupiter.api.Assertions.*;

public class BookTest {

    private Book book = new Book("Tytus, Romek i Atomek",   new Double(29.99));
    private DBookApplication dbApp = new DBookApplication();

    @Test
    public void stateTest(){

        assertNotEquals(this.book.getState(), BookState.CLAIMED);
        assertEquals(this.book.getState(), BookState.AVAILABLE);

    }

    @Test
    public void dbAddTest(){

        try(Session s = dbApp.getDriver().session(SessionConfig.builder().withDefaultAccessMode(AccessMode.WRITE).build())) {
            s.writeTransaction(book::addToDB);
        }

    }

    @Test
    public void dbGetTest(){

        try(Session s = dbApp.getDriver().session(SessionConfig.builder().withDefaultAccessMode(AccessMode.WRITE).build())) {
            Book b = s.readTransaction(
                    tx -> {
                        Result res = this.book.getFromDB(tx);
                        if(res.hasNext()){
                            Book nb = this.book.mapResult(res.next());
                            System.out.println("has next");
                            return nb;
                        }
                        else{
                            System.out.println("not has next");
                            return null;
                        }
                    }
            );
            System.out.println(b.getPrice());
        }

    }

    @Test
    public void dbUpdateTest(){

        try(Session s = dbApp.getDriver().session(SessionConfig.builder().withDefaultAccessMode(AccessMode.WRITE).build())) {
            this.book.setAuthor("Henryk Jerzy Chmielecki");
            ResultSummary summary = s.writeTransaction(book::update).consume();
            System.out.println(summary.toString());
        }

    }

    @Test
    public void dbRemoveTest(){

        try(Session s = dbApp.getDriver().session(SessionConfig.builder().withDefaultAccessMode(AccessMode.WRITE).build())) {
            s.writeTransaction(book::removeFromDB);
        }

    }

}
