package DBook.project.app.users;

import DBook.project.app.book.Book;
import DBook.project.app.DBookApplication;
import org.neo4j.driver.Transaction;

import java.util.ArrayList;

public class Employee extends User{

    private DBookApplication dBookApplication;
    public Employee(DBookApplication dBookApplication, String name, String surname, String email){
        super(name, surname, email);
        this.dBookApplication = dBookApplication;
    }

    public void listSomeoneInvoices(Integer userID){//wylistowuje faktury danego uzytkownika
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

    public Float calculateInvoice(Integer userID, Transaction txt){ // ma zwrocic ile ma dany uzytkownik ma zaplacic
        ArrayList<Integer> invoicesIDList = new ArrayList(this.dBookApplication.getUserArrayList().get(userID).getUsersOffers().entrySet());
        Float result = new Float(0.0);
        for (Integer invoiceID: invoicesIDList) {
            result += this.dBookApplication.getUserArrayList().get(userID).calculateInvoice(invoiceID, txt);
        }
        return result;
    }

    public void calculateOffer(Integer userID){ //ma zwrocic ile danemu uzytkownikowi mamy wydac pieniedzy

    }

    public void listBooksToReturn(Integer userID){ //listuje ksiązki, które mamy zwrócić klientowi

    }

    public void listOfferBooks(Integer offerID){ //wypisuje ksiazki, ktore maja bycprzyniesione przez klienta

    }

    public void listInvoiceBooks(Integer invoiceID){ //wypisuje ksiazki, ktore maja byc przyniesione dla klienta

    }
    public void listOrderBooks(Integer orderID){// wypisuje ksiazki, ktore maja byc przyniesione przez klienta

    }
    public void acceptInvoice(Integer invoiceID, ArrayList<Book> acceptedBooks){// akceptuje zamowienie (zostalo zaplacone) i od razu usuwa te ksiazki z zamowienia, ktore nie zostaly kupione na miejscu

    }

    public void acceptOffer(Integer offerID, ArrayList<Book> acceptedBooks){//akceptuje zamowienie (ksiazki zostaly przyniesione) i od razu usuwa te ksiazki z zamowienia, ktore nie zostaly przyjete/przyniesione

    }

    public void deleteUnacceptedOffers(){

    }

    public void deleteUnacceptedInvoices(){

    }
}
