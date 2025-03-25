package at.fhv.battleship.clients.player;

public class PlayerValidationException extends RuntimeException{

    public PlayerValidationException(String messages){
        super(messages);
    }
}
