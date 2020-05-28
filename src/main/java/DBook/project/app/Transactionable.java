package DBook.project.app;

import org.neo4j.driver.Transaction;

public interface Transactionable {

    void addToDB(Transaction tx);

    void removeFromDB(Transaction tx);

    void getFromDB(Transaction tx);

    void update(Transaction tx);

}
