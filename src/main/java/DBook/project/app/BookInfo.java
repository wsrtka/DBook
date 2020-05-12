package DBook.project.app;

public class BookInfo {
    private Integer bookInfoID;

    public BookInfo(Integer bookInfoID, String title, Float price, BookType type, String publisher, String subject, Integer semester, String author, String isbn){
        this.bookInfoID = bookInfoID;
    }
}
