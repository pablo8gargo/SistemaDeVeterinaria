package co.edu.udistrital.mdp.adopcion.entities.events.medical;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import co.edu.udistrital.mdp.adopcion.entities.events.EventEntity;
import co.edu.udistrital.mdp.adopcion.entities.pet.PetEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class VaccineCardEntity extends EventEntity {
    private Date lastVacineDate;
    private Date lastDewormingDate;

    @PodamExclude
    @OneToMany(mappedBy="vaccineCard")
	private List<VaccineEntity> vaccines = new ArrayList<>();

    @PodamExclude
    @OneToMany(mappedBy="vaccineCard")
	private List<DewormingEntity> dewormings = new ArrayList<>();

    @PodamExclude
    @OneToOne(mappedBy = "vaccineCard", fetch = FetchType.LAZY)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private PetEntity pet;
}
