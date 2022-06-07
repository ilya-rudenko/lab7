package data;

import java.io.Serializable;

public enum Country implements EnumInterface, Serializable {
    RUSSIA("RUSSIA"),
    USA("USA"),
    CHINA("CHINA"),
    ITALY("ITALY"),
    THAILAND("THAILAND");

    private String value;

    Country(String value){
        this.value=value;
    }
    public String getValue(){
        return value;
    }
    public boolean includesInEnum(String str){
        for (Country val:Country.values()){
            if (str.equals(val.getValue())) return true;
        }
        return false;
    }
    public static Country getEnum(String req,boolean nullable){
        if (req==null && nullable) return null;
        for (Country val:Country.values()){
            if (req.equals(val.getValue())) return val;
        }
        return null;
    }

}