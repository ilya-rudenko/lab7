package data;

import java.io.Serializable;

public class Location implements Serializable {

    private Long x; //Поле не может быть null
    private float y;
    private long z;
    public Location(Long x,float y, long z){
        this.x=x;
        this.y=y;
        this.z=z;
    }

    public float getY() {
        return y;
    }

    public Long getX() {
        return x;
    }

    public long getZ() {
        return z;
    }

    @Override
    public String toString() {
        return "x: " + x + ", y: " + y + ", z: " + z;
    }
}
