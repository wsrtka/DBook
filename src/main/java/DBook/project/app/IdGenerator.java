package DBook.project.app;

import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Transaction;

import static org.neo4j.driver.Values.parameters;

public class IdGenerator {

    private Integer nextID;

    public IdGenerator(){
        this.nextID = 0;
    }

    public IdGenerator(String classType, Transaction tx){

        String query = "MATCH (n: " + classType + ") " +
                "RETURN max(id(n))";

        Result res = tx.run(query);

        Record rec = res.next();

        this.nextID = rec.get("max(id(n))").asInt() + 1;

    }

    public int getNextID() {
        return this.nextID++;
    }
}
