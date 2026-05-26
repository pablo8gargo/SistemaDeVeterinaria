package co.edu.udistrital.mdp.adopcion.entities.adoption;

import co.edu.udistrital.mdp.adopcion.entities.events.EventEntity;
import co.edu.udistrital.mdp.adopcion.entities.person.VeterinarianEntity;
import co.edu.udistrital.mdp.adopcion.entities.pet.PetConditionEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class AdoptionFollowUpEntity extends EventEntity{
    private String description;
    private String observations;
    private FollowUpStatusEnum followUpStatus;
    private PetConditionEnum petCondition;

    @PodamExclude
    @ManyToOne
    private VeterinarianEntity veterinarian;
    
}
