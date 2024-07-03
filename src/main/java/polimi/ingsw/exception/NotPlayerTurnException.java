package polimi.ingsw.exception;

public class NotPlayerTurnException extends RuntimeException{

    public NotPlayerTurnException (String message){
        super(message);
    }

    @Override
    public String getMessage() {
        return "It's not your turn now!";
    }

}
