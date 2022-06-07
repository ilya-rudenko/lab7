package data;

import java.io.Serializable;

public enum Color implements EnumInterface, Serializable {

    GREEN("GREEN"),
    RED("RED"),
    ORANGE("ORANGE"),
    WHITE("WHITE");

    private String value;

    Color(String value){
        this.value=value;
    }
    public String getValue(){
        if (value==null) return null;
        return value;
    }
    public boolean includesInEnum(String str){
        for (Color val:Color.values()){
            if (str.equals(val.getValue())) return true;
        }
        return false;
    }
    public static Color getEnum(String req,boolean nullable){
        if (req==null && nullable) return null;
        for (Color val:Color.values()){
            if (req.equals(val.getValue())) return val;
        }
        return null;
    }
}
