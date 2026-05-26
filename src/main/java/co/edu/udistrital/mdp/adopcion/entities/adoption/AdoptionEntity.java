package co.edu.udistrital.mdp.adopcion.entities.adoption;


import co.edu.udistrital.mdp.adopcion.entities.events.EventEntity;
import co.edu.udistrital.mdp.adopcion.entities.person.OwnerEntity;
import co.edu.udistrital.mdp.adopcion.entities.person.VeterinarianEntity;
import co.edu.udistrital.mdp.adopcion.entities.pet.PetEntity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;
@Data
@Entity
public class AdoptionEntity extends EventEntity{
    private String observations;
    private AdoptionStatusEnum adoptionStatus;
    
    @PodamExclude
    @ManyToOne
	private  OwnerEntity owner;

    @PodamExclude
    @OneToOne
    private VeterinarianEntity veterinarian;

    @PodamExclude
    @OneToOne
    private PetEntity pet;

    @PodamExclude
    @OneToOne
    private AdoptionApplicationEntity adoptionApplication;
}
