package DBook.project.app.book;

import DBook.project.app.IdGenerator;
import DBook.project.app.Transactionable;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Transaction;

import java.util.HashMap;
import java.util.Map;

import static org.neo4j.driver.Values.parameters;

public class Book implements Transactionable {

    private static IdGenerator idGen;

    private Integer bookID;

    private String title;
    private Integer price;
    private BookType type;
    private String publisher;
    private Integer semester;
    private String author;
    private String isbn;
    private BookState state;

    private Map<String, Object> params;

    // minimal required book info
    public Book(String title, Integer price){

        this.title = title;
        this.price = price;
        this.state = BookState.AVAILABLE;

        if(idGen == null){
            idGen = new IdGenerator();
        }
        this.bookID = idGen.getNextID();

        this.params = new HashMap<>();
        this.updateParams();
        this.params.put("title", this.title);
        this.params.put("price", this.price);
        this.params.put("bookID", this.bookID);

    }

    @Override
    public void setupIdGenerator(Transaction tx){
        idGen = new IdGenerator("User", tx);
    }

    public void disclaimBook(){
        this.state = BookState.AVAILABLE;
    }

    public void reserveBook(){
        this.state = BookState.RESERVED;
    }

    public void claimBook(){
        this.state = BookState.CLAIMED;
    }

    public boolean isSold(){
        return this.state == BookState.CLAIMED;
    }

    @Override
    public Result addToDB(Transaction tx) {

        String query = "CREATE (b: Book)" +
                " SET b.title = $title" +
                ",  b.price = $price" +
                ",  b.bookID = $bookID" +
                ", b.state = $state";

        query = this.addOptionalAttributes(query);

        this.updateParams();

        return tx.run(query, this.params);

    }

    @Override
    public Result removeFromDB(Transaction tx) {

        String query = "MATCH (b: Book {bookID: $bookID})" +
                " DELETE b";

        return tx.run(query, parameters("bookID", this.bookID));

    }

    @Override
    public Result getFromDB(Transaction tx) {

        String query = "MATCH (b: Book {price: $price, title: $title";
        query = addOptionalFilters(query);
        query = query + "}) RETURN b";

        updateParams();

        return tx.run(query, this.params);
    }

    @Override
    public Result update(Transaction tx) {

        String query = "MATCH (b: Book {bookID: $bookID})" +
                " SET b.title = $title" +
                ", b.price = $price" +
                ", b.bookID = $bookID" +
                ", b.state = $state";

        query = this.addOptionalAttributes(query);

        this.updateParams();

        return tx.run(query, this.params);

    }

    @Override
    public void updateParams(){

        if(this.isbn != null){
            if(this.params.containsKey("isbn") && !this.params.get("isbn").equals(this.isbn)){
                this.params.replace("isbn", this.isbn);
            }
            else{
                this.params.put("isbn", this.isbn);
            }
        }
        if(this.type != null){
            if(this.params.containsKey("type") && !this.params.get("type").equals(this.type)){
                this.params.replace("type", this.type);
            }
            else{
                this.params.put("type", this.type);
            }
        }
        if(this.author != null){
            if(this.params.containsKey("author") && !this.params.get("author").equals(this.author)){
                this.params.replace("author", this.author);
            }
            else{
                this.params.put("author", this.author);
            }
        }
        if(this.publisher != null){
            if(this.params.containsKey("publisher") && !this.params.get("publisher").equals(this.publisher)){
                this.params.replace("publisher", this.publisher);
            }
            else{
                this.params.put("publisher", this.publisher);
            }
        }
        if(this.semester != null){
            if(this.params.containsKey("semester") && !this.params.get("semester").equals(this.semester)){
                this.params.replace("semester", this.semester);
            }
            else{
                this.params.put("semester", this.semester);
            }
        }
        if(this.state != null){
            if(this.params.containsKey("state") && !this.params.get("state").equals(this.state.toString())){
                this.params.replace("state", this.state.toString());
            }
            else{
                this.params.put("state", this.state.toString());
            }
        }

    }

    private String addOptionalAttributes(String query){

        if(this.type != null){
            query = query + ", b.type = $type";
        }
        if(this.publisher != null){
            query = query + ", b.publisher = $publisher";
        }
        if(this.semester != null){
            query = query + ", b.semester = $semester";
        }
        if(this.author != null){
            query = query + ", b.author = $author";
        }
        if(this.isbn != null){
            query = query + ", b.isbn = $isbn";
        }
        if(this.isbn != null){
            query = query + ", b.sold = $sold";
        }

        return query;

    }

    private String addOptionalFilters(String query){

        if(this.type != null){
            query = query + ", b.type: $type";
        }
        if(this.publisher != null){
            query = query + ", b.publisher: $publisher";
        }
        if(this.semester != null){
            query = query + ", b.semester: $semester";
        }
        if(this.author != null){
            query = query + ", b.author: $author";
        }
        if(this.isbn != null){
            query = query + ", b.isbn: $isbn";
        }
        if(this.isbn != null){
            query = query + ", b.sold: $sold";
        }

        return query;

    }

    public Book mapResult(Record rec) {

        Map<String, Object> recMap = rec.get(0).asMap();
        Book b;

        if (recMap.containsKey("title") && recMap.containsKey("price") && recMap.containsKey("bookID")) {
            b = new Book((String) recMap.get("title"), new Integer((int) recMap.get("price")));
            b.setBookID(((Long) recMap.get("bookID")).intValue());
        } else {
            return null;
        }

        for (String key : recMap.keySet()) {

            if (key.equals("type")) {
                b.setType(BookType.COMPENDIUM.fromString((String) recMap.get(key)));
            }
            if(key.equals("publisher")){
                b.setPublisher((String) recMap.get(key));
            }
            if(key.equals("semester")){
                b.setSemester(((Long) recMap.get(key)).intValue());
            }
            if(key.equals("author")){
                b.setAuthor((String) recMap.get(key));
            }
            if(key.equals("isbn")) {
                b.setIsbn((String) recMap.get(key));
            }

        }

        return b;

    }

    private void setBookID(Integer id){
        this.bookID = id;
    }

    public void setType(BookType type) {
        this.type = type;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setState(BookState state) {
        this.state = state;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public Integer getBookID() {
        return bookID;
    }

    public Integer getPrice() {
        return price;
    }

    public BookState getState() {
        return state;
    }
}
