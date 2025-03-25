package at.fhv.battleship.mapper;

import at.fhv.battleship.clients.player.PlayerClient;
import at.fhv.battleship.clients.player.PlayerNotFoundException;

import at.fhv.battleship.dtos.ShipDTO;
import at.fhv.battleship.dtos.ShotDTO;
import at.fhv.battleship.entities.Game;
import at.fhv.battleship.entities.Shot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShotMapper {
    @Autowired
    private PlayerClient playerClient;

    @Autowired
    private GameMapper gameMapper;

    public ShotDTO convertToDTO(Shot shot) throws PlayerNotFoundException {
        return new ShotDTO( shot.getId(),
                playerClient.getPlayerById(shot.getPlayerId()),
                gameMapper.convertToDTO(shot.getGame()),
                shot.getX(),
                shot.getY(),
                shot.getStatus());
    }


    public Shot convertToEntity(ShotDTO shotDTO, Game game){
        Shot shot = new Shot();
        if(shotDTO.getId() != null){
            shot.setId(shotDTO.getId());
        }
        shot.setStatus(shotDTO.getStatus());
        shot.setGame(game);
        shot.setPlayerId(shotDTO.getPlayer().getId());
        shot.setX(shotDTO.getX());
        shot.setY(shotDTO.getY());
        return shot;
    }
}
