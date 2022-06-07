package data;

import java.io.Serializable;

public class Coordinates implements Serializable {

    private long x; //Максимальное значение поля: 293
    private Integer y; //Максимальное значение поля: 686, Поле не может быть null

    public Coordinates (long x,Integer y){
        this.x=x;
        this.y=y;
    }

    public Integer getY() {
        return y;
    }

    public long getX() {
        return x;
    }

    @Override
    public String toString() {
        return "x: " + x + ", y: " + y;
    }
}
