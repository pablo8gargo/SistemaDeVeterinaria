package co.edu.udistrital.mdp.adopcion.dto.events;

import co.edu.udistrital.mdp.adopcion.dto.person.VeterinarianDTO;
import co.edu.udistrital.mdp.adopcion.dto.pet.PetDTO;
import lombok.Data;

@Data
public class MedicalEventDTO extends EventDTO {
    private VeterinarianDTO veterinarian;
    private PetDTO pet;
}
