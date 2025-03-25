package at.fhv.battleship.controllers;

import at.fhv.battleship.dtos.GridDTO;
import at.fhv.battleship.entities.Grid;
import at.fhv.battleship.services.grid.GridNotFoundException;
import at.fhv.battleship.services.grid.GridService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grids")
public class GridController {

    @Autowired
    final GridService gridService;

    public GridController(GridService gridService) {
        this.gridService = gridService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<GridDTO>> getAllGrids(){
        return new ResponseEntity<>(gridService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGridById(@PathVariable Integer id){
        try{
            return new ResponseEntity<>(gridService.findById(id), HttpStatus.OK);
        } catch (GridNotFoundException ge) {
            return new ResponseEntity<>(ge.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<GridDTO> createGrid(@RequestBody Grid grid){
        return new ResponseEntity<>(gridService.create(grid), HttpStatus.CREATED);
    }

}
