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
import java.util.Map;

public class User implements Transactionable {

    private Integer userID;

    private static IdGenerator idGenerator = new IdGenerator();

    private String name;
    private String surname;
    private String email;

    private HashMap<Integer, Offer> usersOffers;
    private HashMap<Integer, Invoice> usersInvoices;

    private Map<String, Object> params;

    public User(String name, String surname, String email){

        this.userID = idGenerator.getNextID();

        this.name = name;
        this.surname = surname;
        this.email = email;

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

        Offer offer = new Offer(books);
        this.usersOffers.put(offer.getOfferID(), offer);

    }

    public void addInvoice(ArrayList<Book> books){
        // TODO: musimy ustalić jak mamy dodać książki (obiekty typu Book)
        Invoice invoice = new Invoice( books);
        this.usersInvoices.put(invoice.getInvoiceID(), invoice);

    }

    public void listUserInvoices(){
        ArrayList<Integer> invoicesIDList = new ArrayList(usersInvoices.entrySet());
        for (Integer invoiceID: invoicesIDList) {
            System.out.println(invoiceID+";");
        }
    }

    public void listUserOffers(){
        ArrayList<Offer> offersIDList = new ArrayList(usersOffers.entrySet());
        for(Offer offerID : offersIDList){
            System.out.println(offerID + ";");
        }
    }

    public Integer calculateInvoice(Integer invoiceID){ // pytanie, czy tu nie robimy opcji tylko dla swoich zamowien/offert (bo pracownik potrzebuje miec dostep do wszystkich)
        return this.usersInvoices.get(invoiceID).calculateInvoice();
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

        if(this.email != null){
            if(this.params.containsKey("email")){
                this.params.replace("email", this.email);
            }
            else{
                this.params.put("email", this.email);
            }
        }

    }
}
