package at.fhv.battleship.repositories;

import at.fhv.battleship.entities.Ship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IShipRepository extends JpaRepository<Ship, Integer> {

    List<Ship> findByGameId(Integer gameId);
}
