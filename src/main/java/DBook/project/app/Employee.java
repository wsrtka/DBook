package DBook.project.app;

import java.util.ArrayList;

public class Employee extends User{

    public Employee(){
        super();
    }

    @Override
    public Integer calculateInvoice(Integer invoiceID){
        return 0;
    }

    @Override
    public void listInvoices(Integer userID){

        ArrayList<Integer> invoicesIDList = new ArrayList(usersInvoices.entrySet());
        for (Integer invoiceID: invoicesIDList) {
            System.out.println(invoiceID+";");
        }
    }

}
