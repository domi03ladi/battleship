package at.fhv.battleship.clients.player;

public class PlayerNotFoundException extends RuntimeException{

    public PlayerNotFoundException(String message){
        super(message);
    }
}
