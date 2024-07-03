package polimi.ingsw.SocketAndRMI.message;

public class SecretAndStartingResponseMessage extends Message {
    String choice;
    boolean side;
    public SecretAndStartingResponseMessage(String nickname, String choice, boolean side) {
        super(nickname,MessageType.SECRET_STARTING_RESPONSE);
        this.choice = choice;
        this.side = side;
    }

    public String getChoice() {
        return choice;
    }

    public boolean getSide() {
        return side;
    }

    @Override
    public String toString() {
        return "SecretTargetAndStartingRequestMessage{" +
                "choice='" + choice + '\'' +
                ", side='" + side + '\'' +
                ", messageType=" + messageType +
                '}';
    }
}
