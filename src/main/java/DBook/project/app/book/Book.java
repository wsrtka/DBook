package DBook.project.app.book;

import DBook.project.app.IdGenerator;
import DBook.project.app.Transactionable;
import org.neo4j.driver.Result;
import org.neo4j.driver.Transaction;

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
    }

    // minimal required book info
    public Book(String title, float price){
        this.title = title;
        this.price = price;

        this.bookID = idGen.getNextID();
    }

    @Override
    public Result addToDB(Transaction tx) {

        String query = "CREATE (b: Book)" +
                " SET b.title = $title" +
                ",  b.price = $price" +
                ",  b.bookID = $bookID";

        query = this.addOptionalAttributes(query);

        return tx.run(query, parameters(
                "title", this.title,
                "price", this.price,
                "bookID", this.bookID
            )
        );

    }

    @Override
    public Result removeFromDB(Transaction tx) {

        String query = "MATCH (b: Book {bookID: $bookID})" +
                " DELETE b";

        return tx.run(query);

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

        return tx.run(query, parameters(
                "title", this.title,
                "price", this.price,
                "bookID", this.bookID)
        );

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
}
