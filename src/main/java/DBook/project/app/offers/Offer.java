package DBook.project.app.offers;

import DBook.project.app.Transactionable;
import DBook.project.app.book.Book;
import org.neo4j.driver.Transaction;

import java.util.ArrayList;
import java.util.HashMap;

public class Offer implements Transactionable {
    private Integer offerID;
    private HashMap<Integer, Book> books;

    public Offer(Integer offerID, ArrayList<Book> books){
        this.offerID = offerID;
        for(Book book : books){ // dodajemy do listy każdą książkę
            this.books.put(book.getBookID(), book);
        }
    }

    public ArrayList<Book> getUnsoldBooks(){
        // tu trzeba zrobić zapytanie do bazy
        return null;
    }

    public Integer calculateOfferRevenue(){
        //tu trzeba zrobić zapytanie do bazy
        return null;
    }

    @Override
    public void addToDB(Transaction tx) {

    }

    @Override
    public void removeFromDB(Transaction tx) {

    }

    @Override
    public void getFromDB(Transaction tx) {

    }

    @Override
    public void update(Transaction tx) {

    }
}
