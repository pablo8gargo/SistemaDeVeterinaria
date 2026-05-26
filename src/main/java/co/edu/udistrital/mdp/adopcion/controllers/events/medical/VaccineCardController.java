package co.edu.udistrital.mdp.adopcion.controllers.events.medical;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.adopcion.dto.events.medical.VaccineCardDTO;
import co.edu.udistrital.mdp.adopcion.dto.events.medical.VaccineCardDetailDTO;
import co.edu.udistrital.mdp.adopcion.entities.events.medical.VaccineCardEntity;
import co.edu.udistrital.mdp.adopcion.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.adopcion.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.adopcion.services.events.medical.VaccineCardService;

@RestController
@RequestMapping("/vaccine-cards")
public class VaccineCardController {
    @Autowired
    private VaccineCardService vaccineCardService;
    
    @Autowired
    private ModelMapper modelMapper;


    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<VaccineCardDetailDTO> findAll() throws IllegalOperationException {
        List<VaccineCardEntity> vaccineCards = vaccineCardService.getAllVaccineCards();
        if (vaccineCards == null || vaccineCards.isEmpty()) {
            throw new IllegalOperationException("No vaccine cards found.");
        }
        return modelMapper.map(vaccineCards, new TypeToken<List<VaccineCardDTO>>() {
        }.getType());
    }

    @GetMapping(value = "/{vaccineCards_id}")
    @ResponseStatus(code = HttpStatus.OK)
    public VaccineCardDetailDTO findOne(@PathVariable Long vaccineCards_id) throws EntityNotFoundException {
        VaccineCardEntity vaccineCardEntity = vaccineCardService.getVaccineCardById(vaccineCards_id);
        if (vaccineCardEntity == null) {
            throw new EntityNotFoundException("Vaccine card not found with ID: " + vaccineCards_id);
        }
        return modelMapper.map(vaccineCardEntity, VaccineCardDetailDTO.class);
    }


    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public VaccineCardDTO create(@RequestBody VaccineCardDTO vaccineCardDTO)
            throws EntityNotFoundException, IllegalOperationException {
        
        // Validar que el DTO no sea null
        if (vaccineCardDTO == null) {
            throw new IllegalOperationException("Vaccine card data cannot be null");
        }
        
        // Validar que tenga una mascota asociada
        if (vaccineCardDTO.getPet() == null || vaccineCardDTO.getPet().getId() == null) {
            throw new IllegalOperationException("Vaccine card must be associated with a pet");
        }
        
        VaccineCardEntity vaccineCardEntity = vaccineCardService
                .createVaccineCard(modelMapper.map(vaccineCardDTO, VaccineCardEntity.class));
        return modelMapper.map(vaccineCardEntity, VaccineCardDTO.class);
    }
    

    @PutMapping(value = "/{vaccineCards_id}")
    @ResponseStatus(code = HttpStatus.OK)
    public VaccineCardDTO update(@PathVariable Long vaccineCards_id, @RequestBody VaccineCardDTO vaccineCardDTO) 
            throws EntityNotFoundException, IllegalOperationException {
        VaccineCardEntity vaccineCardEntity = vaccineCardService.updateVaccineCard(vaccineCards_id, modelMapper.map(vaccineCardDTO, VaccineCardEntity.class));
        if (vaccineCardEntity == null) {
            throw new EntityNotFoundException("Vaccine card not found with ID: " + vaccineCards_id);
        }
        return modelMapper.map(vaccineCardEntity, VaccineCardDTO.class);
    }

    @DeleteMapping(value = "/{vaccineCards_id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long vaccineCards_id) throws EntityNotFoundException, IllegalOperationException {
        if (vaccineCards_id == null) {
            throw new EntityNotFoundException("Vaccine card ID cannot be null");
        }
        vaccineCardService.deleteVaccineCard(vaccineCards_id);
    }

}