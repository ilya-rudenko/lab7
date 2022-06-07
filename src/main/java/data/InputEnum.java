package data;

public enum InputEnum implements EnumInterface {
    LOGIN("login"),
    SIGN_IN("sign_in");

    private String value;

    InputEnum(String value){
        this.value=value;
    }
    public String getValue(){
        return value;
    }
    public boolean includesInEnum(String str){
        for (InputEnum val:InputEnum.values()){
            if (str.equals(val.getValue())) return true;
        }
        return false;
    }
    public static InputEnum getEnum(String req,boolean nullable){
        if (req==null && nullable) return null;
        for (InputEnum val:InputEnum.values()){
            if (req.equals(val.getValue())) return val;
        }
        return null;
    }
}
