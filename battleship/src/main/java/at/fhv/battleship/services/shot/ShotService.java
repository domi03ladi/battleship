package at.fhv.battleship.services.shot;

import at.fhv.battleship.clients.gameHistory.GameAlreadyFinishedException;
import at.fhv.battleship.clients.player.PlayerNotFoundException;
import at.fhv.battleship.dtos.GameDTO;
import at.fhv.battleship.dtos.ShotDTO;
import at.fhv.battleship.entities.Game;
import at.fhv.battleship.entities.Ship;
import at.fhv.battleship.entities.Shot;
import at.fhv.battleship.mapper.GameMapper;
import at.fhv.battleship.mapper.ShotMapper;
import at.fhv.battleship.repositories.IShotRepository;
import at.fhv.battleship.services.game.GameNotFoundException;
import at.fhv.battleship.services.game.GameService;
import at.fhv.battleship.services.ship.ShipService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShotService {

    @Autowired
    final IShotRepository shotRepository;

    @Autowired
    private GameMapper gameMapper;

    @Autowired
    private ShotMapper shotMapper;

    @Lazy
    final GameService gameService;

    final ShipService shipService;

    public ShotService(IShotRepository shotRepository, GameService gameService, ShipService shipService) {
        this.shotRepository = shotRepository;
        this.gameService = gameService;
        this.shipService = shipService;
    }

    public List<Shot> findAll(){
        return shotRepository.findAll();
    }

    public List<Shot> findByGameId(Integer id){
        return shotRepository.findByGameId(id);
    }

    public Shot findById(Integer id) throws ShotNotFoundException{
        Optional<Shot> optionalShot = shotRepository.findById(id);
        if(optionalShot.isPresent()){
            return optionalShot.get();
        } else{
            throw new ShotNotFoundException("Shot with Id: " + id + " not Found!");
        }
    }

    @Transactional
    public ShotDTO create(ShotDTO shot) throws GameNotFoundException, ShotValidationException, PlayerNotFoundException, GameAlreadyFinishedException {

        Game gameToCheckEntity = gameService.findByIdEntity(shot.getGame().getId());
        gameToCheckEntity.checkForGameStatus();
        gameToCheckEntity.checkForShotPosition(shot);
        for(Ship s: gameToCheckEntity.getShips()){
            if(s.getX() == shot.getX() && s.getY() == shot.getY()){
                if(s.getPlayerId() == shot.getPlayer().getId()){
                    throw new ShotValidationException("You cannot shot your own ship!");
                } else{
                    shot.setStatus("Hit!");
                    s.setStatus("destroyed");
                    shipService.update(s);
                    break;
                }
            }
        }
        Shot shotEntity = shotMapper.convertToEntity(shot, gameToCheckEntity);
        shotEntity.setDefaultValues();
        return shotMapper.convertToDTO(shotRepository.save(shotEntity));
    }
}
