package DBook.project.app.book;

import DBook.project.app.IdGenerator;
import DBook.project.app.Transactionable;
import org.neo4j.driver.Result;
import org.neo4j.driver.Transaction;

import java.util.HashMap;
import java.util.Map;

import static org.neo4j.driver.Values.parameters;

public class Book implements Transactionable {

    private static final IdGenerator idGen = new IdGenerator();

    private Integer bookID;

    private String title;
    private Float price;
    private BookType type;
    private String publisher;
    private Integer semester;
    private String author;
    private String isbn;
    private BookState state;

    private Map<String, Object> params;

    public Book(){
        this.params = new HashMap<>();
        this.updateParams();
        this.params.put("title", this.title);
        this.params.put("price", this.price);
        this.params.put("bookID", this.bookID);
    }

    // minimal required book info
    public Book(String title, float price){
        this();

        this.title = title;
        this.price = price;
        this.state = BookState.AVAILABLE;

        this.bookID = idGen.getNextID();
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
        query = addOptionalAttributes(query);
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

    public void setPrice(Float price) {
        this.price = price;
    }

    public void setState(BookState state) {
        this.state = state;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
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

    public Integer getBookID() {
        return bookID;
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
            if(this.params.containsKey("state") && !this.params.get("state").equals(this.state)){
                this.params.replace("state", this.state);
            }
            else{
                this.params.put("state", this.state);
            }
        }

    }

    public Float getPrice() {
        return price;
    }
}
