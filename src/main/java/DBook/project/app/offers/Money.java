package DBook.project.app.offers;

public class Money {
    private Integer value;

    public Money(){
        this.value = 0;
    }

    public void add(Integer subvalue){
        this.value = this.value + subvalue;
    }

    public Integer getValue(){
        return this.value;
    }
}
