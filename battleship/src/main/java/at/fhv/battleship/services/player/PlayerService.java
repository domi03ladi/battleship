package at.fhv.battleship.services.player;

import at.fhv.battleship.clients.player.PlayerNotFoundException;
import at.fhv.battleship.entities.Player;
import at.fhv.battleship.repositories.IPlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    @Autowired
    final IPlayerRepository playerRepository;

    public PlayerService(IPlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> findAll(){
        return playerRepository.findAll();
    }

    public Player findById(Integer id) throws PlayerNotFoundException {
        Optional<Player> optionalPlayer = playerRepository.findById(id);
        if(optionalPlayer.isPresent()){
            return optionalPlayer.get();
        } else{
            throw new PlayerNotFoundException("Player with id: " + id + " not found!");
        }
    }

    public Player create(Player player){
        return playerRepository.save(player);
    }

}
