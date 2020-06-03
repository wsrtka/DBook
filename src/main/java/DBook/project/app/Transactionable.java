package DBook.project.app;

import org.neo4j.driver.Result;
import org.neo4j.driver.Transaction;

import java.util.Map;

public interface Transactionable {

    Result addToDB(Transaction tx);

    Result removeFromDB(Transaction tx);

    Result getFromDB(Transaction tx);

    Result update(Transaction tx);

    void updateParams();

    void setupIdGenerator(Transaction tx);

}
