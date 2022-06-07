package data;

import java.io.Serializable;

public class Person implements Serializable {
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Long height; //Поле не может быть null, Значение поля должно быть больше 0
    private Color eyeColor; //Поле не может быть null
    private Color hairColor; //Поле не может быть null
    private Country nationality; //Поле может быть null
    private Location location; //Поле может быть null


    public Person (String name,Long height,Color eyeColor,Color hairColor,Country nationality,Location location){
        this.name=name;
        this.height=height;
        this.eyeColor=eyeColor;
        this.hairColor=hairColor;
        this.nationality=nationality;
        this.location=location;
    }

    public String getName() {
        return name;
    }

    public Color getEyeColor() {
        return eyeColor;
    }

    public Color getHairColor() {
        return hairColor;
    }

    public Country getNationality() {
        return nationality;
    }

    public Location getLocation() {
        return location;
    }

    public Long getHeight() {
        return height;
    }


    public String toFormalString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", height=" + height +
                ", eyeColor=" + eyeColor +
                ", hairColor=" + hairColor +
                ", nationality=" + nationality +
                ", location=" + location +
                '}';
    }

    @Override
    public String toString() {
        return  "\t"+ "name           :" + name + '\n' +
                "\t"+ "height         :" + height + "\n"+
                "\t"+ "eyeColor       :" + eyeColor + "\n"+
                "\t"+ "hairColor      :" + hairColor + "\n"+
                "\t"+ "nationality    :" + nationality + "\n"+
                "\t"+ "location       :" + location + "\n";
    }
}
