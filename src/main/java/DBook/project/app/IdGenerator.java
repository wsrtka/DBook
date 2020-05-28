package DBook.project.app;

public class IdGenerator {

    private Integer nextID;

    public IdGenerator(){
        this.nextID = 0;
    }

    public int getNextID() {
        return this.nextID++;
    }
}
