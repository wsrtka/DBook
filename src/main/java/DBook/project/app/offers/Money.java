package DBook.project.app.offers;

public class Money {
    private Float value;

    public Money(){
        this.value = new Float(0.0);
    }

    public void add(Float subvalue){
        this.value += subvalue;
    }

    public Float getValue(){
        return this.value;
    }
}
