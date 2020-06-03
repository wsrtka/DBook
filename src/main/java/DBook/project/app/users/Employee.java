package DBook.project.app.users;

import DBook.project.app.book.Book;
import DBook.project.app.DBookApplication;
import DBook.project.app.offers.Invoice;
import DBook.project.app.offers.Money;
import DBook.project.app.offers.Offer;
import org.neo4j.driver.Result;
import org.neo4j.driver.Transaction;

import java.util.ArrayList;

import static org.neo4j.driver.Values.ofEntity;
import static org.neo4j.driver.Values.parameters;

public class Employee extends User{

    private DBookApplication dBookApplication;

    public Employee(DBookApplication dBookApplication, String name, String surname, String email){

        super(name, surname, email);
        this.dBookApplication = dBookApplication;

    }

    public void listSomeoneInvoices(Integer userID, Transaction tx){

        ArrayList<Integer> invoicesIDList = new ArrayList(this.dBookApplication.getUserArrayList().get(userID).getUsersInvoices(tx).entrySet());

        for (Integer invoiceID: invoicesIDList) {
            System.out.println(invoiceID+";");
        }

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

    public Double calculateInvoice(Integer userID, Transaction tx){

        ArrayList<Integer> invoicesIDList = new ArrayList(this.dBookApplication.getUserArrayList().get(userID).getUsersOffers(tx).entrySet());
        Double result = new Double(0.0);

        for (Integer invoiceID: invoicesIDList) {
            result += this.dBookApplication.getUserArrayList().get(userID).calculateInvoice(invoiceID);
        }

        return result;

    }

    public Integer calculateOffer(Integer userID, Transaction tx){

        User user = dBookApplication.getUserArrayList().get(userID);
        Money money = new Money();

        user.getUsersOffers(tx).forEach((k, v) ->money.add(v.calculateOfferRevenue()));

        return money.getValue();

    }

    public ArrayList<Book> listBooksToReturn(Integer userID, Transaction tx){

        ArrayList<Book> booksToReturn = new ArrayList<>();
        User user = dBookApplication.getUserArrayList().get(userID);

        user.getUsersOffers(tx).forEach((k, v) -> booksToReturn.addAll(v.getUnsoldBooks()));

        return booksToReturn;
    }

    public ArrayList<Book> listInvoiceBooks(Integer invoiceID, Integer userID, Transaction tx){

        ArrayList<Book> booksToBeBrought = new ArrayList<>();
        Invoice invoice = dBookApplication.getUserArrayList().get(userID).getUsersInvoices(tx).get(invoiceID);

        booksToBeBrought.addAll(invoice.getInvoiceBooks().values());

        return booksToBeBrought;

    }

    public ArrayList<Book> listOrderBooks(Integer orderID, Integer userID, Transaction tx){

        ArrayList<Book> booksOffer = new ArrayList<>();

        Offer offer = dBookApplication.getUserArrayList().get(userID).getUsersOffers(tx).get(orderID);
        booksOffer.addAll(offer.getBooks().values());

        return booksOffer;

    }

    public void acceptInvoice(Integer invoiceID, ArrayList<Book> acceptedBooks, Integer userID, Transaction tx){

            Invoice invoice = dBookApplication.getUserArrayList().get(userID).getUsersInvoices(tx).get(invoiceID);
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

    public void acceptOffer(Offer offer, ArrayList<Book> acceptedBooks, Integer userID, Transaction tx){

        offer.acceptOffer();

        for(Book book:acceptedBooks){
            book.claimBook();
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

        for(User user : dBookApplication.getUserArrayList()){
            user.getUsersOffers(tx).forEach((k, v)->{
                if(v.isAccepted()){
                    v.removeFromDB(tx);
//                    uwaga: może nie działać
                    user.getUsersOffers(tx).remove(v);
                }
            });
        }

    }

    public void deleteUnacceptedInvoices(Transaction tx){
        for(User user : dBookApplication.getUserArrayList()){
            user.getUsersInvoices(tx).forEach((k, v)->{
                if(v.isAccepted()){
                    v.removeFromDB(tx);
//                    uwaga: może nie działać
                    user.getUsersInvoices(tx).remove(v);
                }
            });
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
