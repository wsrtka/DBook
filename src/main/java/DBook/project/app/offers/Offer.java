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

public class Offer implements Transactionable {

    private Integer offerID;

    private static IdGenerator idGen = new IdGenerator();

    private HashMap<Integer, Book> books;

    private Map<String, Object> params;

    private boolean accepted;

    private Offer(){

        this.offerID = idGen.getNextID();

        this.books = new HashMap<>();

        this.accepted = false;

        this.params = new HashMap<>();
        this.params.put("offerID", this.offerID);
        this.params.put("accepted", this.accepted);

    }

    public Offer(ArrayList<Book> books, Transaction tx){

        for(Book book : books){
            this.addBook(book, tx);
        }

    }

    public Result addBook(Book b, Transaction tx) {

        this.books.put(b.getBookID(), b);

        Result res = b.getFromDB(tx);

        if (!res.hasNext()) {
            b.addToDB(tx);
        }

        String query = "MATCH (o: Offer {offerID: $offerID}), " +
                "(b: Book {bookID: $bookID}) " +
                "CREATE (o)-[:HAS_A]->(b)";

        return tx.run(query, parameters("invoiceID", this.offerID, "bookID", b.getBookID()));

    }

    public HashMap<Integer, Book> getBooks(){
        return this.books;
    }

    public ArrayList<Book> getUnsoldBooks(){

        ArrayList<Book> unsoldBooks = new ArrayList<>();

        this.books.forEach((k, v) ->{
            if(!v.isSold()){
                unsoldBooks.add(v);
            }
        });

        return unsoldBooks;

    }

    public HashMap<Integer, Book> getOfferBooks(){
        return this.books;
    }

    public Double calculateOfferRevenue(){

        Money revenue = new Money();

        this.books.forEach((k, v) ->{
            if(v.isSold()){
                revenue.add(v.getPrice());
            }
        });

        return revenue.getValue();

    }

    public void acceptOffer(){
        this.accepted = true;
    }

    public boolean isAccepted() {
        return this.accepted;
    }

    @Override
    public Result addToDB(Transaction tx) {

        String query = "CREATE (o: Offer {offerID: $offerID, accepted: $accepted})";

        return tx.run(query, this.params);

    }

    @Override
    public Result removeFromDB(Transaction tx) {

        String query = "MATCH (o: Offer {offerID: $offerID}) " +
                "DETACH DELETE o";

        return tx.run(query, this.params);

    }

    @Override
    public Result getFromDB(Transaction tx) {

        String query = "MATCH (o: Offer {offerID: $offerID}) " +
                "RETURN o";

        return tx.run(query, this.params);

    }

    @Override
    public Result update(Transaction tx) {

        String query = "MATCH (o: Offer {offerID: $offerID})" +
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

    public Integer getOfferID() {
        return offerID;
    }
}
