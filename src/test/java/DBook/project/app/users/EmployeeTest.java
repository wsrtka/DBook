package DBook.project.app.users;

import DBook.project.app.DBookApplication;
import DBook.project.app.book.Book;
import DBook.project.app.book.BookState;
import DBook.project.app.offers.Invoice;
import DBook.project.app.offers.Offer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;

import java.util.ArrayList;
import java.util.List;

public class EmployeeTest {
    private DBookApplication dbApp = new DBookApplication();
    private Employee employee = new Employee(this.dbApp, "Piotr Dariusz", "Świderski", "pds@falelo.pl");

    @Test
    public void addToDBTest(){

        try(Session s = dbApp.getDriver().session(SessionConfig.builder().withDefaultAccessMode(AccessMode.WRITE).build())) {
            s.writeTransaction(employee::addToDB);
        }

    }

    @Test
    public void deleteUnacceptedOfferTest(){
        //given
        Book book1 = new Book("Stokrotka", 10);
        Book book2 = new Book("Hejhop", 40);
        Book book3 = new Book("Opa", 15);
        ArrayList<Book> offer1Books = new ArrayList<>();
        ArrayList<Book> offer2Books = new ArrayList<>();
        User user1 = new User("piotr", "zale", "ae@am.pl");
        User user2 = new User("qwert", "zasd", "ze@vc.pl");
        dbApp.getUserArrayList().add(user1);
        dbApp.getUserArrayList().add(user2);

        offer1Books.add(book1);
        offer2Books.add(book2);
        offer2Books.add(book3);

        //when
        ArrayList<Book> offer2AcceptedBooks = new ArrayList<>();
        offer2AcceptedBooks.add(book2);

        try(Session s = dbApp.getDriver().session(SessionConfig.builder().withDefaultAccessMode(AccessMode.WRITE).build())) {
            s.writeTransaction(
                    tx -> {
                        user1.addOffer(offer1Books, tx);
                        user2.addOffer(offer2Books, tx);
                        this.employee.deleteUnacceptedOffers(tx);
                        return 0;
                    }
            );
        }
        //then
        try(Session s = dbApp.getDriver().session(SessionConfig.builder().withDefaultAccessMode(AccessMode.WRITE).build())) {
            s.writeTransaction(
                    tx -> {
                        Assertions.assertEquals(0, user2.getUsersOffers(tx).values().size());
                        Assertions.assertEquals(0, user1.getUsersOffers(tx).values().size());
                        return 0;
                    }
            );
        }
    }

    @Test
    public void acceptInvoiceTest(){
        //given
        Book book = new Book("Stokrotka", 10);
        ArrayList<Book> offerBooks = new ArrayList<>();
        User user1 = new User("piotr", "zale", "ae@am.pl");
        User user2 = new User("qwert", "zasd", "ze@vc.pl");
        offerBooks.add(book);

        //when

        try(Session s = dbApp.getDriver().session(SessionConfig.builder().withDefaultAccessMode(AccessMode.WRITE).build())) {
            s.writeTransaction(
                    tx -> {
                        user1.addOffer(offerBooks, tx);
                        List<Offer> user1Offers = new ArrayList<>(user1.getUsersOffers(tx).values());
                        Assertions.assertEquals(1, user1Offers.size());
                        Offer offer1 = user1Offers.get(0);
                        employee.acceptOffer(offer1, offerBooks, user1.getUserID(),tx);
                        ArrayList<Book> user2Invoice = new ArrayList<>();
                        user2Invoice.add(book);
                        user2.addInvoice(user2Invoice, tx);
                        return 0;
                    }
            );
        }
        //then
        try(Session s = dbApp.getDriver().session(SessionConfig.builder().withDefaultAccessMode(AccessMode.WRITE).build())) {
            s.writeTransaction(
                    tx -> {
                        ArrayList<Invoice> user2Invoices = new ArrayList<>(user2.getUsersInvoices(tx).values());
                        ArrayList<Book> acceptedBooks = new ArrayList<>();
                        acceptedBooks.add(book);
                        Invoice invoice = user2Invoices.get(0);
                        employee.acceptInvoice(invoice, acceptedBooks, user2.getUserID(), tx);
                        Assertions.assertEquals(BookState.CLAIMED, book.getState());
                        Assertions.assertTrue(invoice.isAccepted());
                        return 0;
                    }
            );
        }
    }

    @Test
    public void calculatingInvoiceTest(){

    }

    @Test
    public void calculatingOfferTest(){

    }

    @Test
    public void listSomeoneInvoicesTest(){

    }

    @Test
    public void listSomeoneOffersTest(){

    }

    @Test
    public void listBooksToReturnTest(){

    }

    @Test
    public void deleteUnacceptedBooksTest(){
        //given
        Book book1 = new Book("Stokrotka", 10);
        Book book2 = new Book("Hejhop", 40);
        Book book3 = new Book("Opa", 15);
        ArrayList<Book> offer1Books = new ArrayList<>();
        ArrayList<Book> offer2Books = new ArrayList<>();
        User user1 = new User("piotr", "zale", "ae@am.pl");
        User user2 = new User("qwert", "zasd", "ze@vc.pl");
        offer1Books.add(book1);
        offer2Books.add(book2);
        offer2Books.add(book3);

        //when
        ArrayList<Book> offer2AcceptedBooks = new ArrayList<>();
        offer2AcceptedBooks.add(book2);

        try(Session s = dbApp.getDriver().session(SessionConfig.builder().withDefaultAccessMode(AccessMode.WRITE).build())) {
            s.writeTransaction(
                    tx -> {
                        user1.addOffer(offer1Books, tx);
                        user2.addOffer(offer2Books, tx);
                        Assertions.assertEquals(1, user1.getUsersOffers(tx).size());

                        List<Offer> user1Offers = new ArrayList<>(user1.getUsersOffers(tx).values());
                        List<Offer> user2Offers = new ArrayList<>(user2.getUsersOffers(tx).values());
                        Assertions.assertEquals(1, user1Offers.size());
                        Offer offer1 = user1Offers.get(0);
                        Offer offer2 = user2Offers.get(0);
                        Assertions.assertEquals(1, offer1.getBooks().size());
                        Assertions.assertEquals(0, offer1.getOfferID());
                        employee.acceptOffer(offer1, offer1Books, user1.getUserID(),tx);
                        employee.acceptOffer(offer2, offer2AcceptedBooks, user2.getUserID(),tx);
                        return 0;
                    }
            );
        }
        //then
        try(Session s = dbApp.getDriver().session(SessionConfig.builder().withDefaultAccessMode(AccessMode.WRITE).build())) {
            s.writeTransaction(
                    tx -> {
                        List user2DiffOffers = new ArrayList<>(user2.getUsersOffers(tx).values());
                        Offer resultOffer = (Offer) user2DiffOffers.get(0);
                        Assertions.assertEquals(1, resultOffer.getBooks().size());
                        Assertions.assertTrue(resultOffer.getBooks().containsValue(book2));
                        return 0;
                    }
            );
        }
    }

}
