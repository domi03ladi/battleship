package at.fhv.battleship.mapper;

import at.fhv.battleship.clients.player.PlayerClient;
import at.fhv.battleship.dtos.ShipDTO;
import at.fhv.battleship.entities.Game;
import at.fhv.battleship.entities.Ship;
import at.fhv.battleship.clients.player.PlayerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShipMapper {

    @Autowired
    private PlayerClient playerClient;

    @Autowired
    private GameMapper gameMapper;

    public ShipDTO convertToDTO(Ship ship) throws PlayerNotFoundException {
        return new ShipDTO( ship.getId(),
                            playerClient.getPlayerById(ship.getPlayerId()),
                            gameMapper.convertToDTO(ship.getGame()),
                            ship.getX(),
                            ship.getY(),
                            ship.getStatus());
    }


    public Ship convertToEntity(ShipDTO shipDTO, Game game){
        Ship ship = new Ship();
        if(shipDTO.getId() != null){
            ship.setId(shipDTO.getId());
        }
        ship.setStatus(shipDTO.getStatus());
        ship.setGame(game);
        ship.setPlayerId(shipDTO.getPlayer().getId());
        ship.setX(shipDTO.getX());
        ship.setY(shipDTO.getY());
        return ship;
    }
}
