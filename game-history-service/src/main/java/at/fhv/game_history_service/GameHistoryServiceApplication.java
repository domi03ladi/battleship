package at.fhv.game_history_service;

import at.fhv.battleship.dtos.GameHistoryDTO;
import at.fhv.game_history_service.services.GameHistoryNotFoundException;
import at.fhv.game_history_service.services.GameHistoryService;
import at.fhv.game_history_service.services.GameHistoryValidationException;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@SpringBootApplication
public class GameHistoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GameHistoryServiceApplication.class, args);
	}


	@Bean
	public Consumer<Message<GameHistoryDTO>> receiveGameHistory(GameHistoryService gameHistoryService) {
		return message -> {
			System.out.println("Received GameHistory: " + message.getPayload());
			try {
				gameHistoryService.createGameHistory(message.getPayload());
				System.out.println("GameHistoryService: Message received!");
			} catch (GameHistoryValidationException e) {
				System.err.println("Error during creation: " + e.getMessage());
			}
		};
	}

	@Bean
	public Consumer<Message<GameHistoryDTO>> receiveGameHistoryUpdate(GameHistoryService gameHistoryService) {
		return message -> {
			System.out.println("Received GameHistory for UPDATE: " + message.getPayload());
			try {
				System.out.println("GameHistoryService: Message received!");
				gameHistoryService.updateGameHistory(message.getPayload().id(), message.getPayload());
			} catch (GameHistoryValidationException | GameHistoryNotFoundException e) {
				System.err.println("Error during creation: " + e.getMessage());
			}
		};
	}

	@Bean
	public DirectExchange directExchange(){
		return new DirectExchange("historyExchange");
	}

	@Bean
	public Queue historyMessagesQueue(){
		return new Queue("historyQueue", true);
	}

	@Bean
	public Binding historyRequestQueueBinding(){
		return BindingBuilder.bind(historyMessagesQueue()).to(directExchange()).with("historyRoute");
	}

	@Bean
	public Jackson2JsonMessageConverter jackson2JsonMessageConverter(){
		return new Jackson2JsonMessageConverter();
	}


}
