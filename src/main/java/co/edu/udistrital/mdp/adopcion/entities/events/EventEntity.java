package co.edu.udistrital.mdp.adopcion.entities.events;

import java.time.LocalDateTime;
import java.util.Date;

import co.edu.udistrital.mdp.adopcion.entities.BaseEntity;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@MappedSuperclass
public abstract class EventEntity extends BaseEntity {
    @PodamExclude
    private Date date;
    private String description;
}
