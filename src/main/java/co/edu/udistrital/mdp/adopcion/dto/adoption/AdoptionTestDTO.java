package co.edu.udistrital.mdp.adopcion.dto.adoption;

import java.sql.Date;

import co.edu.udistrital.mdp.adopcion.dto.person.OwnerDTO;
import co.edu.udistrital.mdp.adopcion.dto.person.VeterinarianDTO;
import co.edu.udistrital.mdp.adopcion.entities.adoption.TestResultEnum;
import lombok.Data;

@Data
public class AdoptionTestDTO {
    private Long id;
    private String description;
    private Date testEnd;
    private String testObservations;
    private TestResultEnum typeTest;
    private OwnerDTO owner;
    private VeterinarianDTO veterinarian;
}

