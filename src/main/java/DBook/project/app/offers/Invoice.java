package DBook.project.app.offers;

import DBook.project.app.IdGenerator;
import DBook.project.app.Transactionable;
import DBook.project.app.book.Book;
import org.neo4j.driver.Result;
import org.neo4j.driver.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.neo4j.driver.Values.parameters;

public class Invoice implements Transactionable {

    private Integer invoiceID;

    private static IdGenerator idGen = new IdGenerator();

    private HashMap<Integer, Book> books;

    private Map<String, Object> params;

    public Invoice(ArrayList<Book> books, Transaction tx){

        this.invoiceID = idGen.getNextID();

        for(Book book : books){
            this.addBook(book, tx);
        }

        this.params = new HashMap<>();
        this.params.put("InvoiceID", this.invoiceID);

    }

    public Float calculateInvoice(){
        Float sum = new Float(0);

        for(Book book : this.books.values()){
            sum = sum + book.getPrice();
        }

        return sum;
    }

    private Result addBook(Book b, Transaction tx){

        this.books.put(b.getBookID(), b);

        Result res = b.getFromDB(tx);

        if(!res.hasNext()){
            b.addToDB(tx);
        }

        String query = "MATCH (i: Invoice {invoiceID: $invoiceID}), " +
                "(b: Book {bookID: $bookID}) " +
                "CREATE (i)-[:HAS_A]->(b)";

        return tx.run(query, parameters("invoiceID", this.invoiceID, "bookID", b.getBookID()));

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

        String query = "MATCH (i: Invoice {i.invoiceID: $invoiceID}) " +
                "RETURN i";

        return tx.run(query, this.params);

    }

    @Override
    public Result update(Transaction tx)git  {
        return null;
    }

    @Override
    public void updateParams() {

    }

    public Integer getInvoiceID() {
        return invoiceID;
    }
}
