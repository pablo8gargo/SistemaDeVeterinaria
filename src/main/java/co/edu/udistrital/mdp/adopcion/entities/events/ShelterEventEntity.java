package co.edu.udistrital.mdp.adopcion.entities.events;

import co.edu.udistrital.mdp.adopcion.entities.ShelterEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class ShelterEventEntity extends EventEntity {
    private String name;
    
    @PodamExclude
    @ManyToOne
    private ShelterEntity shelter;
}
