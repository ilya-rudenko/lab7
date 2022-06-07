package data;

import java.io.Serializable;

public enum MpaaRating implements EnumInterface, Serializable {
    G("G"),
    PG("PG"),
    PG_13("PG_13");

    private String value;

    MpaaRating(String value){
        this.value=value;
    }
    public String getValue(){
        if (value==null) return null;
        return value;
    }
    public boolean includesInEnum(String str){
        for (MpaaRating val:MpaaRating.values()){
            if (str.equals(val.getValue())) return true;
        }
        return false;
    }
    public static MpaaRating getEnum(String req,boolean nullable){
        if (req==null && nullable) return null;
        for (MpaaRating val:MpaaRating.values()){
            if (req.equals(val.getValue())) return val;
        }
        return null;
    }
}
