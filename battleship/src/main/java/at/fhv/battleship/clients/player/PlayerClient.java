package at.fhv.battleship.clients.player;

import at.fhv.battleship.dtos.PlayerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class PlayerClient {

    @Autowired
    private Resilience4JCircuitBreakerFactory circuitBreakerFactory;

    @Autowired
    private RestTemplate rest;
    private final String URL = "http://localhost:8081/api/players";

    public PlayerDTO getPlayerById(Integer id) {
        return circuitBreakerFactory.create("breaker").run(
                () -> rest.getForObject(URL + "/" + id, PlayerDTO.class),
                throwable -> { throw new PlayerNotFoundException("Player mit ID " + id + " nicht gefunden!"); }
        );
    }

    public PlayerDTO createPlayer(PlayerDTO player) throws PlayerValidationException {
        return circuitBreakerFactory.create("breaker").run(() ->
                        rest.postForObject(URL + "/create", player, PlayerDTO.class),
                throwable -> { throw new PlayerValidationException("Error during the creation of the player!"); });
    }

    public List<PlayerDTO> getAllPlayers(){
        return circuitBreakerFactory.create("breaker").run(() ->
                rest.getForObject(URL + "/all", List.class));
    }


/*    public PlayerDTO getPlayerById(Integer id) throws PlayerNotFoundException {
        try{
            return circuitBreakerFactory.create("breaker").run(() ->
                    rest.getForObject(URL + "/" + id, PlayerDTO.class));
        } catch(NoFallbackAvailableException ex){
            throw new PlayerNotFoundException(ex.getCause().getMessage());
        }
    }*/

/*    public PlayerDTO createPlayer(PlayerDTO player) throws PlayerValidationException {
        try{
            return rest.postForObject(URL + "/create", player, PlayerDTO.class);
        } catch(HttpClientErrorException.BadRequest ex){
            throw new PlayerValidationException(ex.getMessage());
        }
    }*/

}
