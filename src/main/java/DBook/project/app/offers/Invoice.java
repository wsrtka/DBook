package DBook.project.app.offers;

import DBook.project.app.book.Book;

import java.util.ArrayList;
import java.util.HashMap;

public class Invoice {
    private Integer invoiceID;
    private HashMap<Integer, Book> books;


    public Invoice(Integer invoiceID, ArrayList<Book> books){
        this.invoiceID = invoiceID;
        for(Book book : books){ // dodajemy do listy każdą książkę
            this.books.put(book.getBookID(), book);
        }
    }

    public Integer calculateInvoice(){
        return null;
    }
}
