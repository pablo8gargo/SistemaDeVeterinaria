package co.edu.udistrital.mdp.adopcion.dto.adoption;

import co.edu.udistrital.mdp.adopcion.dto.events.EventDTO;
import co.edu.udistrital.mdp.adopcion.dto.person.OwnerDTO;
import co.edu.udistrital.mdp.adopcion.dto.person.VeterinarianDTO;
import co.edu.udistrital.mdp.adopcion.dto.pet.PetDTO;
import co.edu.udistrital.mdp.adopcion.entities.adoption.AdoptionStatusEnum;
import lombok.Data;

@Data
public class AdoptionDTO extends EventDTO {
    private Long id;
    private String observations;
    private AdoptionStatusEnum adoptionStatus;
    private OwnerDTO owner;
    private VeterinarianDTO veterinarian;
    private PetDTO pet;
    private AdoptionApplicationDTO adoptionApplication;

}
