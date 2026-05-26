package co.edu.udistrital.mdp.adopcion.dto.events;

import co.edu.udistrital.mdp.adopcion.dto.ShelterDTO;
import co.edu.udistrital.mdp.adopcion.dto.person.VeterinarianDTO;
import co.edu.udistrital.mdp.adopcion.dto.pet.PetDTO;
import co.edu.udistrital.mdp.adopcion.entities.pet.PetConditionEnum;
import lombok.Data;

@Data
public class ShelterArrivalDTO extends EventDTO {
    private PetDTO pet;
    private ShelterDTO shelter;
    private VeterinarianDTO veterinarian;
    private PetConditionEnum petCondition;
}
