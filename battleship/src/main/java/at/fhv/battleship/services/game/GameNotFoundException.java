package at.fhv.battleship.services.game;

public class GameNotFoundException extends Exception{

    public GameNotFoundException(String message){
        super(message);
    }
}
