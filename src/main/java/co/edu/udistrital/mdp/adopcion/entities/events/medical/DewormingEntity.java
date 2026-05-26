package co.edu.udistrital.mdp.adopcion.entities.events.medical;

import co.edu.udistrital.mdp.adopcion.entities.events.MedicalEventEntity;
import co.edu.udistrital.mdp.adopcion.entities.person.VeterinarianEntity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

import java.util.Date;

import jakarta.persistence.Entity;

@Data
@Entity
public class DewormingEntity extends MedicalEventEntity {
    
    private String brandName;
    private Date nextDate;
    private Double dosis;
    private VeterinarianEntity veterinarian;
    private DewormingTypeEnum type;

    @PodamExclude
    @ManyToOne
	private VaccineCardEntity vaccineCard;
}
