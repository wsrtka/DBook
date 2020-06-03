package DBook.project.app.book;

public enum BookType {

    TEXT_BOOK, EXERCISE_BOOK, COMPENDIUM, OTHER;

    private String toString;

    static {
        TEXT_BOOK.toString = "Text book";
        EXERCISE_BOOK.toString = "Exercise book";
        COMPENDIUM.toString = "Compendium";
        OTHER.toString = "Other";
    }

    @Override
    public String toString(){
        return this.toString;
    }

    public BookType fromString(String s){

        for(BookType t : BookType.values()){
            if(t.toString().equals(s)){
                return t;
            }
        }

        return null;

    }

}
