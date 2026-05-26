package co.edu.udistrital.mdp.adopcion.entities.adoption;


import java.util.Date;

import co.edu.udistrital.mdp.adopcion.entities.events.EventEntity;
import co.edu.udistrital.mdp.adopcion.entities.person.OwnerEntity;
import co.edu.udistrital.mdp.adopcion.entities.person.VeterinarianEntity;
import co.edu.udistrital.mdp.adopcion.entities.pet.PetEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class AdoptionApplicationEntity extends EventEntity {
    private Date applicationDate;
    private Date applicationEnd;
    private String observations;
    private ApplicationStatusEnum applicationStatus;
    private ApplicationResultEnum result;
    
    @PodamExclude
    @ManyToOne
	private OwnerEntity owner;

    @PodamExclude
    @ManyToOne
	private VeterinarianEntity veterinarian;

    @PodamExclude
    @OneToOne(mappedBy = "adoptionApplication")
    private AdoptionEntity adoption;

    @PodamExclude
    @ManyToOne
    private PetEntity pet;
}
