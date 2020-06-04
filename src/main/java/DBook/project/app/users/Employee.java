package DBook.project.app.users;

import DBook.project.app.book.Book;
import DBook.project.app.DBookApplication;
import DBook.project.app.offers.Invoice;
import DBook.project.app.offers.Money;
import DBook.project.app.offers.Offer;
import org.neo4j.driver.Result;
import org.neo4j.driver.Transaction;

import java.util.ArrayList;
import java.util.Collection;

import static org.neo4j.driver.Values.ofEntity;
import static org.neo4j.driver.Values.parameters;

public class Employee extends User{

    private DBookApplication dBookApplication;

    public Employee(DBookApplication dBookApplication, String name, String surname, String email){

        super(name, surname, email);
        this.dBookApplication = dBookApplication;

    }

    public ArrayList<Invoice> listSomeoneInvoices(User user, Transaction tx){

        //ArrayList<Integer> invoicesIDList = new ArrayList(this.dBookApplication.getUserArrayList().get(userID).getUsersInvoices(tx).entrySet());
        return new ArrayList(user.getUsersInvoices(tx).values());
    }

    public void listSomeoneOffers(Integer userID, Transaction tx){

        ArrayList<Integer> invoicesIDList = new ArrayList(this.dBookApplication.getUserArrayList().get(userID).getUsersOffers(tx).entrySet());

        for (Integer invoiceID: invoicesIDList) {
            System.out.println(invoiceID+";");
        }

    }

    public void listAllUsers(){

        for(User user:this.dBookApplication.getUserArrayList()){
            System.out.println(user.toString()+";");
        }

    }

    public Double calculateInvoice(User user, Transaction tx){
        Collection<Invoice> invoicesList = new ArrayList(user.getUsersInvoices(tx).values());
        Double result = new Double(0.0);

        for (Invoice invoice: invoicesList) {
            result += user.calculateInvoice(invoice.getInvoiceID());
        }

        return result;

    }

    public Integer calculateOffer(User user, Transaction tx){

        Money money = new Money();
        user.getUsersOffers(tx).forEach((k, v) ->money.add(v.calculateOfferRevenue()));

        return money.getValue();

    }

    public ArrayList<Book> listBooksToReturn(User user, Transaction tx){

        ArrayList<Book> booksToReturn = new ArrayList<>();
        user.getUsersOffers(tx).forEach((k, v) -> booksToReturn.addAll(v.getUnsoldBooks()));

        return booksToReturn;
    }

    public ArrayList<Book> listInvoiceBooks(Integer invoiceID, User user, Transaction tx){

        ArrayList<Book> booksToBeBrought = new ArrayList<>();
        Invoice invoice = user.getUsersInvoices(tx).get(invoiceID);

        booksToBeBrought.addAll(invoice.getInvoiceBooks().values());

        return booksToBeBrought;

    }

    public ArrayList<Book> listOfferBooks(Integer offerID, User user, Transaction tx){

        ArrayList<Book> booksOffer = new ArrayList<>();

        Offer offer = user.getUsersOffers(tx).get(offerID);
        booksOffer.addAll(offer.getBooks().values());

        return booksOffer;

    }

    public void acceptInvoice(Invoice invoice, ArrayList<Book> acceptedBooks,  Transaction tx){

            invoice.acceptInvoice();

            for(Book book:acceptedBooks){
                book.claimBook();
                book.update(tx);
            }
            ArrayList<Book> booksToBeDeleted = new ArrayList<>();
            invoice.getInvoiceBooks().forEach((k, v)->{
                if(!acceptedBooks.contains(v)){
                    v.disclaimBook();
                    v.update(tx);
                    booksToBeDeleted.add(v);
                }
            });
            for(Book book: booksToBeDeleted){
                invoice.getInvoiceBooks().values().remove(book);
            }
    }

    public void acceptOffer(Offer offer, ArrayList<Book> acceptedBooks,  Transaction tx){

        offer.acceptOffer();

        for(Book book:acceptedBooks){
            book.update(tx);
        }
        ArrayList<Book> booksToBeDeleted = new ArrayList<>();
        offer.getOfferBooks().forEach((k, v)->{
            if(!acceptedBooks.contains(v)){
                v.removeFromDB(tx);
                booksToBeDeleted.add(v);
            }
        });
        for(Book book: booksToBeDeleted){
            offer.getBooks().values().remove(book);
        }
    }

    public void deleteUnacceptedOffers(Transaction tx){

        for(User user : this.dBookApplication.getUserArrayList()){
            ArrayList<Offer> offersToBeDeleted = new ArrayList<>();
            user.getUsersOffers(tx).forEach((k, v)->{
                if(!v.isAccepted()){
                    ArrayList<Book> booksToBeDeleted = new ArrayList<>();
                    v.getOfferBooks().forEach((bk, bv)->{
                            v.removeFromDB(tx);
                            booksToBeDeleted.add(bv);
                    });
                    for(Book book: booksToBeDeleted){
                        v.getBooks().values().remove(book);
                    }
                    v.removeFromDB(tx);
                    offersToBeDeleted.add(v);
                }
            });
            for(Offer offer: offersToBeDeleted){
                user.getUsersOffers(tx).values().remove(offer);
            }
        }

    }

    public void deleteUnacceptedInvoices(Transaction tx){
        for(User user : this.dBookApplication.getUserArrayList()){
            ArrayList<Invoice> invoicesToBeDeleted = new ArrayList<>();
            user.getUsersInvoices(tx).forEach((k, v)->{
                if(!v.isAccepted()){
                    v.removeFromDB(tx);
                    invoicesToBeDeleted.add(v);
                }
            });
            for(Invoice invoice: invoicesToBeDeleted){
                user.getUsersOffers(tx).values().remove(invoice);
            }
        }
    }

    @Override
    public Result addToDB(Transaction tx) {

        super.addToDB(tx);

        String query = "MATCH (u: User {userID: $userID}) " +
                "SET u :User:Employee";

        return tx.run(query, parameters("userID", super.getUserID()));

    }

}
