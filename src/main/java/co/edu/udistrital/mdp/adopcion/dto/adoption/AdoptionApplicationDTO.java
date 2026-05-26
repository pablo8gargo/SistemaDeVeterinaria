package co.edu.udistrital.mdp.adopcion.dto.adoption;

import java.util.Date;

import co.edu.udistrital.mdp.adopcion.dto.person.OwnerDTO;
import co.edu.udistrital.mdp.adopcion.dto.person.VeterinarianDTO;
import co.edu.udistrital.mdp.adopcion.dto.pet.PetDTO;
import co.edu.udistrital.mdp.adopcion.entities.adoption.ApplicationResultEnum;
import co.edu.udistrital.mdp.adopcion.entities.adoption.ApplicationStatusEnum;
import lombok.Data;

@Data
public class AdoptionApplicationDTO {
    private Long id;
    private Date applicationDate;
    private Date applicationEnd;
    private String observations;
    private ApplicationStatusEnum applicationStatus;
    private ApplicationResultEnum result;
    private OwnerDTO owner;
    private VeterinarianDTO veterinarian;
    private PetDTO pet;
}
