package at.fhv.battleship.dtos;

public class GridDTO {

    private Integer id;
    private int height;
    private int width;

    public GridDTO() {
    }

    public GridDTO(Integer id, int height, int width) {
        this.id = id;
        this.height = height;
        this.width = width;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

}
