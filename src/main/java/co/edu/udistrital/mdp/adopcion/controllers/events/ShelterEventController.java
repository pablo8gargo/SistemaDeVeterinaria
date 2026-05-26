package co.edu.udistrital.mdp.adopcion.controllers.events;

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

import co.edu.udistrital.mdp.adopcion.dto.events.ShelterEventDTO;
import co.edu.udistrital.mdp.adopcion.dto.events.ShelterEventDetailDTO;
import co.edu.udistrital.mdp.adopcion.entities.events.ShelterEventEntity;
import co.edu.udistrital.mdp.adopcion.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.adopcion.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.adopcion.services.events.ShelterEventService;

@RestController
@RequestMapping("/shelter-events")
public class ShelterEventController {
    @Autowired
    private ShelterEventService shelterEventService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Get all shelter events.
     * 
     * @return
     */
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<ShelterEventDetailDTO> findAll() {
        List<ShelterEventEntity> shelterEvents = shelterEventService.getAllShelterEvents();
        return modelMapper.map(shelterEvents, new TypeToken<List<ShelterEventDetailDTO>>() {
        }.getType());
    }

    /**
     * Get shelter event by ID.
     * 
     * @param shelter_event_id
     * @return
     * @throws EntityNotFoundException
     */
    @GetMapping(value = "/{shelter_event_id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ShelterEventDetailDTO findOne(@PathVariable Long shelter_event_id) throws EntityNotFoundException {
        ShelterEventEntity shelterEventEntity = shelterEventService.getShelterEventById(shelter_event_id);
        if (shelterEventEntity == null) {
            throw new EntityNotFoundException("Shelter event not found with ID: " + shelter_event_id);
        }
        return modelMapper.map(shelterEventEntity, ShelterEventDetailDTO.class);
    }

    /**
     * Create a new shelter event.
     * 
     * @param shelterEventDTO
     * @return
     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ShelterEventDTO create(@RequestBody ShelterEventDTO shelterEventDTO)
            throws EntityNotFoundException, IllegalOperationException {
        ShelterEventEntity shelterEventEntity = shelterEventService
                .createShelterEvent(modelMapper.map(shelterEventDTO, ShelterEventEntity.class));
        return modelMapper.map(shelterEventEntity, ShelterEventDTO.class);
    }

    /**
     * Update an existing shelter event.
     * 
     * @param shelter_event_id
     * @param shelterEventDTO
     * @return
     */
    @PutMapping(value = "/{shelter_event_id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ShelterEventDTO update(@PathVariable Long shelter_event_id, @RequestBody ShelterEventDTO shelterEventDTO)
            throws EntityNotFoundException, IllegalOperationException {
        ShelterEventEntity shelterEventEntity = shelterEventService.updateShelterEvent(shelter_event_id,
                modelMapper.map(shelterEventDTO, ShelterEventEntity.class));
        if (shelterEventEntity == null) {
            throw new EntityNotFoundException("Shelter event not found with ID: " + shelter_event_id);
        }
        return modelMapper.map(shelterEventEntity, ShelterEventDTO.class);
    }

    /**
     * Delete a shelter event.
     * 
     * @param shelter_event_id
     */
    @DeleteMapping(value = "/{shelter_event_id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long shelter_event_id) throws EntityNotFoundException, IllegalOperationException {
        if (shelterEventService.getShelterEventById(shelter_event_id) == null) {
            throw new EntityNotFoundException("Shelter event not found with ID: " + shelter_event_id);
        }
        shelterEventService.deleteShelterEvent(shelter_event_id);
    }
}
