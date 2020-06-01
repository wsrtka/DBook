package DBook.project.app.users;

import DBook.project.app.Transactionable;
import DBook.project.app.book.Book;
import DBook.project.app.book.BookType;
import DBook.project.app.IdGenerator;
import DBook.project.app.offers.Invoice;
import DBook.project.app.offers.Offer;
import org.neo4j.driver.Result;
import org.neo4j.driver.Transaction;

import java.util.ArrayList;
import java.util.HashMap;

public class User implements Transactionable {

    private Integer userID;
    private static IdGenerator idGenerator = new IdGenerator();
    private HashMap<Integer, Offer> usersOffers;
    private HashMap<Integer, Invoice> usersInvoices;

    public User(){
        this.userID = this.idGenerator.getNextID();
        this.usersOffers = new HashMap<>();
        this.usersInvoices = new HashMap<>();
    }

    public HashMap<Integer, Invoice> getUsersInvoices() {
        return usersInvoices;
    }

    public HashMap<Integer, Offer> getUsersOffers() {
        return usersOffers;
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
    }

    public void listMyInvoices(){
        ArrayList<Integer> invoicesIDList = new ArrayList(usersInvoices.entrySet());
        for (Integer invoiceID: invoicesIDList) {
            System.out.println(invoiceID+";");
        }
    }

    public void listMyOffers(){
        ArrayList<Offer> offersIDList = new ArrayList(usersOffers.entrySet());
        for(Offer offerID : offersIDList){
            System.out.println(offerID + ";");
        }
    }

    public Integer calculateInvoice(Integer invoiceID){ // pytanie, czy tu nie robimy opcji tylko dla swoich zamowien/offert (bo pracownik potrzebuje miec dostep do wszystkich)
        return this.usersInvoices.get(invoiceID).calculateInvoice();
    }


    public void listBooksWithSpecifiedBookInfo(){ // ma zwrocicliste ksiazek z kategorii, ktora nas interesuje

    }

    public void listAllBookInfos(){ // ma wyswietlic wszyskie book info (moze by jakos z baza to polaczyc?)

    }

    @Override
    public Result addToDB(Transaction tx) {
        return null;
    }

    @Override
    public Result removeFromDB(Transaction tx) {
        return null;
    }

    @Override
    public Result getFromDB(Transaction tx) {
        return null;
    }

    @Override
    public Result update(Transaction tx) {
        return null;
    }

    @Override
    public void updateParams() {

    }
}
