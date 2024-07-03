package polimi.ingsw.exception;

public class LackResourceException extends RuntimeException{

    public LackResourceException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "Lack of resources!";
    }

}
