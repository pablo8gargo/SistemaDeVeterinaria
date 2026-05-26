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

import co.edu.udistrital.mdp.adopcion.dto.events.medical.DewormingDTO;
import co.edu.udistrital.mdp.adopcion.dto.events.medical.DewormingDetailDTO;
import co.edu.udistrital.mdp.adopcion.entities.events.medical.DewormingEntity;
import co.edu.udistrital.mdp.adopcion.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.adopcion.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.adopcion.services.events.medical.DewormingService;

@RestController
@RequestMapping("/dewormings")
public class DewormingController {


    @Autowired
    private DewormingService dewormingService;
    
    @Autowired
    private ModelMapper modelMapper;


    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<DewormingDetailDTO> findAll()  throws IllegalOperationException {
        List<DewormingEntity> dewormings = dewormingService.getAllDewormings();
        if (dewormings==null || dewormings.isEmpty()) {
            throw new IllegalOperationException("No dewormings found.");
        }
        return modelMapper.map(dewormings, new TypeToken<List<DewormingDetailDTO>>() {
        }.getType());
    }

    @GetMapping(value = "/{dewormings_id}")
    @ResponseStatus(code = HttpStatus.OK)
    public DewormingDetailDTO findOne(@PathVariable Long dewormings_id) throws EntityNotFoundException {
        DewormingEntity dewormingEntity = dewormingService.getDewormingById(dewormings_id);
        if (dewormingEntity == null) {
            throw new EntityNotFoundException("Deworming not found with ID: " + dewormings_id);
        }
        return modelMapper.map(dewormingEntity, DewormingDetailDTO.class);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public DewormingDTO create(@RequestBody DewormingDTO dewormingDTO) throws IllegalOperationException, EntityNotFoundException {
        if (dewormingDTO == null) {
            throw new IllegalOperationException("Deworming data cannot be null");
        }
        DewormingEntity dewormingEntity = dewormingService.createDeworming(modelMapper.map(dewormingDTO, DewormingEntity.class));
        return modelMapper.map(dewormingEntity, DewormingDTO.class);
    }

    @PutMapping(value = "/{dewormings_id}")
    @ResponseStatus(code = HttpStatus.OK)
    public DewormingDTO update(@PathVariable Long dewormings_id, @RequestBody DewormingDTO dewormingDTO) 
            throws EntityNotFoundException, IllegalOperationException {
        DewormingEntity dewormingEntity = dewormingService.updateDeworming(dewormings_id, modelMapper.map(dewormingDTO, DewormingEntity.class));
        if (dewormingEntity == null) {
            throw new EntityNotFoundException("Deworming not found with ID: " + dewormings_id);
        }
        return modelMapper.map(dewormingEntity, DewormingDTO.class);
    }

    @DeleteMapping(value = "/{dewormings_id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long dewormings_id) throws EntityNotFoundException, IllegalOperationException {
        if (dewormingService.getDewormingById(dewormings_id) == null) {
            throw new EntityNotFoundException("Deworming not found with ID: " + dewormings_id);
        }
        dewormingService.deleteDeworming(dewormings_id);
    }


}
