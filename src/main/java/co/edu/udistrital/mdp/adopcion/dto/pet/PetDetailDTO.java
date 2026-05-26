package co.edu.udistrital.mdp.adopcion.dto.pet;

import java.util.ArrayList;
import java.util.List;

import co.edu.udistrital.mdp.adopcion.dto.person.OwnerDTO;
import co.edu.udistrital.mdp.adopcion.dto.adoption.AdoptionApplicationDTO;
import co.edu.udistrital.mdp.adopcion.dto.adoption.AdoptionDTO;
import co.edu.udistrital.mdp.adopcion.dto.events.MedicalEventDTO;
import co.edu.udistrital.mdp.adopcion.dto.events.ShelterArrivalDTO;
import co.edu.udistrital.mdp.adopcion.dto.multimedia.MultimediaDTO;
import lombok.Data;

@Data
public class PetDetailDTO extends PetDTO {
    private List<OwnerDTO> owners = new ArrayList<>();
    private List<AdoptionApplicationDTO> adoptionApplications = new ArrayList<>();
    private List<MedicalEventDTO> medicalEvents = new ArrayList<>();
    private List<MultimediaDTO> multimedia = new ArrayList<>();
    private AdoptionDTO adoption;
    private ShelterArrivalDTO shelterArrival;
}
