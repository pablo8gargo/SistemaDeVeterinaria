package co.edu.udistrital.mdp.adopcion.entities.events;

import co.edu.udistrital.mdp.adopcion.entities.ShelterEntity;
import co.edu.udistrital.mdp.adopcion.entities.person.VeterinarianEntity;
import co.edu.udistrital.mdp.adopcion.entities.pet.PetConditionEnum;
import co.edu.udistrital.mdp.adopcion.entities.pet.PetEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class ShelterArrivalEntity extends EventEntity{
    private PetConditionEnum petCondition;
    
    @PodamExclude
    @OneToOne
    private PetEntity pet;
    @ManyToOne
    private ShelterEntity shelter;
    @ManyToOne
    private VeterinarianEntity veterinarian;
}