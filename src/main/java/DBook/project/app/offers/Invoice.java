package DBook.project.app.offers;

import DBook.project.app.IdGenerator;
import DBook.project.app.Transactionable;
import DBook.project.app.book.Book;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import org.neo4j.driver.exceptions.NoSuchRecordException;
import org.neo4j.driver.summary.ResultSummary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.neo4j.driver.Values.parameters;

public class Invoice implements Transactionable {

    private Integer invoiceID;

    private static IdGenerator idGen = new IdGenerator();

    private HashMap<Integer, Book> books;

    private Map<String, Object> params;

    private boolean accepted;

    public Invoice(ArrayList<Book> books, Transaction tx){

        this.invoiceID = idGen.getNextID();
        this.books = new HashMap<>();
        for(Book book : books){
            this.addBook(book, tx);
        }
        this.accepted = false;
        this.params = new HashMap<>();
        this.params.put("InvoiceID", this.invoiceID);

    }

    public Double calculateInvoice(){
        Money result = new Money();
        this.books.forEach((k, v) ->result.add(v.getPrice()));
        return result.getValue();
    }
    public Double calculateInvoice(Transaction txt){
        Money result = new Money();
        this.books.forEach((k, v) ->result.add(Double.parseDouble(v.getFromDB(txt).list().get(1).toString())));
        return result.getValue();
    }

    public HashMap<Integer, Book> getInvoiceBooks(){
        return  this.books;
    }

    private Result addBook(Book b, Transaction tx) {

        this.books.put(b.getBookID(), b);

        Result res = b.getFromDB(tx);

        if (!res.hasNext()) {
            b.addToDB(tx);
        }

        String query = "MATCH (i: Invoice {invoiceID: $invoiceID}), " +
                "(b: Book {bookID: $bookID}) " +
                "CREATE (i)-[:HAS_A]->(b)";

        return tx.run(query, parameters("invoiceID", this.invoiceID, "bookID", b.getBookID()));
    }
    public void acceptInvoice(){
        this.accepted = true;
    }

    public boolean isAccepted() {
        return this.accepted;
    }

    @Override
    public Result addToDB(Transaction tx) {

        String query = "CREATE (i: Invoice {i.invoiceID: $invoiceID})";

        return tx.run(query, this.params);

    }

    @Override
    public Result removeFromDB(Transaction tx) {

        String query = "MATCH (i: Invoice {i.invoiceID: $invoiceID, accepted: $accepted}}) " +
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
    public Result update(Transaction tx){
        String query = "MATCH (i: Invoice {invoiceID: $invoiceID})" +
                " SET o.accepted = $accepted";
        this.updateParams();
        return tx.run(query, this.params);
    }

    @Override
    public void updateParams() {
        if(this.accepted){
            if(this.params.containsKey("accepted") && !this.params.get("accepted").equals(this.accepted)){
                this.params.replace("accepted", this.accepted);
            }
            else{
                this.params.put("accepted", this.accepted);
            }
        }
    }

    public Integer getInvoiceID() {
        return this.invoiceID;
    }

}
