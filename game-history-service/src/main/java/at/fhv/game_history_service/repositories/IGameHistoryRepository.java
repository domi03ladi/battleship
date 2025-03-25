package at.fhv.game_history_service.repositories;

import at.fhv.game_history_service.dtos.GameHistoryDTO;
import at.fhv.game_history_service.entities.GameHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IGameHistoryRepository extends JpaRepository<GameHistory, Integer> {


    @Query("SELECT g FROM GameHistory g WHERE g.player1Name = :playerName OR g.player2Name= :playerName")
    List<GameHistory> getAllGamesByPlayerName(@Param("playerName") String playerName);

    @Query("SELECT g FROM GameHistory g WHERE g.winner = :playerName")
    List<GameHistory> getAllWinsByPlayerName(@Param("playerName") String playerName);

    @Query("SELECT g FROM GameHistory g WHERE g.gameId = :gameId")
    Optional<GameHistory> getGameHistoriesByGameId(@Param("gameId") Integer gameId);
}
