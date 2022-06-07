package exceptions;

public class NoSuchCommandException extends Exception {
    public NoSuchCommandException(){
        super("There is no such command");
    }
}
