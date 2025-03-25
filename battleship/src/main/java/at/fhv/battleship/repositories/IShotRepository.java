package at.fhv.battleship.repositories;

import at.fhv.battleship.entities.Shot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IShotRepository extends JpaRepository<Shot, Integer> {

    List<Shot> findByGameId(Integer id);
}
