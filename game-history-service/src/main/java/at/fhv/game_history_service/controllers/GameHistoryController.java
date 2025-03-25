package at.fhv.game_history_service.controllers;

import at.fhv.game_history_service.dtos.GameHistoryDTO;
import at.fhv.game_history_service.services.GameHistoryNotFoundException;
import at.fhv.game_history_service.services.GameHistoryService;
import at.fhv.game_history_service.services.GameHistoryValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/game-history")
public class GameHistoryController {

    @Autowired
    GameHistoryService service;

    @GetMapping("/all")
    public ResponseEntity<List<GameHistoryDTO>> getAllGameHistory(){
        return new ResponseEntity<>(service.getAllGameHistories(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGameHistoryById(@PathVariable Integer id){
        try{
            return new ResponseEntity<>(service.getGameHistoryById(id),HttpStatus.OK);
        } catch(GameHistoryNotFoundException ge){
            return new ResponseEntity<>(ge.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all/{playerName}")
    public ResponseEntity<List<GameHistoryDTO>> getAllGameHistoryByPlayerName(@PathVariable String playerName){
        return new ResponseEntity<>(service.getAllGamesByPlayerName(playerName), HttpStatus.OK);
    }

    @GetMapping("/wins/{playerName}")
    public ResponseEntity<List<GameHistoryDTO>> getAllWinsByPlayerName(@PathVariable String playerName){
        return new ResponseEntity<>(service.getAllWinsByPlayerName(playerName), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createGameHistory(@RequestBody GameHistoryDTO gameHistoryRequest){
        try{
            return new ResponseEntity<>(service.createGameHistory(gameHistoryRequest), HttpStatus.CREATED);
        } catch (GameHistoryValidationException ge){
            return new ResponseEntity<>(ge.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateGameHistory(@PathVariable Integer id, @RequestBody GameHistoryDTO gameHistoryDTO) {
        try {
            return ResponseEntity.ok(service.updateGameHistory(id, gameHistoryDTO));
        } catch (GameHistoryNotFoundException ge) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ge.getMessage());
        } catch (GameHistoryValidationException ge) {
            return ResponseEntity.badRequest().body(ge.getMessage());
        }
    }

    @GetMapping("/game/{id}")
    public ResponseEntity<?> getGameHistoryByGameId(@PathVariable Integer id){
        try{
            return ResponseEntity.ok(service.getGameHistoryByGameId(id));
        } catch (GameHistoryNotFoundException ge){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ge.getMessage());
        }
    }
}
