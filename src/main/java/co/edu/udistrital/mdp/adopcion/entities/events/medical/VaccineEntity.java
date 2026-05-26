package co.edu.udistrital.mdp.adopcion.entities.events.medical;
import java.util.Date;

import co.edu.udistrital.mdp.adopcion.entities.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class VaccineEntity extends BaseEntity {
    private String name;
    private String brandName;
    private Date nextDate;
    private Double dosis;

    @PodamExclude
    @ManyToOne
    private  VaccineCardEntity vaccineCard;
}
