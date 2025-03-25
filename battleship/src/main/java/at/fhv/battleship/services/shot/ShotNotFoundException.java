package at.fhv.battleship.services.shot;

public class ShotNotFoundException extends Exception{

    public ShotNotFoundException(String message){
        super(message);
    }
}
