package DBook.project.app.offers;

import DBook.project.app.IdGenerator;
import DBook.project.app.Transactionable;
import DBook.project.app.book.Book;
import org.neo4j.driver.Result;
import org.neo4j.driver.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Invoice implements Transactionable {

    private Integer invoiceID;

    private static IdGenerator idGen = new IdGenerator();

    private HashMap<Integer, Book> books;

    private Map<String, Object> params;

    public Invoice(ArrayList<Book> books){

        this.invoiceID = idGen.getNextID();

        for(Book book : books){
            this.books.put(book.getBookID(), book);
        }

        this.params = new HashMap<>();
        this.params.put("InvoiceID", this.invoiceID);

    }

    public Integer calculateInvoice(){
        return null;
    }

    @Override
    public Result addToDB(Transaction tx) {

        String query = "CREATE (i: Invoice {i.invoiceID: $invoiceID})";

        return tx.run(query, this.params);

    }

    @Override
    public Result removeFromDB(Transaction tx) {

        String query = "MATCH (i: Invoice {i.invoiceID: $invoiceID}) " +
                "DELETE i";

        return tx.run(query, this.params);

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

    public Integer getInvoiceID() {
        return invoiceID;
    }
}
