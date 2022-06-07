package data;

import java.io.Serializable;

public enum MovieGenre implements EnumInterface, Serializable {
    ACTION("ACTION"),
    MUSICAL("MUSICAL"),
    THRILLER("THRILLER"),
    HORROR("HORROR"),
    FANTASY("FANTASY");

    private String value;

    MovieGenre(String value){
        this.value=value;
    }
    public String getValue(){
        return value;
    }
    public boolean includesInEnum(String str){
        for (MovieGenre val:MovieGenre.values()){
            if (str.equals(val.getValue())) return true;
        }
        return false;
    }
    public static MovieGenre getEnum(String req,boolean nullable){
        if (req==null && nullable) return null;
        for (MovieGenre val:MovieGenre.values()){
            if (req.equals(val.getValue())) return val;
        }
        return null;
    }
}
