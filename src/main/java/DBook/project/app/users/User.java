package DBook.project.app.users;

import DBook.project.app.Transactionable;
import DBook.project.app.book.Book;
import DBook.project.app.book.BookState;
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

    public void addInvoice(ArrayList<Book> books, Transaction txt){ // musimy ustalić jak mamy dodać książki (obiekty typu Book)
        Integer invoiceID = this.idGenerator.getNextID();
        Invoice invoice = new Invoice(books, txt);
        this.usersInvoices.put(invoiceID, invoice);
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

    public Double calculateInvoice(Integer invoiceID, Transaction txt){ // pytanie, czy tu nie robimy opcji tylko dla swoich zamowien/offert (bo pracownik potrzebuje miec dostep do wszystkich)
        return this.usersInvoices.get(invoiceID).calculateInvoice(txt);
    }

    private String title;
    private Float price;
    private BookType type;
    private String publisher;
    private Integer semester;
    private String author;
    private String isbn;
    private BookState state;

    public Result listBooks(String title, BookType type, String publisher, Integer semester, String author, Transaction tx){
        Map<String,Object> params = new HashMap<>();
        params.put("title", title);
        params.put("type", type);
        params.put("publisher", publisher);
        params.put("semester", semester);
        params.put("author", author);

        String query = "MATCH (b: Book {price: $price, title: $title";
        if(title != null){
            query = query + "title: $title, ";
        }
        if(type != null){
            query = query + "type: $type, ";
        }
        if(publisher != null){
            query = query + "publisher: $publisher, ";
        }
        if(semester != null){
            query = query + "semester: $semester, ";
        }
        if(author != null){
            query = query + "author: $author";
        }
        query = query + "}) RETURN b";

        return tx.run(query, this.params);
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
        String query = "MATCH (u: User {userID: $userID})" +
                " DELETE u";

        return tx.run(query, parameters("userID", this.userID));
    }

    @Override
    public Result getFromDB(Transaction tx) {
        String query = "MATCH (u: User {name: $name, surname: $surname, email: $email";
        query = addOptionalAttributes(query);
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

        query = this.addOptionalAttributes(query);

        this.updateParams();

        return tx.run(query, this.params);
    }
}
