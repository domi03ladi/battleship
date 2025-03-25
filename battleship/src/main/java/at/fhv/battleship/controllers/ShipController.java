package at.fhv.battleship.controllers;

import at.fhv.battleship.clients.gameHistory.GameAlreadyFinishedException;
import at.fhv.battleship.clients.gameHistory.GameHistoryNotFoundException;
import at.fhv.battleship.clients.gameHistory.GameHistoryValidationException;
import at.fhv.battleship.clients.player.PlayerNotFoundException;
import at.fhv.battleship.dtos.GameInfoDTO;
import at.fhv.battleship.dtos.ShipDTO;
import at.fhv.battleship.entities.Ship;
import at.fhv.battleship.services.game.GameNotFoundException;
import at.fhv.battleship.services.game.GameService;
import at.fhv.battleship.services.ship.ShipService;
import at.fhv.battleship.services.ship.ShipValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ships")
public class ShipController {

    @Autowired
    final ShipService shipService;
    final GameService gameService;

    public ShipController(ShipService shipService, GameService gameService) {
        this.shipService = shipService;
        this.gameService = gameService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createShip(@RequestBody ShipDTO ship) {
        try {
            ShipDTO createdShip = shipService.create(ship);
            GameInfoDTO gameInfoDTO = gameService.getGameInfo(createdShip.getGame().getId(), "Ship was successfully placed!");
            return new ResponseEntity<>(gameInfoDTO, HttpStatus.OK);
        } catch (ShipValidationException | GameHistoryValidationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (GameNotFoundException | PlayerNotFoundException | GameHistoryNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (GameAlreadyFinishedException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
        }
    }
}
