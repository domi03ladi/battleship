package at.fhv.battleship.services.ship;

public class ShipNotFoundException extends Exception{
    public ShipNotFoundException(String message){
        super(message);
    }
}
