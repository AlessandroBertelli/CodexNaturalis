package polimi.ingsw.exception;


public class DuplicatePlayerException extends RuntimeException {

    public DuplicatePlayerException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "Player already exists";
    }
}
