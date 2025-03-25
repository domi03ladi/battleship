package at.fhv.player_service.services;


import at.fhv.player_service.dtos.PlayerDTO;
import at.fhv.player_service.entities.Player;
import at.fhv.player_service.repositories.IPlayerRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    @Autowired
    private IPlayerRepository playerRepository;

    public List<PlayerDTO> findAll(){
        List<Player> players = playerRepository.findAll();
        List<PlayerDTO> playerDTOs = new ArrayList<>();
        for(Player p: players){
            playerDTOs.add(convertToDTO(p));
        }
        return playerDTOs;
    }

    public PlayerDTO findById(Integer id) throws PlayerNotFoundException{
        Optional<Player> optionalPlayer = playerRepository.findById(id);
        if(optionalPlayer.isPresent()){
            return convertToDTO(optionalPlayer.get());
        } else{
            throw new PlayerNotFoundException("Player with id: " + id + " not found!");
        }
    }

    public PlayerDTO create(PlayerDTO playerDTO) throws PlayerValidationException{
        if(playerRepository.existsByName(playerDTO.getName())){
            throw new PlayerValidationException("A player with the name " + playerDTO.getName() + " already exists!");
        }
        Player savedPlayer = playerRepository.save(convertToEntity(playerDTO));
        return convertToDTO(savedPlayer);
    }

    private PlayerDTO convertToDTO(Player player){
        return new PlayerDTO(player.getId(), player.getName());
    }

    private Player convertToEntity(PlayerDTO playerDTO){
        Player player = new Player();

        if(playerDTO.getId() != null){
            player.setId(playerDTO.getId());
        }
        player.setName(playerDTO.getName());
        return player;
    }
}
