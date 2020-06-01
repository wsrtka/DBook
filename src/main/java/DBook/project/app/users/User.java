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
    private HashMap<Integer, Offer> usersOffers;
    private HashMap<Integer, Invoice> usersInvoices;
    private String name;
    private String surname;
    private String email;
    private Map<String, Object> params;

    public User(String name, String surname, String email){
        this.userID = this.idGenerator.getNextID();
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

    public void addOffer(ArrayList<Book> books){
        Integer offerID = this.idGenerator.getNextID();
        Offer offer = new Offer(books);
        this.usersOffers.put(offerID, offer);
    }

    public void addInvoice(ArrayList<Book> books){ // musimy ustalić jak mamy dodać książki (obiekty typu Book)
        Integer invoiceID = this.idGenerator.getNextID();
        Invoice invoice = new Invoice(books);
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


    private String addOptionalAttributes(String query){

        if(this.name != null){
            query = query + ", u.name = $name";
        }
        if(this.surname != null){
            query = query + ", u.surname = $surname";
        }
        if(this.email != null){
            query = query + ", u.email = $email";
        }
        if(this.userID != null){
            query = query + ", u.userID = $userID";
        }
        return query;
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
        if(this.userID != null){
            if(this.params.containsKey("userID") && !this.params.get("userID").equals(this.userID)){
                this.params.replace("userID", this.userID);
            }
            else{
                this.params.put("userID", this.userID);
            }
        }
    }

    @Override
    public Result addToDB(Transaction tx) {
        String query = "CREATE (u: User)" +
                " SET c.name = $title" +
                ",  c.surname = $surname" +
                ",  c.email = $email" +
                ",  c.userID = $userID";

        query = this.addOptionalAttributes(query);
        this.updateParams();

        return tx.run(query, this.params);
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
}
