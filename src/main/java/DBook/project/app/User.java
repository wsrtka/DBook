package DBook.project.app;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    private Integer userID;
    private IdGenerator idGenerator;
    private HashMap<Integer, Offer> usersOffers;
    private HashMap<Integer, Invoice> usersInvoices;

    public User(){
        this.idGenerator = IdGenerator.getInstance();
        this.userID = this.idGenerator.getNextID();
        this.usersOffers = new HashMap<>();
        this.usersInvoices = new HashMap<>();
    }

    public void addOffer(ArrayList<Book> books){
        Integer offerID = this.idGenerator.getNextID();
        Offer offer = new Offer(offerID, books);
        this.usersOffers.put(offerID, offer);
    }

    public void addInvoice(){

    }

    public void createBookInfo(String title, Float price, BookType type, String publisher, String subject, Integer semester, String author, String isbn){
        Integer bookInfoID = this.idGenerator.getNextID();
        new BookInfo(bookInfoID,title, price, type, publisher, subject, semester, author, isbn);
    }
}
