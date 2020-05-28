package DBook.project.app.book;

import DBook.project.app.IdGenerator;
import DBook.project.app.Transactionable;
import org.neo4j.driver.Transaction;

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
    public void addToDB(Transaction tx) {

        String query = "CREATE (b: Book)" +
                " SET b.title = " + this.title +
                " SET b.price =" + this.price.toString() +
                " SET b.bookID = " + this.bookID.toString();

        if(this.type != null){
            query = query + " SET b.type = " + this.type.toString();
        }
        if(this.publisher != null){
            query = query + " SET b.publisher = " + this.publisher;
        }
        if(this.semester != null){
            query = query + " SET b.semester = " + this.semester.toString();
        }
        if(this.author != null){
            query = query + " SET b.author = " + this.author;
        }
        if(this.isbn != null){
            query = query + " SET b.isbn = " + this.isbn;
        }

        tx.run(query);

    }

    @Override
    public void removeFromDB(Transaction tx) {

        String query = "MATCH (b: Book {bookID: " + this.bookID + "})" +
                " DELETE b";

        tx.run(query);

    }

    @Override
    public void getFromDB(Transaction tx) {

    }

    @Override
    public void update(Transaction tx) {

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
}
