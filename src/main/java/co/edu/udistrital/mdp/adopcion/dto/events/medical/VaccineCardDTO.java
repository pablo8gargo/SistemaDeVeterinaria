package co.edu.udistrital.mdp.adopcion.dto.events.medical;

import java.util.Date;

import co.edu.udistrital.mdp.adopcion.dto.events.EventDTO;
import co.edu.udistrital.mdp.adopcion.dto.pet.PetDTO;
import lombok.Data;


@Data
public class VaccineCardDTO extends EventDTO {
    private Long id;
    private Date lastVacineDate;
    private Date lastDewormingDate;
    @com.fasterxml.jackson.annotation.JsonIgnore
    private PetDTO pet;
}
