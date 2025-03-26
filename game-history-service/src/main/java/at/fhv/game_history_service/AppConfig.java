package at.fhv.game_history_service;

import at.fhv.game_history_service.dtos.GameHistoryDTO;
import at.fhv.game_history_service.services.GameHistoryService;
import at.fhv.game_history_service.services.GameHistoryValidationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class AppConfig {


    @Bean
    public Consumer<GameHistoryDTO> receiveGameHistory(GameHistoryService gameHistoryService) {
        return gameHistoryDTO -> {
            System.out.println("Empfange GameHistory: " + gameHistoryDTO);
            try {
                System.out.println("GameHistoryService: Message received!");
                gameHistoryService.createGameHistory(gameHistoryDTO);
            } catch (GameHistoryValidationException e) {
                System.err.println("Error during creation: " + e.getMessage());
            }
        };
    }



}
