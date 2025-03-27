package at.fhv.game_history_service.services;

import at.fhv.battleship.dtos.GameHistoryDTO;
import at.fhv.game_history_service.entities.GameHistory;
import at.fhv.game_history_service.repositories.IGameHistoryRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GameHistoryService {

    @Autowired
    private IGameHistoryRepository repository;

    public List<GameHistoryDTO> getAllGameHistories() {
        List<GameHistory> gameHistories = repository.findAll();
        List<GameHistoryDTO> gameHistoryDTOS = new ArrayList<>();
        for (GameHistory g : gameHistories) {
            gameHistoryDTOS.add(convertToDTO(g));
        }
        return gameHistoryDTOS;
    }

    @RabbitListener(queues = "historyQueue")
    public GameHistoryDTO getGameHistoryById(Integer id) throws GameHistoryNotFoundException{
        Optional<GameHistory> gameHistoryOptional = repository.findById(id);
        if(gameHistoryOptional.isPresent()){
            return convertToDTO(gameHistoryOptional.get());
        } else {
            throw new GameHistoryNotFoundException("The GameHistory with id " + id + " doesn't exists!");
        }
    }

/*    @RabbitListener(queues = "historyQueue")
    public Integer getGameHistoryById(Integer id){
        System.out.println("Received Data: " + id);
        return id;
    }*/

    public GameHistoryDTO getGameHistoryByGameId(Integer id) throws GameHistoryNotFoundException{
        Optional<GameHistory> gameHistoryOptional = repository.getGameHistoriesByGameId(id);
        if(gameHistoryOptional.isPresent()){
            return convertToDTO(gameHistoryOptional.get());
        } else {
            throw new GameHistoryNotFoundException("The GameHistory with gameId " + id + " doesn't exists!");
        }
    }

    public List<GameHistoryDTO> getAllGamesByPlayerName(String name){
        List<GameHistory> gameHistories = repository.getAllGamesByPlayerName(name);
        List<GameHistoryDTO> gameHistoryDTOS = new ArrayList<>();
        for(GameHistory g : gameHistories){
            gameHistoryDTOS.add(convertToDTO(g));
        }
        return gameHistoryDTOS;
    }

    public List<GameHistoryDTO> getAllWinsByPlayerName(String name){
        List<GameHistory> gameHistories = repository.getAllWinsByPlayerName(name);
        List<GameHistoryDTO> gameHistoryDTOS = new ArrayList<>();
        for(GameHistory g : gameHistories){
            gameHistoryDTOS.add(convertToDTO(g));
        }
        return gameHistoryDTOS;
    }
    
    public GameHistoryDTO createGameHistory(GameHistoryDTO gameHistoryDTO) throws GameHistoryValidationException{
        GameHistory gameHistory = convertToEntity(gameHistoryDTO);
        gameHistory.validateGameHistory();
        GameHistory savedGame = repository.save(gameHistory);
        return convertToDTO(savedGame);
    }

    public GameHistoryDTO updateGameHistory(Integer id, GameHistoryDTO gameHistoryDTO) throws GameHistoryNotFoundException, GameHistoryValidationException {
        GameHistory existingGameHistory = repository.findById(id)
                .orElseThrow(() -> new GameHistoryNotFoundException("GameHistory with ID " + id + " not found!"));

        existingGameHistory.setPlayer1Name(gameHistoryDTO.player1Name());
        existingGameHistory.setPlayer2Name(gameHistoryDTO.player2Name());
        existingGameHistory.setWinner(gameHistoryDTO.winner());
        existingGameHistory.setGameId(gameHistoryDTO.gameId());
        existingGameHistory.setStartedAt(gameHistoryDTO.startedAt());
        existingGameHistory.setFinishedAt(gameHistoryDTO.finishedAt());

        existingGameHistory.validateGameHistory();

        GameHistory updatedGame = repository.save(existingGameHistory);
        return convertToDTO(updatedGame);
    }


    private GameHistoryDTO convertToDTO(GameHistory gameHistory) {
        return new GameHistoryDTO(gameHistory.getId(),
                gameHistory.getPlayer1Name(),
                gameHistory.getPlayer2Name(),
                gameHistory.getGameId(),
                gameHistory.getWinner(),
                gameHistory.getStartedAt(),
                gameHistory.getFinishedAt());
    }

    private GameHistory convertToEntity(GameHistoryDTO gameHistoryDTO){
        GameHistory gameHistory = new GameHistory();
        if(gameHistoryDTO.id() != null){
            gameHistory.setId(gameHistoryDTO.id());
        }
        gameHistory.setPlayer1Name(gameHistoryDTO.player1Name());
        gameHistory.setPlayer2Name(gameHistoryDTO.player2Name());
        gameHistory.setGameId(gameHistoryDTO.gameId());
        gameHistory.setWinner(gameHistoryDTO.winner());
        gameHistory.setStartedAt(gameHistoryDTO.startedAt());
        gameHistory.setFinishedAt(gameHistoryDTO.finishedAt());

        return gameHistory;
    }

}
