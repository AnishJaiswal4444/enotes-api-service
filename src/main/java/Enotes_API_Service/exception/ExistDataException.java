package Enotes_API_Service.exception;

public class ExistDataException extends RuntimeException{
    public ExistDataException(String message){
        super(message);
    }
}
