package at.fhv.battleship.controllers;


import at.fhv.battleship.clients.gameHistory.GameAlreadyFinishedException;
import at.fhv.battleship.clients.gameHistory.GameHistoryNotFoundException;
import at.fhv.battleship.clients.gameHistory.GameHistoryValidationException;
import at.fhv.battleship.clients.player.PlayerNotFoundException;
import at.fhv.battleship.dtos.GameInfoDTO;
import at.fhv.battleship.dtos.ShotDTO;
import at.fhv.battleship.entities.Shot;
import at.fhv.battleship.services.game.GameNotFoundException;
import at.fhv.battleship.services.game.GameService;
import at.fhv.battleship.services.shot.ShotService;
import at.fhv.battleship.services.shot.ShotValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shots")
public class ShotController {

    @Autowired
    final ShotService shotService;
    final GameService gameService;

    public ShotController(ShotService shotService, GameService gameService) {
        this.shotService = shotService;
        this.gameService = gameService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createShot(@RequestBody ShotDTO shot){
        try{
            ShotDTO createdShot = shotService.create(shot);
            GameInfoDTO gameInfoDTO = gameService.getGameInfo(shot.getGame().getId(), "Your shot has been fired!");
            return new ResponseEntity<>(gameInfoDTO, HttpStatus.OK);
        } catch (GameNotFoundException | PlayerNotFoundException | GameHistoryNotFoundException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ShotValidationException | GameHistoryValidationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (GameAlreadyFinishedException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
        }
    }
}
