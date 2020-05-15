package DBook.project.app;

import java.util.ArrayList;
import java.util.HashMap;

public class BookInfo {
    private Integer bookInfoID;

    private ArrayList<Book> books;

    public BookInfo(Integer bookInfoID, String title, Float price, BookType type, String publisher, String subject, Integer semester, String author, String isbn){
        this.bookInfoID = bookInfoID;
        this.books = new ArrayList<>();
    }

    public void attachBook(Book book){
        this.books.add(book);
    }
}
