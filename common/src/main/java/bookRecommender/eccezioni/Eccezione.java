package bookRecommender.eccezioni;

import java.io.Serializable;

public class Eccezione implements Serializable{
    private String message;
    private int errorCode;

    public Eccezione(int errorCode, String message){
        this.message = message;
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public int getErrorCode() {
        return errorCode;
    }

}
