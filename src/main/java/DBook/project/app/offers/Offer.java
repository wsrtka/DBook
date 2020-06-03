
package DBook.project.app.offers;

import DBook.project.app.IdGenerator;
import DBook.project.app.Transactionable;
import DBook.project.app.book.Book;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.neo4j.driver.Values.parameters;

public class Offer implements Transactionable {

    private Integer offerID;

    private static IdGenerator idGen;

    private HashMap<Integer, Book> books;

    private Map<String, Object> params;

    private boolean accepted;

    public Offer(){

        if(idGen == null){
            idGen = new IdGenerator();
        }
        this.offerID = idGen.getNextID();

        this.accepted = false;

        this.params = new HashMap<>();
        this.params.put("offerID", this.offerID);
        this.params.put("accepted", this.accepted);

    }

    @Override
    public void setupIdGenerator(Transaction tx){
        idGen = new IdGenerator("Offer", tx);
    }

    public Offer(List<Book> books, Transaction tx){

        this();

        this.addToDB(tx);

        this.books = new HashMap<>();
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

        return tx.run(query, parameters("offerID", this.offerID, "bookID", b.getBookID()));

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

    public Integer calculateOfferRevenue(){

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

    public Offer mapResult(Record rec){

        Map<String, Object> recMap = rec.get(0).asMap();
        Offer o;

        if(recMap.containsKey("offerID")){
            o = new Offer();
            o.setOfferID(((Long) recMap.get("offerID")).intValue());
        }
        else{
            return null;
        }

        if(recMap.containsKey("accepted") && ((boolean) recMap.get("accepted"))){
            o.acceptOffer();
        }

        return o;

    }

    public Integer getOfferID() {
        return offerID;
    }

    private void setOfferID(Integer id){
        this.offerID = id;
    }

}
