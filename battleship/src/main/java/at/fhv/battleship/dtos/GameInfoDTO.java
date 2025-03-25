package at.fhv.battleship.dtos;

public class GameInfoDTO {

    private String feedBack;
    private String player1Name;
    private String player2Name;
    private String status;
    private char[][] gridInfo;

    public GameInfoDTO(){}

    public String getFeedBack(){
        return this.feedBack;
    }

    public void setFeedBack(String feedBack){
        this.feedBack = feedBack;
    }

    public String getPlayer1Name() {
        return player1Name;
    }

    public void setPlayer1Name(String player1Name) {
        this.player1Name = player1Name;
    }

    public String getPlayer2Name() {
        return player2Name;
    }

    public void setPlayer2Name(String player2Name) {
        this.player2Name = player2Name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public char[][] getGridInfo() {
        return gridInfo;
    }

    public void setGridInfo(char[][] gridInfo) {
        this.gridInfo = gridInfo;
    }
}
