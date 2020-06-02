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

public class Invoice implements Transactionable {

    private Integer invoiceID;

    private static IdGenerator idGen = new IdGenerator();

    private HashMap<Integer, Book> books;

    private Map<String, Object> params;

    public Invoice(ArrayList<Book> books){

        this.invoiceID = idGen.getNextID();
        this.books = new HashMap<>();
        for(Book book : books){
            this.books.put(book.getBookID(), book);
        }

        this.params = new HashMap<>();
        this.params.put("InvoiceID", this.invoiceID);

    }

    public Float calculateInvoice(Transaction txt){
        Money result = new Money();
        this.books.forEach((k, v) ->result.add(Float.parseFloat(v.getFromDB(txt).list().get(1).toString())));
        return result.getValue();
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
