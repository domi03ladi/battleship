package at.fhv.player_service.services;

public class PlayerValidationException extends Exception{

    public PlayerValidationException(String messages){
        super(messages);
    }
}
