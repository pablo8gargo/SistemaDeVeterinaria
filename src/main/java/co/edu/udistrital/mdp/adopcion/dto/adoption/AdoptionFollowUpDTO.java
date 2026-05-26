package co.edu.udistrital.mdp.adopcion.dto.adoption;

import java.sql.Date;

import co.edu.udistrital.mdp.adopcion.dto.person.VeterinarianDTO;
import co.edu.udistrital.mdp.adopcion.entities.adoption.FollowUpStatusEnum;
import co.edu.udistrital.mdp.adopcion.entities.pet.PetConditionEnum;
import lombok.Data;

@Data
public class AdoptionFollowUpDTO {
    private Long id;
    private String description;
    private String observations;
    private Date visitDate;
    private FollowUpStatusEnum followUpStatus;
    private PetConditionEnum petCondition;
    private VeterinarianDTO veterinarian;
}