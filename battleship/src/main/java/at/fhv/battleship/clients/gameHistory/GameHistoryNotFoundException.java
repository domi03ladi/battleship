package at.fhv.battleship.clients.gameHistory;

import at.fhv.battleship.dtos.GameHistoryDTO;

public class GameHistoryNotFoundException extends RuntimeException{
    public GameHistoryNotFoundException(String message){
        super(message);
    }
}
