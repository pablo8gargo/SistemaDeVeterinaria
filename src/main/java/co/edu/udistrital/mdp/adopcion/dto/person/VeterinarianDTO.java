package co.edu.udistrital.mdp.adopcion.dto.person;


import java.util.List;

import co.edu.udistrital.mdp.adopcion.entities.person.Speciality;
import co.edu.udistrital.mdp.adopcion.entities.person.DisponibilityEnum;
import lombok.Data;

@Data
public class VeterinarianDTO extends PersonDTO {

    private Long id;

    private String licenseNumber;
    private Speciality speciality;
    private List<DisponibilityEnum> disponibilities;
}