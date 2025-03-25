package at.fhv.game_history_service.dtos;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


import java.time.LocalDateTime;

public class GameHistoryDTO {

    private Integer id;
    private String player1Name;
    private String player2Name;
    private Integer gameId;
    private String winner;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

    public GameHistoryDTO() {
    }

    public GameHistoryDTO(Integer id, String player1Name, String player2Name, Integer gameId, String winner, LocalDateTime startedAt, LocalDateTime finishedAt) {
        this.id = id;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.gameId = gameId;
        this.winner = winner;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlayer1Name() {
        return player1Name;
    }

    public void setPlayer1Name(String player1Name) {
        this.player1Name = player1Name;
    }

    public String getPlayer2Name() {
        return player2Name;
    }

    public void setPlayer2Name(String player2Name) {
        this.player2Name = player2Name;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(LocalDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }
}
