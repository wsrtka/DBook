package DBook.project.app;

import java.util.HashMap;
import java.util.Set;

public class User {
    private Integer userID;
    private IdGenerator idGenerator;
    private HashMap<Integer, Offert> usersOfferts;
    private HashMap<Integer, Invoice> usersInvoices;

    public User(){
        this.idGenerator = IdGenerator.getInstance();
        this.userID = this.idGenerator.getNextID();
        this.usersOfferts = new HashMap<>();
        this.usersInvoices = new HashMap<>();
    }


}
