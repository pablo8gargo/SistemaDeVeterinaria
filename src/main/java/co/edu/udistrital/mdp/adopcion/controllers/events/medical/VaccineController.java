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

import co.edu.udistrital.mdp.adopcion.dto.events.medical.VaccineDTO;
import co.edu.udistrital.mdp.adopcion.dto.events.medical.VaccineDetailDTO;
import co.edu.udistrital.mdp.adopcion.entities.events.medical.VaccineEntity;
import co.edu.udistrital.mdp.adopcion.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.adopcion.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.adopcion.services.events.medical.VaccineService;

@RestController
@RequestMapping("/vaccines")
public class VaccineController {

    @Autowired
    private VaccineService vaccineService;
    
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<VaccineDetailDTO> findAll()  throws IllegalOperationException {
        List<VaccineEntity> vaccines = vaccineService.getAllVaccines();
        if (vaccines == null || vaccines.isEmpty()) {
            throw new IllegalOperationException("No vaccines found.");
        }
        return modelMapper.map(vaccines, new TypeToken<List<VaccineDetailDTO>>() {
        }.getType());
    }

    @GetMapping(value = "/{vaccines_id}")
    @ResponseStatus(code = HttpStatus.OK)
    public VaccineDetailDTO findOne(@PathVariable Long vaccines_id) throws EntityNotFoundException {
        VaccineEntity vaccineEntity = vaccineService.getVaccineById(vaccines_id);
        if (vaccineEntity == null) {
            throw new EntityNotFoundException("Vaccine not found with ID: " + vaccines_id);
        }
        return modelMapper.map(vaccineEntity, VaccineDetailDTO.class);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public VaccineDTO create(@RequestBody VaccineDTO vaccineDTO) throws IllegalOperationException, EntityNotFoundException {
        if (vaccineDTO == null) {
            throw new IllegalOperationException("Vaccine data cannot be null");
        }
        
        // Validar que tenga una vaccine card asociada
        if (vaccineDTO.getVaccineCard() == null || vaccineDTO.getVaccineCard().getId() == null) {
            throw new IllegalOperationException("Vaccine must be associated with a vaccine card");
        }

        VaccineEntity vaccineEntity = vaccineService.createVaccine(modelMapper.map(vaccineDTO, VaccineEntity.class));
        return modelMapper.map(vaccineEntity, VaccineDTO.class);
    }

    @PutMapping(value = "/{vaccines_id}")
    @ResponseStatus(code = HttpStatus.OK)
    public VaccineDTO update(@PathVariable Long vaccines_id, @RequestBody VaccineDTO vaccineDTO) 
            throws EntityNotFoundException, IllegalOperationException {
        VaccineEntity vaccineEntity = vaccineService.updateVaccine(vaccines_id, modelMapper.map(vaccineDTO, VaccineEntity.class));
        if (vaccineEntity == null) {
            throw new EntityNotFoundException("Vaccine not found with ID: " + vaccines_id);
        }
        return modelMapper.map(vaccineEntity, VaccineDTO.class);
    }

    @DeleteMapping(value = "/{vaccines_id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long vaccines_id) throws EntityNotFoundException, IllegalOperationException {
        if(vaccineService.getVaccineById(vaccines_id) == null) {
            throw new EntityNotFoundException("Vaccine not found with ID: " + vaccines_id);
        }
        vaccineService.deleteVaccine(vaccines_id);
    }
}
