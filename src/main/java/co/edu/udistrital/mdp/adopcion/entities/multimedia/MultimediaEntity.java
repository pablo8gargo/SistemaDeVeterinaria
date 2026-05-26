package co.edu.udistrital.mdp.adopcion.entities.multimedia;

import co.edu.udistrital.mdp.adopcion.entities.BaseEntity;
import co.edu.udistrital.mdp.adopcion.entities.ShelterEntity;
import co.edu.udistrital.mdp.adopcion.entities.pet.PetEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class MultimediaEntity extends BaseEntity {
    private String url;
    private String description;
    private MultimediaTypeEnum multimediaType;

    @PodamExclude
    @ManyToOne
    private PetEntity pet;

    @PodamExclude
    @ManyToOne
    private ShelterEntity shelter;
}
