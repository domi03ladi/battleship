package at.fhv.battleship.mapper;

import at.fhv.battleship.dtos.GridDTO;
import at.fhv.battleship.entities.Grid;
import org.springframework.stereotype.Service;

@Service
public class GridMapper {

    public GridDTO convertToDTO(Grid grid) {
        return new GridDTO(grid.getId(), grid.getHeight(), grid.getWidth());
    }

    public Grid convertToEntity(GridDTO gridDTO){
        Grid grid = new Grid();
        if(gridDTO.getId() != null){
            grid.setId(gridDTO.getId());
        }

        grid.setHeight(gridDTO.getHeight());
        grid.setWidth(gridDTO.getWidth());
        return grid;
    }
}
