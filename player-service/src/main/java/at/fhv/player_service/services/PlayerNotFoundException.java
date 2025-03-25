package at.fhv.player_service.services;

public class PlayerNotFoundException extends Exception{

    public PlayerNotFoundException(String messages){
        super(messages);
    }
}
