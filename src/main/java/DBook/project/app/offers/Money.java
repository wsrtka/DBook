package DBook.project.app.offers;

import org.neo4j.driver.Record;

public class Money {
    private Integer value;

    public Money(){
        this.value = 0;
    }

    public void add(Record value){
        this.value = this.value + value;
    }

    public Integer getValue(){
        return this.value;
    }
}
