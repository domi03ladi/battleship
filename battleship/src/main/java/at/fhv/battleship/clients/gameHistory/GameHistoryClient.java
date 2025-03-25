package at.fhv.battleship.clients.gameHistory;

import at.fhv.battleship.clients.player.PlayerNotFoundException;
import at.fhv.battleship.clients.player.PlayerValidationException;
import at.fhv.battleship.dtos.GameHistoryDTO;
import at.fhv.battleship.dtos.PlayerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class GameHistoryClient {

    @Autowired
    private Resilience4JCircuitBreakerFactory circuitBreakerFactory;

    @Autowired
    private RestTemplate rest;

    private final String URL = "http://localhost:8082/api/game-history";


    public List<GameHistoryDTO> getAllGameHistoryEntries(){
        return circuitBreakerFactory.create("breaker").run(() ->
                rest.getForObject(URL + "/all", List.class));
    }

    public GameHistoryDTO getGameHistoryById(Integer id) throws GameHistoryNotFoundException{
        return circuitBreakerFactory.create("breaker").run(
                () -> rest.getForObject(URL + "/" + id, GameHistoryDTO.class),
                throwable -> { throw new GameHistoryNotFoundException("GameHistory with ID " + id + " not found!"); }
        );
    }

    public GameHistoryDTO getGameHistoryByGameId(Integer id) throws GameHistoryNotFoundException{
        return circuitBreakerFactory.create("breaker").run(
                () -> rest.getForObject(URL + "/game/" + id, GameHistoryDTO.class),
                throwable -> { throw new GameHistoryNotFoundException("GameHistory with gameId " + id + " not found!"); }
        );
    }

    public List<GameHistoryDTO> getAllGameHistoryByPlayerName(String playerName){
        return circuitBreakerFactory.create("breaker").run(() ->
                rest.getForObject(URL + "/all/" + playerName, List.class));
    }

    public List<GameHistoryDTO> getAllWinsByPlayerName(String playerName){
        return circuitBreakerFactory.create("breaker").run(() ->
                rest.getForObject(URL + "/wins/" + playerName, List.class));
    }

    public GameHistoryDTO createGameHistory(GameHistoryDTO gameHistoryDTO) throws GameHistoryValidationException{
        return circuitBreakerFactory.create("breaker").run(() ->
                rest.postForObject(URL + "/create", gameHistoryDTO, GameHistoryDTO.class),
                throwable -> { throw new GameHistoryValidationException("Error during the creation of the gameHistory!"); });
    }

    public GameHistoryDTO updateGameHistory(Integer id, GameHistoryDTO gameHistoryDTO)
            throws GameHistoryNotFoundException, GameHistoryValidationException {

        return circuitBreakerFactory.create("breaker").run(() -> {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<GameHistoryDTO> requestEntity = new HttpEntity<>(gameHistoryDTO, headers);

                ResponseEntity<GameHistoryDTO> response = rest.exchange(
                        URL + "/update/" + id,
                        HttpMethod.PUT,
                        requestEntity,
                        GameHistoryDTO.class
                );

                return response.getBody();
            } catch (HttpClientErrorException.NotFound ex) {
                throw new GameHistoryNotFoundException("GameHistory with ID " + id + " not found!");
            } catch (HttpClientErrorException.BadRequest ex) {
                throw new GameHistoryValidationException("Validation error while updating GameHistory!");
            }
        }, throwable -> {
            throw new GameHistoryValidationException("Error during the update of the gameHistory!");
        });
    }
}
