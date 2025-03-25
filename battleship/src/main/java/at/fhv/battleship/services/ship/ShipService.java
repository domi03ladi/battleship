package at.fhv.battleship.services.ship;

import at.fhv.battleship.clients.gameHistory.GameAlreadyFinishedException;
import at.fhv.battleship.clients.gameHistory.GameHistoryClient;
import at.fhv.battleship.dtos.ShipDTO;
import at.fhv.battleship.entities.Game;
import at.fhv.battleship.entities.Ship;
import at.fhv.battleship.mapper.GameMapper;
import at.fhv.battleship.mapper.ShipMapper;
import at.fhv.battleship.repositories.IShipRepository;
import at.fhv.battleship.services.game.GameNotFoundException;
import at.fhv.battleship.services.game.GameService;
import at.fhv.battleship.clients.player.PlayerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShipService {

    @Autowired
    final IShipRepository shipRepository;

    @Autowired
    private ShipMapper shipMapper;

    @Autowired
    private GameMapper gameMapper;

    @Lazy
    final GameService gameService;

    public ShipService(IShipRepository shipRepository, GameService gameService) {
        this.shipRepository = shipRepository;
        this.gameService = gameService;
    }

    public List<ShipDTO> findAll() throws PlayerNotFoundException {
        List<Ship> ships = shipRepository.findAll();
        List<ShipDTO> shipDTOs = new ArrayList<>();
        for(Ship s : ships){
            shipDTOs.add(shipMapper.convertToDTO(s));
        }
        return shipDTOs;
    }

    public List<Ship> findByGameId(Integer id){
        return shipRepository.findByGameId(id);
    }

    public Ship findById(Integer id) throws ShipNotFoundException{
        Optional<Ship> optionalShip = shipRepository.findById(id);
        if(optionalShip.isPresent()){
            return optionalShip.get();
        } else{
            throw new ShipNotFoundException("Ship with Id: " + id + " not found!");
        }
    }

    public ShipDTO create(ShipDTO ship) throws ShipValidationException, GameNotFoundException, PlayerNotFoundException, GameAlreadyFinishedException {
        Game gameToCheck = gameService.findByIdEntity(ship.getGame().getId());
        Ship shipEntity = shipMapper.convertToEntity(ship, gameToCheck);
        gameToCheck.checkForGameStatus();
        gameToCheck.checkForShipPosition(shipEntity);
        shipEntity.setDefaultValues();
        Ship createdShip = shipRepository.save(shipEntity);
        return shipMapper.convertToDTO(createdShip);
    }

    public Ship update(Ship ship){
        return shipRepository.save(ship);
    }

}
