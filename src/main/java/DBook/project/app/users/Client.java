package DBook.project.app.users;

import org.neo4j.driver.Result;
import org.neo4j.driver.Transaction;

import static org.neo4j.driver.Values.parameters;

public class Client extends User {

    public Client(String name, String surname, String email){
        super(name, surname, email);
    }

    @Override
    public Result addToDB(Transaction tx) {

        super.addToDB(tx);

        String query = "MATCH (u: User {userID: $userID}) " +
                "SET u :User:Client";

        return tx.run(query, parameters("userID", super.getUserID()));

    }

}
