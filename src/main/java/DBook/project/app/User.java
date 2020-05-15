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

    public void addInvoice(ArrayList<Book> books){ // musimy ustalić jak mamy dodać książki (obiekty typu Book)
        Integer invoiceID = this.idGenerator.getNextID();
        Invoice invoice = new Invoice(invoiceID, books);
        this.usersInvoices.put(invoiceID, invoice);
    }

    public void createBookInfo(String title, Float price, BookType type, String publisher, String subject, Integer semester, String author, String isbn){
        Integer bookInfoID = this.idGenerator.getNextID();
        new BookInfo(bookInfoID,title, price, type, publisher, subject, semester, author, isbn);
    }

    public void listInvoices(){
        ArrayList<Integer> invoicesIDList = new ArrayList(usersInvoices.entrySet());
        for (Integer invoiceID: invoicesIDList) {
            System.out.println(invoiceID+";");
        }
    }

    public void listOffers(){
        ArrayList<Offer> offersIDList = new ArrayList(usersOffers.entrySet());
        for(Offer offerID : offersIDList){
            System.out.println(offerID + ";");
        }
    }

    public Integer calculateInvoice(Integer invoiceID){ // pytanie, czy tu nie robimy opcji tylko dla swoich zamowien/offert (bo pracownik potrzebuje miec dostep do wszystkich)
        return this.usersInvoices.get(invoiceID).calculateInvoice();
    }
}
