package at.fhv.battleship.controllers;


import at.fhv.battleship.clients.gameHistory.GameHistoryNotFoundException;
import at.fhv.battleship.clients.gameHistory.GameHistoryValidationException;
import at.fhv.battleship.clients.player.PlayerValidationException;
import at.fhv.battleship.dtos.GameDTO;
import at.fhv.battleship.dtos.GameHistoryDTO;
import at.fhv.battleship.dtos.GameInfoDTO;
import at.fhv.battleship.dtos.PlayerDTO;
import at.fhv.battleship.entities.Game;
import at.fhv.battleship.services.game.GameNotFoundException;
import at.fhv.battleship.services.game.GameService;
import at.fhv.battleship.services.grid.GridNotFoundException;
import at.fhv.battleship.clients.player.PlayerNotFoundException;
import at.fhv.battleship.services.grid.GridValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {

    @Autowired
    final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllGames(){
        try{
            return new ResponseEntity<>(gameService.findAll(), HttpStatus.OK);
        } catch (PlayerNotFoundException pe){
            return new ResponseEntity<>(pe.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGameById(@PathVariable Integer id){
        try{
            return new ResponseEntity<>(gameService.findById(id), HttpStatus.OK);
        } catch(GameNotFoundException | PlayerNotFoundException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<?> getGameInfoById(@PathVariable Integer id){
        try{
            GameInfoDTO gameInfoDTO = gameService.getGameInfo(id, "Game-Info");
            return new ResponseEntity<>(gameInfoDTO, HttpStatus.OK);
        } catch(GameNotFoundException | PlayerNotFoundException | GameHistoryNotFoundException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (GameHistoryValidationException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createGame(@RequestBody GameDTO game){
        try{
            GameDTO createdGame = gameService.create(game);
            return new ResponseEntity<>(createdGame, HttpStatus.CREATED);
        } catch (PlayerNotFoundException | GridNotFoundException pe){
            return new ResponseEntity<>(pe.getMessage(), HttpStatus.NOT_FOUND);
        } catch (GameHistoryValidationException pe){
            return new ResponseEntity<>(pe.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    // Player Microservice

    @PostMapping("/createPlayer")
    public ResponseEntity<?> createPlayer(@RequestBody PlayerDTO player){
        try{
            return new ResponseEntity<>(gameService.createPlayer(player), HttpStatus.CREATED);
        } catch (PlayerValidationException pe){
            return new ResponseEntity<>(pe.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/allPlayers")
    public ResponseEntity<List<PlayerDTO>> getAllPlayers(){
        return new ResponseEntity<>(gameService.getAllPlayers(), HttpStatus.OK);
    }

    @GetMapping("/player/{id}")
    public ResponseEntity<?> getPlayerById(@PathVariable Integer id){
        try{
            return new ResponseEntity<>(gameService.getPlayerById(id), HttpStatus.OK);
        } catch(PlayerNotFoundException pe){
            return new ResponseEntity<>(pe.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    // GameHistory Microservice

    @GetMapping("/history/all")
    public ResponseEntity<List<GameHistoryDTO>> getAllGameHistoryEntries(){
        return new ResponseEntity<>(gameService.getAllGameHistoryEntries(), HttpStatus.OK);
    }

    @GetMapping("/history/{id}")
    public ResponseEntity<?> getGameHistoryById(@PathVariable Integer id){
        try{
            return new ResponseEntity<>(gameService.getGameHistoryById(id), HttpStatus.OK);
        } catch(GameHistoryNotFoundException ge){
            return new ResponseEntity<>(ge.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/history/game/{gameId}")
    public ResponseEntity<?> getGameHistoryByGameId(@PathVariable Integer gameId){
        try{
            return new ResponseEntity<>(gameService.getGameHistoryByGameId(gameId), HttpStatus.OK);
        } catch(GameHistoryNotFoundException ge){
            return new ResponseEntity<>(ge.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/history/player/{playerName}")
    public ResponseEntity<List<GameHistoryDTO>> getAllGamesByPlayerName(@PathVariable String playerName){
        return new ResponseEntity<>(gameService.getAllGamesByPlayerName(playerName), HttpStatus.OK);
    }

    @GetMapping("/history/wins/{playerName}")
    public ResponseEntity<List<GameHistoryDTO>> getAllWinsByPlayerName(@PathVariable String playerName){
        return new ResponseEntity<>(gameService.getAllWinsByPlayerName(playerName), HttpStatus.OK);
    }

}