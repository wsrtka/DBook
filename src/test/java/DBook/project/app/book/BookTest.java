package DBook.project.app.book;

import DBook.project.app.DBookApplication;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;
import org.neo4j.driver.summary.ResultSummary;

import static org.junit.jupiter.api.Assertions.*;

public class BookTest {

    private Book book = new Book("Tytus, Romek i Atomek",   29.99f);
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
            s.readTransaction(book::getFromDB);
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
