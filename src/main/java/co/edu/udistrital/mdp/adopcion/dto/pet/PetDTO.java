package co.edu.udistrital.mdp.adopcion.dto.pet;

import java.util.Date;

import co.edu.udistrital.mdp.adopcion.entities.pet.SizeEnum;
import co.edu.udistrital.mdp.adopcion.entities.pet.GenderEnum;
import co.edu.udistrital.mdp.adopcion.dto.ShelterDTO;
import co.edu.udistrital.mdp.adopcion.dto.events.medical.VaccineCardDTO;
import lombok.Data;

@Data
public class PetDTO {
    private Long id;
    private String name;
    private Date birthDate;
    private String breed;
    private SizeEnum size;
    private GenderEnum gender;
    private String behaviorProfile;
    private ShelterDTO shelter;
    private VaccineCardDTO vaccineCard;
}
