package at.fhv.battleship.services.game;

import at.fhv.battleship.clients.gameHistory.GameHistoryClient;
import at.fhv.battleship.clients.gameHistory.GameHistoryNotFoundException;
import at.fhv.battleship.clients.gameHistory.GameHistoryValidationException;
import at.fhv.battleship.clients.player.PlayerClient;
import at.fhv.battleship.clients.player.PlayerValidationException;
import at.fhv.battleship.dtos.GameDTO;
import at.fhv.battleship.dtos.GameHistoryDTO;
import at.fhv.battleship.dtos.GameInfoDTO;
import at.fhv.battleship.dtos.PlayerDTO;
import at.fhv.battleship.entities.*;
import at.fhv.battleship.mapper.GameMapper;
import at.fhv.battleship.mapper.GridMapper;
import at.fhv.battleship.repositories.IGameRepository;
import at.fhv.battleship.services.grid.GridNotFoundException;
import at.fhv.battleship.services.grid.GridService;
import at.fhv.battleship.clients.player.PlayerNotFoundException;
import at.fhv.battleship.services.player.PlayerService;
import at.fhv.battleship.services.ship.ShipService;
import at.fhv.battleship.services.shot.ShotService;
import jakarta.transaction.Transactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    private RabbitTemplate rabbitTemplate;

    @Autowired
    private StreamBridge streamBridge;

    @Autowired
    final IGameRepository gameRepository;

    @Lazy
    final ShipService shipService;

    @Lazy
    final ShotService shotService;

    @Autowired
    private PlayerClient playerClient;

    @Autowired
    private GameMapper gameMapper;

    @Autowired
    private GridMapper gridMapper;

    @Autowired
    private GameHistoryClient gameHistoryClient;

    @Lazy
    final GridService gridService;

    public GameService(IGameRepository gameRepository, @Lazy ShipService shipService, @Lazy ShotService shotService, @Lazy PlayerService playerService, @Lazy GridService gridService, RabbitTemplate rabbitTemplate) {
        this.gameRepository = gameRepository;
        this.shipService = shipService;
        this.shotService = shotService;
        this.gridService = gridService;
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setReplyTimeout(4000);
        this.rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
    }

    public List<GameDTO> findAll() throws PlayerNotFoundException {
        List<Game> games = gameRepository.findAll();
        List<GameDTO> gameDTOs = new ArrayList<>();
        for(Game g : games){
            gameDTOs.add(gameMapper.convertToDTO(g));
        }
        return gameDTOs;
    }

    public GameDTO findById(Integer id) throws GameNotFoundException, PlayerNotFoundException {
        Optional<Game> optionalGame = gameRepository.findById(id);
        if (optionalGame.isPresent()) {
            return gameMapper.convertToDTO(optionalGame.get());
        } else {
            throw new GameNotFoundException("Gane with the Id: " + id + " not found!");
        }
    }

    public Game findByIdEntity(Integer id) throws GameNotFoundException, PlayerNotFoundException {
        Optional<Game> optionalGame = gameRepository.findById(id);
        if (optionalGame.isPresent()) {
            return optionalGame.get();
        } else {
            throw new GameNotFoundException("Gane with the Id: " + id + " not found!");
        }
    }

    public GameDTO create(GameDTO game) throws PlayerNotFoundException, GridNotFoundException, GameHistoryValidationException {
        game.setGrid(gridService.findById(game.getGrid().getId()));
        Game gameEntity = gameMapper.convertToEntity(game);
        gameEntity.setDefaultValues();
        String player1Name = playerClient.getPlayerById(game.getPlayer1().getId()).getName();
        String player2Name = playerClient.getPlayerById(game.getPlayer2().getId()).getName();

        Game createdGame = gameRepository.save(gameEntity);
        GameHistoryDTO gameHistoryDTO = new GameHistoryDTO(null,
                player1Name,
                player2Name,
                createdGame.getId(),
                null,
                LocalDateTime.now(),
                null);

        //gameHistoryClient.createGameHistory(gameHistoryDTO);
        streamBridge.send("gameHistoryTopic", gameHistoryDTO);

        return gameMapper.convertToDTO(createdGame);
    }

    @Transactional
    public GameInfoDTO getGameInfo(Integer gameId, String feedback) throws GameNotFoundException, PlayerNotFoundException, GameHistoryNotFoundException, GameHistoryValidationException {
        GameDTO game = findById(gameId);
        List<Ship> ships = shipService.findByGameId(gameId);
        List<Shot> shots = shotService.findByGameId(gameId);
        GameHistoryDTO currentGameHistory = gameHistoryClient.getGameHistoryByGameId(gameId);

        // variables for tracking the winner
        int shipsAlivePlayer1 = 0;
        int shipsAlivePlayer2 = 0;
        boolean player1HasShips = false;
        boolean player2HasShips = false;

        GameInfoDTO gameInfoDTO = new GameInfoDTO();
        gameInfoDTO.setFeedBack(feedback);
        gameInfoDTO.setPlayer1Name(game.getPlayer1().getName());
        gameInfoDTO.setPlayer2Name(game.getPlayer2().getName());

        int width = game.getGrid().getWidth();
        int height = game.getGrid().getHeight();

        char[][] grid = new char[height][width];

        //fill array with waves
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = '~';
            }
        }

        for (Ship s : ships) {
            // place ships in the grid
            if (s.getPlayerId() == game.getPlayer1().getId()) {
                player1HasShips = true;
                grid[s.getY()][s.getX()] = 's';
            } else {
                grid[s.getY()][s.getX()] = 'u';
                player2HasShips = true;
            }

            // check if one player has already lost
            if ((s.getPlayerId() == game.getPlayer1().getId()) && s.getStatus().equals("alive")) {
                shipsAlivePlayer1++;
            } else if ((s.getPlayerId() == game.getPlayer2().getId()) && s.getStatus().equals("alive")) {
                shipsAlivePlayer2++;
            }
        }

        for (Shot s : shots) {
            if (s.getPlayerId() == game.getPlayer1().getId()) {
                grid[s.getY()][s.getX()] = 'x';
            } else {
                grid[s.getY()][s.getX()] = 'y';
            }
        }

        gameInfoDTO.setGridInfo(grid);

        //if one count is zero - change game status
        if (player1HasShips && player2HasShips) {
            if (shipsAlivePlayer1 == 0) {
                GameHistoryDTO updatedGameHistoryDTO = new GameHistoryDTO(currentGameHistory.id(),
                        currentGameHistory.player1Name(),
                        currentGameHistory.player2Name(),
                        currentGameHistory.gameId(),
                        currentGameHistory.player2Name(),
                        currentGameHistory.startedAt(),
                        LocalDateTime.now());
                game.setStatus("Game over - Player 2 won");
                this.gameRepository.save(gameMapper.convertToEntity(game));
                streamBridge.send("gameHistoryUpdateTopic", updatedGameHistoryDTO);
            } else if (shipsAlivePlayer2 == 0) {
                game.setStatus("Game over - Player 1 won");
                GameHistoryDTO updatedGameHistoryDTO = new GameHistoryDTO(currentGameHistory.id(),
                        currentGameHistory.player1Name(),
                        currentGameHistory.player2Name(),
                        currentGameHistory.gameId(),
                        currentGameHistory.player1Name(),
                        currentGameHistory.startedAt(),
                        LocalDateTime.now());
                this.gameRepository.save(gameMapper.convertToEntity(game));
                streamBridge.send("gameHistoryUpdateTopic", updatedGameHistoryDTO);
            }
        }

        gameInfoDTO.setStatus(game.getStatus());

        return gameInfoDTO;
    }

    // Player Microservice

    public PlayerDTO createPlayer(PlayerDTO playerDTO) throws PlayerValidationException {
        return playerClient.createPlayer(playerDTO);
    }

    public List<PlayerDTO> getAllPlayers() {
        return playerClient.getAllPlayers();
    }

    public PlayerDTO getPlayerById(Integer id) throws PlayerNotFoundException {
        return playerClient.getPlayerById(id);
    }

    // Game-History Microservice

    public List<GameHistoryDTO> getAllGameHistoryEntries(){
        return gameHistoryClient.getAllGameHistoryEntries();
    }

    public GameHistoryDTO getGameHistoryById(Integer id) throws GameHistoryNotFoundException {
        Object response = rabbitTemplate.convertSendAndReceive("historyExchange", "historyRoute", id);
        return (GameHistoryDTO) response;
    }

    public GameHistoryDTO getGameHistoryByGameId(Integer id) throws GameHistoryNotFoundException {
        return gameHistoryClient.getGameHistoryByGameId(id);
    }

    public List<GameHistoryDTO> getAllGamesByPlayerName(String name){
        return gameHistoryClient.getAllGameHistoryByPlayerName(name);
    }

    public List<GameHistoryDTO> getAllWinsByPlayerName(String name){
        return gameHistoryClient.getAllWinsByPlayerName(name);
    }
}
