package se.eostre.apistogramma;

public class Status extends Exception {

    private static final long serialVersionUID = 5114127077449598769L;
    
    int code = 200;
    
    public Status(String message) {
        super(message);
    }
    
    public Status(String message, int code) {
        super(message);
        this.code = code;
    }
    
    public Status(String message, int code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

}
