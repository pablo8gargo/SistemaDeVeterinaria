package co.edu.udistrital.mdp.adopcion.dto.events.medical;

import java.sql.Date;

import co.edu.udistrital.mdp.adopcion.dto.person.VeterinarianDTO;
import co.edu.udistrital.mdp.adopcion.entities.events.medical.DewormingTypeEnum;
import lombok.Data;

@Data
public class DewormingDTO {
    private Long id;
    private String brandName;
    private Date nextDate;
    private Double dosis;
    private VeterinarianDTO veterinarian;
    private DewormingTypeEnum type;
    private VaccineCardDTO vaccineCard;
}
