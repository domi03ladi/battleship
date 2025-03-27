package at.fhv.game_history_service.dtos;

import java.time.LocalDateTime;

public record GameHistoryDTO(Integer id, String player1Name, String player2Name, Integer gameId, String winner, LocalDateTime startedAt, LocalDateTime finishedAt) {}
