package DBook.project.app.offers;

import DBook.project.app.Transactionable;
import DBook.project.app.book.Book;
import org.neo4j.driver.Result;
import org.neo4j.driver.Transaction;

import java.util.ArrayList;
import java.util.HashMap;

public class Offer implements Transactionable {
    private Integer offerID;
    private HashMap<Integer, Book> books;

    public Offer(ArrayList<Book> books){
        this.offerID = 0;
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
    public Result addToDB(Transaction tx) {
        return null;
    }

    @Override
    public Result removeFromDB(Transaction tx) {
        return null;
    }

    @Override
    public Result getFromDB(Transaction tx) {
        return null;
    }

    @Override
    public Result update(Transaction tx) {
        return null;
    }

    @Override
    public void updateParams() {

    }

    public Integer getOfferID() {
        return offerID;
    }
}
