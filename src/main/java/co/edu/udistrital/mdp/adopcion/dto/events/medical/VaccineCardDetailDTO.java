package co.edu.udistrital.mdp.adopcion.dto.events.medical;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class VaccineCardDetailDTO extends VaccineCardDTO {
    private List<VaccineDTO> vaccines = new ArrayList<>();
    private List<DewormingDTO> dewormings = new ArrayList<>();

}
