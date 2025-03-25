package at.fhv.battleship.entities;

import at.fhv.battleship.clients.gameHistory.GameAlreadyFinishedException;
import at.fhv.battleship.dtos.ShotDTO;
import at.fhv.battleship.services.ship.ShipValidationException;
import at.fhv.battleship.services.shot.ShotValidationException;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "game", schema = "battleship")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "player_1_id", nullable = false)
    private Integer player1Id;

    @Column(name = "player_2_id", nullable = false)
    private Integer player2Id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "grid_id", nullable = false)
    private Grid grid;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference("game-ships")
    private List<Ship> ships = new ArrayList<>();

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference("game-shots")
    private List<Shot> shots = new ArrayList<>();

    public void checkForShipPosition(Ship ship) throws ShipValidationException {
        if(ship.getX() >= this.getGrid().getWidth() || ship.getY() >= this.getGrid().getHeight()){
            throw new ShipValidationException("Enter valid coordinates for your ship!");
        }

            for(Ship s : ships){
                if(s.getY() == ship.getY() && s.getX() == ship.getX()){
                    throw new ShipValidationException("There is already a ship at these coordinates!");
                }
            }

            for(Shot s : shots){
                if(s.getY() == ship.getY() && s.getX() == ship.getX()){
                    throw new ShipValidationException("These coordinates have already been shot at, choose another field!");
                }
            }
    }

    public void checkForGameStatus() throws GameAlreadyFinishedException {
        if(!status.equals("ongoing")){
            throw new GameAlreadyFinishedException();
        }
    }

    public void checkForShotPosition(ShotDTO shot) throws ShotValidationException {
        if(shot.getX() >= this.getGrid().getWidth() || shot.getY() >= this.getGrid().getHeight()){
            throw new ShotValidationException("Enter valid coordinates for your shot!");
        }

        for(Shot s : shots){
            if(s.getY() == shot.getY() && s.getX() == shot.getX()){
                throw new ShotValidationException("These coordinates have already been shot at, choose another field!");
            }
        }
    }

    public void setDefaultValues(){
        this.status = "ongoing";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPlayer1Id() {
        return player1Id;
    }

    public void setPlayer1Id(Integer player1Id) {
        this.player1Id = player1Id;
    }

    public Integer getPlayer2Id() {
        return player2Id;
    }

    public void setPlayer2Id(Integer player2Id) {
        this.player2Id = player2Id;
    }

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public void setShips(List<Ship> ships) {
        this.ships = ships;
    }

    public List<Shot> getShots() {
        return shots;
    }

    public void setShots(List<Shot> shots) {
        this.shots = shots;
    }
}