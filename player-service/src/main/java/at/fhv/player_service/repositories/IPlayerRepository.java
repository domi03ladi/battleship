package at.fhv.player_service.repositories;

import at.fhv.player_service.entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface IPlayerRepository extends JpaRepository<Player, Integer> {

    boolean existsByName(String username);
}
