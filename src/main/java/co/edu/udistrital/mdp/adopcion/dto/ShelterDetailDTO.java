package co.edu.udistrital.mdp.adopcion.dto;

import java.util.ArrayList;
import java.util.List;

import co.edu.udistrital.mdp.adopcion.dto.events.ShelterArrivalDTO;
import co.edu.udistrital.mdp.adopcion.dto.events.ShelterEventDTO;
import co.edu.udistrital.mdp.adopcion.dto.pet.PetDTO;
import lombok.Data;

@Data
public class ShelterDetailDTO {
    private List<ShelterEventDTO> shelterEvents = new ArrayList<>();
    private List<PetDTO> pets = new ArrayList<>();
    private List<ShelterArrivalDTO> shelterArrivals = new ArrayList<>();
}
