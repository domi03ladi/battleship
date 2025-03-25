package at.fhv.battleship.repositories;

import at.fhv.battleship.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface IGameRepository extends JpaRepository<Game, Integer> {

}
