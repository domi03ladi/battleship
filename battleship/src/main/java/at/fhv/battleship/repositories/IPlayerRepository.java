package at.fhv.battleship.repositories;

import at.fhv.battleship.entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPlayerRepository extends JpaRepository<Player, Integer> {

}
