package DBook.project.app.offers;

import DBook.project.app.Transactionable;
import DBook.project.app.book.Book;
import org.neo4j.driver.Transaction;

import java.util.ArrayList;
import java.util.HashMap;

public class Invoice implements Transactionable {
    private Integer invoiceID;
    private HashMap<Integer, Book> books;


    public Invoice(Integer invoiceID, ArrayList<Book> books){
        this.invoiceID = invoiceID;
        for(Book book : books){ // dodajemy do listy każdą książkę
            this.books.put(book.getBookID(), book);
        }
    }

    public Integer calculateInvoice(){
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
