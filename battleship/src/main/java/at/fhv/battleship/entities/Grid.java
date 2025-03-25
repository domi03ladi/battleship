package at.fhv.battleship.entities;

import at.fhv.battleship.services.grid.GridValidationException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "grid", schema = "battleship")
@NoArgsConstructor @AllArgsConstructor

public class Grid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "width", nullable = false)
    private Integer width;

    @Column(name = "height", nullable = false)
    private Integer height;

    public Integer getId() {
        return id;
    }

    public void validate() throws GridValidationException{
        validateWidth();
        validateHeight();
    }

    private void validateWidth() throws GridValidationException {
        if(width != null){
            if(width < 1){
                throw new GridValidationException("Width cannot be smaller than 1");
            }
        } else {
            throw new GridValidationException("Width cannot be null!");
        }
    }

    private void validateHeight() throws GridValidationException{
        if(height != null){
            if(height < 1){
                throw new GridValidationException("Height cannot be smaller then 1");
            }
        } else{
            throw new GridValidationException("Height cannot be null!");
        }
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
}