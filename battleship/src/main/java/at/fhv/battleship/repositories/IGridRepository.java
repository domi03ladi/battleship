package at.fhv.battleship.repositories;

import at.fhv.battleship.entities.Grid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IGridRepository extends JpaRepository<Grid, Integer> {
}
