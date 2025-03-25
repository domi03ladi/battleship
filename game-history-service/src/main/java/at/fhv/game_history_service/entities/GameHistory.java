package at.fhv.game_history_service.entities;

import at.fhv.game_history_service.services.GameHistoryValidationException;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name ="game_history", schema = "game_history_db")
public class GameHistory {

    public GameHistory() {
    }

    public GameHistory(Integer id, String player1Name, String player2Name, Integer gameId, String winner, LocalDateTime startedAt, LocalDateTime finishedAt) {
        this.id = id;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.gameId = gameId;
        this.winner = winner;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "player_1_name", nullable = false)
    private String player1Name;

    @Column(name = "player_2_name", nullable = false)
    private String player2Name;

    @Column(name = "game_id", nullable = false)
    private Integer gameId;

    @Column(name = "winner")
    private String winner;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;


    public void validateGameHistory() throws GameHistoryValidationException{
        checkForValidPlayerPair();
        checkForValidWinner();
    }

    private void checkForValidPlayerPair() throws GameHistoryValidationException {
        if (player1Name == null || player2Name == null) {
            throw new GameHistoryValidationException("Player names cannot be null!");
        }
        if (player1Name.equals(player2Name)) {
            throw new GameHistoryValidationException("Please assign two different Players to the game!");
        }
    }

    private void checkForValidWinner() throws GameHistoryValidationException {
        if(winner != null){
            if(!winner.equals(player1Name) && !winner.equals(player2Name)){
                throw new GameHistoryValidationException("The winner has to be one of the two players!");
            }
        }
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
