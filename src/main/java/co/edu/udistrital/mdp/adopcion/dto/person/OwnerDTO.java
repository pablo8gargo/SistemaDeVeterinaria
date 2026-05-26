package co.edu.udistrital.mdp.adopcion.dto.person;


import co.edu.udistrital.mdp.adopcion.entities.person.HouseTypeEnum;
import lombok.Data;

@Data
public class OwnerDTO extends PersonDTO {
    private HouseTypeEnum houseType;
    private String address;
}