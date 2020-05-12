package DBook.project.app;

public class IdGenerator {

    private static final IdGenerator instance = new IdGenerator();
    private Integer nextID;

    private IdGenerator(){
        this.nextID = 0;
    }

    public static IdGenerator getInstance(){
        return instance;
    }

    public int getNextID() {
        this.nextID++;
        return this.nextID-1;
    }
}
