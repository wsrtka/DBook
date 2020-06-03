package DBook.project.app.offers;

import DBook.project.app.DBookApplication;
import DBook.project.app.book.Book;
import DBook.project.app.users.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class InvoiceTest {

    private ArrayList<Book> books = new ArrayList<>();
    private Invoice invoice;
    private DBookApplication dbApp = new DBookApplication();

    public InvoiceTest(){

        this.books.addAll(
                Arrays.asList(
                        new Book("Proces", 46),
                        new Book("Perfume", 16),
                        new Book("Matematyka nie jest trudna", 35)
                )
        );

        try(Session s = dbApp.getDriver().session(SessionConfig.builder().withDefaultAccessMode(AccessMode.WRITE).build())) {
            s.writeTransaction(
                    tx -> {
                        this.invoice = new Invoice(this.books, tx);
                        return 0;
                    }
            );
        }

    }
    @Test
    public void invoiceValueTest(){

        try(Session s = dbApp.getDriver().session(SessionConfig.builder().withDefaultAccessMode(AccessMode.WRITE).build())) {
            s.writeTransaction(invoice::addToDB);
        }

        //then
        Assertions.assertEquals(3, this.invoice.getInvoiceBooks().values().size());
        Assertions.assertEquals(97, this.invoice.calculateInvoice());
    }
    @Test
    public void dbAddTest(){

        try(Session s = dbApp.getDriver().session(SessionConfig.builder().withDefaultAccessMode(AccessMode.WRITE).build())) {
            s.writeTransaction(invoice::addToDB);
        }

    }

    @Test
    public void dbGetTest(){

        try(Session s = dbApp.getDriver().session(SessionConfig.builder().withDefaultAccessMode(AccessMode.WRITE).build())) {
            Invoice newInvoice = s.readTransaction(tx -> {
                Result res = this.invoice.getFromDB(tx);
                if(res.hasNext()){
                    return this.invoice.mapResult(res.next());
                }
                else{
                    return null;
                }
            });
            System.out.println(newInvoice.getInvoiceID());
        }

    }

    @Test
    public void dbUpdateTest(){

        try(Session s = dbApp.getDriver().session(SessionConfig.builder().withDefaultAccessMode(AccessMode.WRITE).build())) {
            s.writeTransaction(
                    tx -> {
                        this.invoice.addBook(new Book("Historia filozofii cz.1", 56), tx);
                        this.invoice.acceptInvoice();
                        this.invoice.update(tx);
                        return 0;
                    }
            );
        }

    }

    @Test
    public void dbRemoveTest(){

        try(Session s = dbApp.getDriver().session(SessionConfig.builder().withDefaultAccessMode(AccessMode.WRITE).build())) {
            s.writeTransaction(invoice::removeFromDB);
        }

    }

}
