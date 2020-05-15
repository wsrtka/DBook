package DBook.project.app;

import java.util.ArrayList;

public class Employee extends User{
    private DBookApplication dBookApplication;
    public Employee(DBookApplication dBookApplication){
        super();
        this.dBookApplication = dBookApplication;
    }

    @Override
    public Integer calculateInvoice(Integer invoiceID){
        return 0;
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
}
