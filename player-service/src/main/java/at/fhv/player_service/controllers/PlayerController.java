package at.fhv.player_service.controllers;


import at.fhv.player_service.dtos.PlayerDTO;
import at.fhv.player_service.services.PlayerNotFoundException;
import at.fhv.player_service.services.PlayerService;
import at.fhv.player_service.services.PlayerValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @GetMapping("/all")
    public ResponseEntity<List<PlayerDTO>> findAllPlayers(){
        return new ResponseEntity<>(playerService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPlayerById(@PathVariable Integer id){
        try {
            return new ResponseEntity<>(playerService.findById(id),HttpStatus.OK);
        } catch (PlayerNotFoundException p){
            return new ResponseEntity<>(p.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPlayer(@RequestBody PlayerDTO player) {
        try{
            return new ResponseEntity<>(playerService.create(player), HttpStatus.CREATED);
        } catch (PlayerValidationException pe){
            return new ResponseEntity<>(pe.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
