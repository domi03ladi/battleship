package at.fhv.battleship.services.grid;

import at.fhv.battleship.dtos.GridDTO;
import at.fhv.battleship.entities.Grid;
import at.fhv.battleship.mapper.GridMapper;
import at.fhv.battleship.repositories.IGridRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GridService {

    @Autowired
    final IGridRepository gridRepository;

    @Autowired
    private GridMapper gridMapper;

    public GridService(IGridRepository gridRepository) {
        this.gridRepository = gridRepository;
    }

    public List<GridDTO> findAll(){
        List<Grid> grids = gridRepository.findAll();
        List<GridDTO> gridDTOs = new ArrayList<>();
        for(Grid g: grids){
            gridDTOs.add(gridMapper.convertToDTO(g));
        }
        return gridDTOs;
    }

    public GridDTO findById(Integer id) throws GridNotFoundException{
        Optional<Grid> optionalGrid = gridRepository.findById(id);
        if(optionalGrid.isPresent()){
            return gridMapper.convertToDTO(optionalGrid.get());
        } else {
            throw new GridNotFoundException("Grid with Id: " + id + " not found!");
        }
    }

    public GridDTO create(Grid grid){
        Grid createdGrid = gridRepository.save(grid);
        return gridMapper.convertToDTO(createdGrid);
    }

}
