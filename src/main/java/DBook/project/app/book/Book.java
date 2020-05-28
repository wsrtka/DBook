package DBook.project.app.book;

import DBook.project.app.IdGenerator;
import DBook.project.app.Transactionable;
import org.neo4j.driver.Transaction;

public class Book implements Transactionable {

    private static final IdGenerator idGen = new IdGenerator();

    private Integer bookID;

    private String title;
    private float price;
    private BookType type;
    private String publisher;
    private int semester;
    private String author;
    private String isbn;

    public Book(
            String title,
            float price,
            BookType type,
            String publisher,
            int semester,
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

    public Integer getBookID() {
        return bookID;
    }

    @Override
    public void addToDB(Transaction tx) {

    }

    @Override
    public void removeFromDB(Transaction tx) {

    }

    @Override
    public void getFromDB(Transaction tx) {

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
