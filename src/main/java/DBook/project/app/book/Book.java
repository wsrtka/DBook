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

    private Map<String, Object> params;

    public Book(
            String title,
            Float price,
            BookType type,
            String publisher,
            Integer semester,
            String author,
            String isbn
            ){
        this.title = title;
        this.price = price;
        this.type = type;
        this.publisher = publisher;
        this.semester = semester;
        this.author = author;
        this.isbn = isbn;

        this.bookID = idGen.getNextID();

        this.params = new HashMap<>();
        this.updateParams();
        this.params.put("title", this.title);
        this.params.put("price", this.price);
        this.params.put("bookID", this.bookID);
    }

    // minimal required book info
    public Book(String title, float price){
        this.title = title;
        this.price = price;

        this.bookID = idGen.getNextID();

        this.params = new HashMap<>();
        this.params.put("title", this.title);
        this.params.put("price", this.price);
        this.params.put("bookID", this.bookID);
    }

    @Override
    public Result addToDB(Transaction tx) {

        String query = "CREATE (b: Book)" +
                " SET b.title = $title" +
                ",  b.price = $price" +
                ",  b.bookID = $bookID";

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
        //potrzebne?
        return null;
    }

    @Override
    public Result update(Transaction tx) {

        String query = "MATCH (b: Book {bookID: $bookID})" +
                " SET b.title = $title" +
                ", b.price = $price" +
                ", b.bookID = $bookID";

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

        return query;

    }

    public Integer getBookID() {
        return bookID;
    }

    private void updateParams(){

        if(this.isbn != null){
            if(this.params.containsKey("isbn")){
                this.params.replace("isbn", this.isbn);
            }
            else{
                this.params.put("isbn", this.isbn);
            }
        }
        if(this.type != null){
            if(this.params.containsKey("type")){
                this.params.replace("type", this.type);
            }
            else{
                this.params.put("type", this.type);
            }
        }
        if(this.author != null){
            if(this.params.containsKey("author")){
                this.params.replace("author", this.author);
            }
            else{
                this.params.put("author", this.author);
            }
        }
        if(this.publisher != null){
            if(this.params.containsKey("publisher")){
                this.params.replace("publisher", this.publisher);
            }
            else{
                this.params.put("publisher", this.publisher);
            }
        }
        if(this.semester != null){
            if(this.params.containsKey("semester")){
                this.params.replace("semester", this.semester);
            }
            else{
                this.params.put("semester", this.semester);
            }
        }


    }

}
