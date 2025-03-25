package at.fhv.battleship.dtos;

public class GameDTO {

    private Integer id;
    private String status;
    private PlayerDTO player1;
    private PlayerDTO player2;
    private GridDTO grid;

    public GameDTO(Integer id, String status, PlayerDTO player1, PlayerDTO player2, GridDTO grid){
        this.id = id;
        this.status = status;
        this.player1 = player1;
        this.player2 = player2;
        this.grid = grid;
    }

    public GameDTO(){

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

    public PlayerDTO getPlayer1() {
        return player1;
    }

    public void setPlayer1(PlayerDTO player1) {
        this.player1 = player1;
    }

    public PlayerDTO getPlayer2() {
        return player2;
    }

    public void setPlayer2(PlayerDTO player2) {
        this.player2 = player2;
    }

    public GridDTO getGrid() {
        return grid;
    }

    public void setGrid(GridDTO grid) {
        this.grid = grid;
    }
}
