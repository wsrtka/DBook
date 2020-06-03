package DBook.project.app.offers;

public class Money {
    private Double value;

    public Money(){
        this.value = new Double(0.0);
    }

    public void add(Double subvalue){
        this.value += subvalue;
    }

    public Double getValue(){
        return this.value;
    }
}
