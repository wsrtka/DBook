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

import java.util.ArrayList;
import java.util.Arrays;

public class OfferTest {

    private ArrayList<Book> books = new ArrayList<>();
    private Offer offer;
    private DBookApplication dbApp = new DBookApplication();

    public OfferTest(){

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
                        this.offer = new Offer(this.books, tx);
                        return 0;
                    }
            );
        }

    }

    @Test
    public void dbAddTest(){

        try(Session s = dbApp.getDriver().session(SessionConfig.builder().withDefaultAccessMode(AccessMode.WRITE).build())) {
            s.writeTransaction(offer::addToDB);
        }

    }

    @Test
    public void dbGetTest(){

        try(Session s = dbApp.getDriver().session(SessionConfig.builder().withDefaultAccessMode(AccessMode.WRITE).build())) {
            Offer newOffer = s.readTransaction(tx -> {
                Result res = this.offer.getFromDB(tx);
                if(res.hasNext()){
                    return this.offer.mapResult(res.next());
                }
                else{
                    return null;
                }
            });
            System.out.println(newOffer.getOfferID());
        }

    }

    @Test
    public void dbUpdateTest(){

        try(Session s = dbApp.getDriver().session(SessionConfig.builder().withDefaultAccessMode(AccessMode.WRITE).build())) {
            s.writeTransaction(
                    tx -> {
                        this.offer.addBook(new Book("Historia filozofii cz.1", 56), tx);
                        this.offer.acceptOffer();
                        this.offer.update(tx);
                        return 0;
                    }
            );
        }

    }

    @Test
    public void dbRemoveTest(){

        try(Session s = dbApp.getDriver().session(SessionConfig.builder().withDefaultAccessMode(AccessMode.WRITE).build())) {
            s.writeTransaction(offer::removeFromDB);
        }

    }

    @Test
    public void addBookTest(){
        // given
        Book book1 = new Book("Stokrotka", 10);
        Book book2 = new Book("Hejhop", 40);
        Book book3 = new Book("Opa", 15);
        ArrayList<Book> offerBooks = new ArrayList<>();
        offerBooks.add(book1);
        offerBooks.add(book2);
        offerBooks.add(book3);

        try(Session s = dbApp.getDriver().session(SessionConfig.builder().withDefaultAccessMode(AccessMode.WRITE).build())) {
            s.writeTransaction(
                    tx -> {
                        Offer offer = new Offer(offerBooks,tx);

                        Assertions.assertEquals(3, offer.getBooks().size());
                        return 0;
                    }
            );
        }
    }

}
