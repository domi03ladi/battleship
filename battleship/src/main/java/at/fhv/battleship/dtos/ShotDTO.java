package at.fhv.battleship.dtos;

public class ShotDTO {

    private Integer id;
    private PlayerDTO player;
    private GameDTO game;
    private int x;
    private int y;
    private String status;

    public ShotDTO() {
    }

    public ShotDTO(Integer id, PlayerDTO player, GameDTO game, int x, int y, String status) {
        this.id = id;
        this.player = player;
        this.game = game;
        this.x = x;
        this.y = y;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PlayerDTO getPlayer() {
        return player;
    }

    public void setPlayer(PlayerDTO player) {
        this.player = player;
    }

    public GameDTO getGame() {
        return game;
    }

    public void setGame(GameDTO game) {
        this.game = game;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
