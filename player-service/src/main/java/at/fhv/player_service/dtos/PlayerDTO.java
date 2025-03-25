package at.fhv.player_service.dtos;


public class PlayerDTO {

    private Integer id;
    private String name;

    public PlayerDTO(){

    }

    public PlayerDTO(Integer id, String name){
        this.id = id;
        this.name = name;
    }

    public Integer getId(){
        return this.id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }
}
