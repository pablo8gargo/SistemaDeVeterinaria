package co.edu.udistrital.mdp.adopcion.dto.person;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

import co.edu.udistrital.mdp.adopcion.dto.adoption.AdoptionApplicationDTO;
import co.edu.udistrital.mdp.adopcion.dto.adoption.AdoptionFollowUpDTO;
import co.edu.udistrital.mdp.adopcion.dto.adoption.AdoptionTestDTO;
import co.edu.udistrital.mdp.adopcion.dto.events.MedicalEventDTO;
import co.edu.udistrital.mdp.adopcion.dto.events.ShelterArrivalDTO;

@Data
public class VeterinarianDetailDTO extends VeterinarianDTO {

    private List<MedicalEventDTO> medicalEvents = new ArrayList<>();
    private List<AdoptionApplicationDTO> adoptionApplications = new ArrayList<>();
    private List<AdoptionFollowUpDTO> followUps = new ArrayList<>();
    private List<AdoptionTestDTO> adoptionTests = new ArrayList<>();
    private List<ShelterArrivalDTO> shelterArrivals = new ArrayList<>();
}
