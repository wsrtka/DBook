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

import static org.neo4j.driver.Values.parameters;

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

        this.params = new HashMap<>();
        this.params.put("userID", this.userID);
        this.updateParams();

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
//        TODO: wyciągać Invoice'y użytkownika z bazy
        ArrayList<Integer> invoicesIDList = new ArrayList(usersInvoices.entrySet());
        for (Integer invoiceID: invoicesIDList) {
            System.out.println(invoiceID+";");
        }
    }

    public void listUserOffers(){
//        TODO: wyciągać Offer'y użytkownika z bazy
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

        String query = "CREATE (u: User) " +
                "SET u.userID = $userID, " +
                "u.name = $name, " +
                "u.surname = $surname, " +
                "u.email = $email";

        this.updateParams();

        return tx.run(query, this.params);

    }

    @Override
    public Result removeFromDB(Transaction tx) {

        String query = "MATCH (u: User {$userID: $userID{)" +
                "DELETE u";

        return tx.run(query, parameters("userID", this.userID));

    }

    @Override
    public Result getFromDB(Transaction tx) {

        String query = "MATCH (u: User {u.name: $name, " +
                "u.surname: $surname " +
                "u.email: $email}) " +
                "RETURN u";

        this.updateParams();

        return tx.run(query, params);

    }

    @Override
    public Result update(Transaction tx) {

        String query = "MATCH (u: User {u.userID: $userID}) " +
                "SET u.name = $name " +
                "u.surname = $surname " +
                "u.email = $email";

        this.updateParams();

        return tx.run(query, this.params);

    }

    @Override
    public void updateParams() {

        if(this.email != null){
            if(this.params.containsKey("email") && !this.params.get("email").equals(this.email)){
                this.params.replace("email", this.email);
            }
            else{
                this.params.put("email", this.email);
            }
        }

        if(this.name != null){
            if(this.params.containsKey("name") && !this.params.get("name").equals(this.name)){
                this.params.replace("name", this.name);
            }
            else{
                this.params.put("name", this.name);
            }
        }

        if(this.surname != null){
            if(this.params.containsKey("surname") && !this.params.get("surname").equals(this.surname)){
                this.params.replace("surname", this.surname);
            }
            else{
                this.params.put("surname", this.surname);
            }
        }

    }
}
