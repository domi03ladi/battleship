package at.fhv.battleship.mapper;

import at.fhv.battleship.clients.player.PlayerClient;
import at.fhv.battleship.dtos.GameDTO;
import at.fhv.battleship.entities.Game;
import at.fhv.battleship.clients.player.PlayerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameMapper {

    @Autowired
    private PlayerClient playerClient;

    @Autowired
    private GridMapper gridMapper;

    public GameDTO convertToDTO(Game game) throws PlayerNotFoundException {
        GameDTO gameDTO = new GameDTO();
        gameDTO.setId(game.getId());
        gameDTO.setStatus(game.getStatus());
        gameDTO.setPlayer1(playerClient.getPlayerById(game.getPlayer1Id()));
        gameDTO.setPlayer2(playerClient.getPlayerById(game.getPlayer2Id()));
        gameDTO.setGrid(gridMapper.convertToDTO(game.getGrid()));
        return gameDTO;
    }

    public Game convertToEntity(GameDTO gameDTO){
        Game game = new Game();
        if(gameDTO.getId() != null){
            game.setId(gameDTO.getId());
        }
        game.setStatus(gameDTO.getStatus());
        game.setShips(null);
        game.setShots(null);
        game.setPlayer1Id(gameDTO.getPlayer1().getId());
        game.setPlayer2Id(gameDTO.getPlayer2().getId());
        game.setGrid(gridMapper.convertToEntity(gameDTO.getGrid()));
        return game;
    }
}
