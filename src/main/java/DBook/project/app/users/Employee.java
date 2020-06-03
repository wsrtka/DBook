package DBook.project.app.users;

import DBook.project.app.book.Book;
import DBook.project.app.DBookApplication;
import DBook.project.app.offers.Invoice;
import DBook.project.app.offers.Money;
import DBook.project.app.offers.Offer;
import org.neo4j.driver.Transaction;

import java.util.ArrayList;

public class Employee extends User{

    private DBookApplication dBookApplication;

    public Employee(DBookApplication dBookApplication, String name, String surname, String email){

        super(name, surname, email);
        this.dBookApplication = dBookApplication;

    }

    public void listSomeoneInvoices(Integer userID){

        ArrayList<Integer> invoicesIDList = new ArrayList(this.dBookApplication.getUserArrayList().get(userID).getUsersInvoices().entrySet());

        for (Integer invoiceID: invoicesIDList) {
            System.out.println(invoiceID+";");
        }

    }

    public void listSomeoneOffers(Integer userID){//wylistowuje oferty danego uzytkownika
        ArrayList<Integer> invoicesIDList = new ArrayList(this.dBookApplication.getUserArrayList().get(userID).getUsersOffers().entrySet());
        for (Integer invoiceID: invoicesIDList) {
            System.out.println(invoiceID+";");
        }
    }

    public void listAllUsers(){//wylistowuje wszystkich uzytkownikow
        for(User user:this.dBookApplication.getUserArrayList()){
            System.out.println(user.toString()+";");
        }
    }

    public Double calculateInvoice(Integer userID, Transaction txt){ // ma zwrocic ile ma dany uzytkownik ma zaplacic
        ArrayList<Integer> invoicesIDList = new ArrayList(this.dBookApplication.getUserArrayList().get(userID).getUsersOffers().entrySet());
        Double result = new Double(0.0);
        for (Integer invoiceID: invoicesIDList) {
            result += this.dBookApplication.getUserArrayList().get(userID).calculateInvoice(invoiceID, txt);
        }
        return result;
    }

    public Double calculateOffer(Integer userID, Transaction tx){
        User user = dBookApplication.getUserArrayList().get(userID);
        Money money = new Money();
        user.getUsersOffers().forEach((k, v) ->money.add(v.calculateOfferRevenue()));
        return money.getValue();
    }

    public ArrayList<Book> listBooksToReturn(Integer userID){ //listuje ksiązki, które mamy zwrócić klientowi
        ArrayList<Book> booksToReturn = new ArrayList<>();
        User user = dBookApplication.getUserArrayList().get(userID);

        user.getUsersOffers().forEach((k, v) -> booksToReturn.addAll(v.getUnsoldBooks()));

        return booksToReturn;
    }

    public ArrayList<Book> listInvoiceBooks(Integer invoiceID, Integer userID){ //wypisuje ksiazki, ktore maja byc przyniesione dla klienta
        ArrayList<Book> booksToBeBrought = new ArrayList<>();
        Invoice invoice = dBookApplication.getUserArrayList().get(userID).getUsersInvoices().get(invoiceID);

        booksToBeBrought.addAll(invoice.getInvoiceBooks().values());
        return booksToBeBrought;
    }

    public ArrayList<Book> listOrderBooks(Integer orderID, Integer userID){// wypisuje ksiazki, ktore maja byc przyniesione przez klienta
        ArrayList<Book> booksOffer = new ArrayList<>();

        Offer offer = dBookApplication.getUserArrayList().get(userID).getUsersOffers().get(orderID);
        booksOffer.addAll(offer.getBooks().values());
        return booksOffer;
    }
    public void acceptInvoice(Integer invoiceID, ArrayList<Book> acceptedBooks, Integer userID, Transaction tx){// akceptuje zamowienie (zostalo zaplacone) i od razu usuwa te ksiazki z zamowienia, ktore nie zostaly kupione na miejscu
            Invoice invoice = dBookApplication.getUserArrayList().get(userID).getUsersInvoices().get(invoiceID);
            invoice.acceptInvoice();
            for(Book book:acceptedBooks){
                book.claimBook();
                book.update(tx);
            }

            invoice.getInvoiceBooks().forEach((k, v)->{
                if(!acceptedBooks.contains(v)){
                    v.disclaimBook();
                    v.update(tx);
                }
            });
    }

    public void acceptOffer(Integer offerID, ArrayList<Book> acceptedBooks, Integer userID, Transaction tx){//akceptuje zamowienie (ksiazki zostaly przyniesione) i od razu usuwa te ksiazki z zamowienia, ktore nie zostaly przyjete/przyniesione
        Offer offer = dBookApplication.getUserArrayList().get(userID).getUsersOffers().get(offerID);
        offer.acceptOffer();
        for(Book book:acceptedBooks){
            book.claimBook();
            book.update(tx);
        }

        offer.getOfferBooks().forEach((k, v)->{
            if(!acceptedBooks.contains(v)){
                v.removeFromDB(tx);
                offer.getBooks().remove(v); // nie jestem pewien czy zadziała to wywołanie poprawnie
            }
        });
    }

    public void deleteUnacceptedOffers(Transaction tx){
        for(User user : dBookApplication.getUserArrayList()){
            user.getUsersOffers().forEach((k, v)->{
                if(v.isAccepted()){
                    v.removeFromDB(tx);
                    user.getUsersOffers().remove(v); // nie jestem pewien czy zadziała
                }
            });
        }
    }

    public void deleteUnacceptedInvoices(Transaction tx){
        for(User user : dBookApplication.getUserArrayList()){
            user.getUsersInvoices().forEach((k, v)->{
                if(v.isAccepted()){
                    v.removeFromDB(tx);
                    user.getUsersInvoices().remove(v); // nie jestem pewien czy zadziała
                }
            });
        }
    }
}
