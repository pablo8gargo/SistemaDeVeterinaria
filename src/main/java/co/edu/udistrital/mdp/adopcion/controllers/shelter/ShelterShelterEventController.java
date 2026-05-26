package co.edu.udistrital.mdp.adopcion.controllers.shelter;

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

import co.edu.udistrital.mdp.adopcion.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.adopcion.exceptions.IllegalOperationException;

import co.edu.udistrital.mdp.adopcion.entities.ShelterEntity;
import co.edu.udistrital.mdp.adopcion.services.ShelterService;
import co.edu.udistrital.mdp.adopcion.dto.events.ShelterEventDTO;
import co.edu.udistrital.mdp.adopcion.dto.events.ShelterEventDetailDTO;
import co.edu.udistrital.mdp.adopcion.entities.events.ShelterEventEntity;
import co.edu.udistrital.mdp.adopcion.services.events.ShelterEventService;

@RestController
@RequestMapping("/shelters")
public class ShelterShelterEventController {

    @Autowired
    private ShelterEventService shelterEventService;

    @Autowired
    private ShelterService shelterService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Get all shelter events.
     *
     * @return List of ShelterEventDetailDTO
     * @throws IllegalOperationException
     * @throws EntityNotFoundException
     */
    @GetMapping("/{shelterId}/events")
    @ResponseStatus(code = HttpStatus.OK)
    public List<ShelterEventDetailDTO> findAll(@PathVariable Long shelterId)
            throws IllegalOperationException, EntityNotFoundException {
        ShelterEntity shelter = shelterService.getShelterById(shelterId);
        if (shelter == null) {
            throw new EntityNotFoundException("Shelter not found with ID: " + shelterId);
        }
        List<ShelterEventEntity> shelterEvents = shelterEventService.getAllShelterEvents();
        shelterEvents.removeIf(event -> !event.getShelter().getId().equals(shelterId));
        if (shelterEvents.isEmpty()) {
            throw new IllegalOperationException("No shelter events found.");
        }
        return modelMapper.map(shelterEvents, new TypeToken<List<ShelterEventDetailDTO>>() {
        }.getType());
    }

    /**
     * Get shelter event by ID for a specific shelter.
     *
     * @param shelterId
     * @param shelterEventId
     * @return ShelterEventDetailDTO
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @GetMapping("/{shelterId}/events/{shelterEventId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ShelterEventDetailDTO findOne(@PathVariable Long shelterId, @PathVariable Long shelterEventId)
            throws EntityNotFoundException, IllegalOperationException {
        ShelterEntity shelter = shelterService.getShelterById(shelterId);
        if (shelter == null) {
            throw new IllegalOperationException("Shelter not found with ID: " + shelterId);
        }
        ShelterEventEntity shelterEvent = shelterEventService.getShelterEventById(shelterEventId);
        if (shelterEvent == null || !shelterEvent.getShelter().getId().equals(shelterId)) {
            throw new EntityNotFoundException("Shelter event not found with ID: " + shelterEventId);
        }
        return modelMapper.map(shelterEvent, ShelterEventDetailDTO.class);
    }

    /**
     * Create a new shelter event for a specific shelter.
     * 
     * @param shelterId
     * @param shelterEventId
     * @return ShelterEventDetailDTO
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @PostMapping("/{shelterId}/events/{shelterEventId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ShelterEventDTO create(@PathVariable Long shelterId, @PathVariable Long shelterEventId)
            throws EntityNotFoundException, IllegalOperationException {
        ShelterEntity shelter = shelterService.getShelterById(shelterId);
        if (shelter == null) {
            throw new IllegalOperationException("Shelter not found with ID: " + shelterId);
        }
        ShelterEventEntity shelterEventEntity = shelterEventService.getShelterEventById(shelterEventId);
        if (shelterEventEntity == null || !shelterEventEntity.getShelter().getId().equals(shelterId)) {
            throw new EntityNotFoundException("Shelter event not found with ID: " + shelterEventId);
        }
        shelterEventEntity.setShelter(shelter);
        shelterEventEntity = shelterEventService.updateShelterEvent(shelterEventId, shelterEventEntity);
        if (shelterEventEntity == null) {
            throw new IllegalOperationException("Failed to create shelter event.");
        }
        return modelMapper.map(shelterEventEntity, ShelterEventDTO.class);
    }

    /**
     * Assign a shelter event list to a specific shelter.
     *
     * @param shelterId
     * @param shelterEventId
     * @param shelterEventDTO
     * @return ShelterEventDetailDTO
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @PutMapping("/{shelterId}/events")
    @ResponseStatus(code = HttpStatus.OK)
    public List<ShelterEventDetailDTO> update(@PathVariable Long shelterId,
            @RequestBody List<ShelterEventDTO> listShelterEventDTO)
            throws EntityNotFoundException, IllegalOperationException {
        ShelterEntity shelter = shelterService.getShelterById(shelterId);
        if (shelter == null) {
            throw new IllegalOperationException("Shelter not found with ID: " + shelterId);
        }
        List<ShelterEventEntity> shelterEventEntities = modelMapper.map(listShelterEventDTO,
                new TypeToken<List<ShelterEventEntity>>() {
                }.getType());
        shelterEventEntities.forEach(event -> event.setShelter(shelter));
        for (ShelterEventEntity event : shelterEventEntities) {
            try {
                event = shelterEventService.updateShelterEvent(event.getId(), event);
            } catch (IllegalArgumentException e) {
                throw new EntityNotFoundException(
                        "Failed to update, not found shelter event with ID: " + event.getId());
            }
        }
        shelterEventEntities = shelterEventService.getAllShelterEvents();
        return findAll(shelterId);
    }

    /**
     * Delete a shelter event by ID for a specific shelter.
     *
     * @param shelterId
     * @param shelterEventId
     * @throws EntityNotFoundException
     */
    @DeleteMapping("/{shelterId}/events/{shelterEventId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long shelterId, @PathVariable Long shelterEventId)
            throws EntityNotFoundException, IllegalOperationException {
        ShelterEntity shelter = shelterService.getShelterById(shelterId);
        if (shelter == null) {
            throw new IllegalOperationException("Shelter not found with ID: " + shelterId);
        }
        ShelterEventEntity shelterEvent = shelterEventService.getShelterEventById(shelterEventId);
        if (shelterEvent == null || !shelterEvent.getShelter().getId().equals(shelterId)) {
            throw new EntityNotFoundException("Shelter event not found with ID: " + shelterEventId);
        }
        shelterEventService.deleteShelterEvent(shelterEventId);
    }
}
