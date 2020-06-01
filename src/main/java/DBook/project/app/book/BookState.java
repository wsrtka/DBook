package DBook.project.app.book;

public enum BookState {

    AVAILABLE, RESERVED, CLAIMED;

    private String toString;

    static {
        AVAILABLE.toString = "Available";
        RESERVED.toString = "Reserved";
        CLAIMED.toString = "Claimed";
    }

    @Override
    public String toString(){
        return this.toString;
    }

}
