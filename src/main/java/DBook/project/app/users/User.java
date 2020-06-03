package DBook.project.app.users;

import DBook.project.app.Transactionable;
import DBook.project.app.book.Book;
import DBook.project.app.book.BookState;
import DBook.project.app.book.BookType;
import DBook.project.app.IdGenerator;
import DBook.project.app.offers.Invoice;
import DBook.project.app.offers.Offer;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.neo4j.driver.Values.parameters;

public class User implements Transactionable {

    private Integer userID;

    private static IdGenerator idGenerator = new IdGenerator();

    private HashMap<Integer, Offer> usersOffers;
    private HashMap<Integer, Invoice> usersInvoices;
    private String name;
    private String surname;
    private String email;

    private Map<String, Object> params;

    public User(String name, String surname, String email){

        this.userID = idGenerator.getNextID();

        this.usersOffers = new HashMap<>();
        this.usersInvoices = new HashMap<>();
        this.params = new HashMap<>();
        this.name = name;
        this.surname = surname;
        this.email = email;

        this.params.put("name", this.name);
        this.params.put("surname", this.surname);
        this.params.put("email", this.email);
        this.params.put("userID", this.userID);

    }

    public HashMap<Integer, Invoice> getUsersInvoices() {
        return usersInvoices;
    }

    public HashMap<Integer, Offer> getUsersOffers() {
        return usersOffers;
    }

    public Result addOffer(ArrayList<Book> books, Transaction tx){

        Offer offer = new Offer(books);
        this.usersOffers.put(offer.getOfferID(), offer);

        offer.addToDB(tx);

        String query = "MATCH (u: User {userID: $userID}), (o: Offer {offerID: $offerID}) " +
                "CREATE (u)-[:HAS_A]->(o)";

        return tx.run(query, parameters("userID", this.userID, "offerID", offer.getOfferID()));

    }

    public Result addInvoice(ArrayList<Book> books, Transaction tx){

        Invoice invoice = new Invoice(books, tx);
        this.usersInvoices.put(invoice.getInvoiceID(), invoice);

        invoice.addToDB(tx);

        String query = "MATCH (u: User {userID: $userID}), (i: Invoice {invoiceID: $invoiceID}) " +
                "CREATE (u)-[:HAS_A]->(i)";

        return tx.run(query, parameters("userID", this.userID, "invoiceID", invoice.getInvoiceID()));


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

    public Double calculateInvoice(Integer invoiceID, Transaction txt){

        return this.usersInvoices.get(invoiceID).calculateInvoice(txt);

    }

    @Override
    public void updateParams(){

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
        if(this.email != null){
            if(this.params.containsKey("email") && !this.params.get("email").equals(this.email)){
                this.params.replace("email", this.email);
            }
            else{
                this.params.put("email", this.email);
            }
        }
    }

    @Override
    public Result addToDB(Transaction tx) {

        String query = "CREATE (u: User)" +
                " SET u.name = $name" +
                ",  u.surname = $surname" +
                ",  u.email = $email" +
                ",  u.userID = $userID";

        return tx.run(query, this.params);

    }

    @Override
    public Result removeFromDB(Transaction tx) {

        String query = "MATCH (u: User {userID: $userID})" +
                " DELETE u";

        return tx.run(query, parameters("userID", this.userID));

    }

    @Override
    public Result getFromDB(Transaction tx) {

        String query = "MATCH (u: User {name: $name, surname: $surname, email: $email";
        query = query + "}) RETURN u";

        updateParams();

        return tx.run(query, this.params);

    }

    @Override
    public Result update(Transaction tx) {

        String query = "MATCH (u: User {userID: $userID})" +
                " SET u.name = $name" +
                ", u.surname = $surname" +
                ", u.email = $email" +
                ", u.userID = $userID";

        this.updateParams();

        return tx.run(query, this.params);
    }

    public User mapResult(Record rec){

        Map<String, Object> recMap = rec.get(0).asMap();
        User u;

        if(
                recMap.containsKey("name")
                && recMap.containsKey("surname")
                && recMap.containsKey("email")
                && recMap.containsKey("userID")
        ){
            u = new User(
                    (String) recMap.get("name"),
                    (String) recMap.get("surname"),
                    (String) recMap.get("email")
            );
            u.setUserID(((Long) recMap.get("userID")).intValue());
        }
        else{
            return null;
        }

        return u;

    }

    private void setUserID(Integer id){
        this.userID = id;
    }

    public String getName(){
        return this.name;
    }

}
