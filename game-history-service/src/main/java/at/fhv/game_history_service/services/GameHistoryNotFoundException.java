package at.fhv.game_history_service.services;

public class GameHistoryNotFoundException extends Exception{
    public GameHistoryNotFoundException(String message){
        super(message);
    }
}
