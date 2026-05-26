package co.edu.udistrital.mdp.adopcion.dto.person;

import java.util.ArrayList;
import java.util.List;

import co.edu.udistrital.mdp.adopcion.dto.adoption.AdoptionDTO;
import co.edu.udistrital.mdp.adopcion.dto.adoption.AdoptionApplicationDTO;
import co.edu.udistrital.mdp.adopcion.dto.adoption.AdoptionTestDTO;
import co.edu.udistrital.mdp.adopcion.dto.pet.PetDTO;
import lombok.Data;

@Data
public class OwnerDetailDTO extends OwnerDTO {
    private List<AdoptionDTO> adoptions = new ArrayList<>();
    private List<AdoptionTestDTO> adoptionTests = new ArrayList<>();
    private List<AdoptionApplicationDTO> adoptionApplications = new ArrayList<>();
    private List<PetDTO> pets = new ArrayList<>();
}