package DBook.project.app.book;

import DBook.project.app.Transactionable;
import org.neo4j.driver.Transaction;

public class Book implements Transactionable {
    private Integer bookID;
    private Integer bookInfoID;

    public Integer getBookID() {
        return bookID;
    }

    @Override
    public void addToDB(Transaction tx) {

    }

    @Override
    public void removeFromDB(Transaction tx) {

    }

    @Override
    public void getFromDB(Transaction tx) {

    }
}
