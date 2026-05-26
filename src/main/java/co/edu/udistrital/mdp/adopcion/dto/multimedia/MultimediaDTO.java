package co.edu.udistrital.mdp.adopcion.dto.multimedia;

import co.edu.udistrital.mdp.adopcion.dto.pet.PetDTO;
import co.edu.udistrital.mdp.adopcion.entities.multimedia.MultimediaTypeEnum;
import lombok.Data;

@Data
public class MultimediaDTO {
    private Long id;
    private String url;
    private String description;
    private MultimediaTypeEnum multimediaType;
    private PetDTO pet;
}