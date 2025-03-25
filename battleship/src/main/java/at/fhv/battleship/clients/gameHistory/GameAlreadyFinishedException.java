package at.fhv.battleship.clients.gameHistory;

public class GameAlreadyFinishedException extends Exception{
    public GameAlreadyFinishedException(){
        super("The game has already been finished!");
    }
}
