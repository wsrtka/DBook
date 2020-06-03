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

    private static final IdGenerator idGen = new IdGenerator();

    private HashMap<Integer, Book> books;

    private Map<String, Object> params;

    private boolean accepted;

    private Invoice(){

        this.invoiceID = idGen.getNextID();

        this.accepted = false;
        this.params = new HashMap<>();

        this.params.put("invoiceID", this.invoiceID);
        this.params.put("accepted", this.accepted);

    }

    public Invoice(ArrayList<Book> books, Transaction tx){

        this();

        this.addToDB(tx);

        this.books = new HashMap<>();
        for(Book book : books){
            this.addBook(book, tx);
        }

    }

    public Integer calculateInvoice(){
        Money result = new Money();
        this.books.forEach((k, v) ->result.add(v.getPrice()));

        return result.getValue();

    }



    public HashMap<Integer, Book> getInvoiceBooks(){
        return this.books;
    }

    public Result addBook(Book b, Transaction tx) {

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

        String query = "CREATE (i: Invoice {invoiceID: $invoiceID, accepted: $accepted})";

        return tx.run(query, this.params);

    }

    @Override
    public Result removeFromDB(Transaction tx) {

        String query = "MATCH (i: Invoice {invoiceID: $invoiceID}) " +
                "DETACH DELETE i";

        return tx.run(query, this.params);

    }

    @Override
    public Result getFromDB(Transaction tx) {

        String query = "MATCH (i: Invoice {invoiceID: $invoiceID, accepted: $accepted}) " +
                "RETURN i";

        return tx.run(query, this.params);

    }

    @Override
    public Result update(Transaction tx){

        String query = "MATCH (i: Invoice {invoiceID: $invoiceID})" +
                " SET i.accepted = $accepted";

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

    public Invoice mapResult(Record rec){

        Map<String, Object> recMap = rec.get(0).asMap();
        Invoice i;

        if(recMap.containsKey("invoiceID")){
            i = new Invoice();
            i.setInvoiceID(((Long) recMap.get("invoiceID")).intValue());
        }
        else{
            return null;
        }

        if(recMap.containsKey("accepted") && ((boolean) recMap.get("accepted"))){
            i.acceptInvoice();
        }

        return i;

    }

    private void setInvoiceID(Integer id){
        this.invoiceID = id;
    }

}
