package co.edu.udistrital.mdp.adopcion.controllers.pet;

import java.util.List;

import org.modelmapper.Conditions;
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

import co.edu.udistrital.mdp.adopcion.dto.events.MedicalEventDTO;
import co.edu.udistrital.mdp.adopcion.dto.events.MedicalEventDetailDTO;
import co.edu.udistrital.mdp.adopcion.entities.events.MedicalEventEntity;
import co.edu.udistrital.mdp.adopcion.entities.pet.PetEntity;
import co.edu.udistrital.mdp.adopcion.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.adopcion.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.adopcion.services.events.MedicalEventService;
import co.edu.udistrital.mdp.adopcion.services.pet.PetService;

@RestController
@RequestMapping("/pets")
public class PetMedicalEventController {
    @Autowired
    private PetService petService;

    @Autowired
    private MedicalEventService medicalEventService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Get all medical events for a specific pet.
     * 
     * @param petId
     * @return List of MedicalEventDetailDTO
     * @throws EntityNotFoundException
     */
    @GetMapping("/{petId}/medical-events")
    @ResponseStatus(code = HttpStatus.OK)
    public List<MedicalEventDetailDTO> findAll(@PathVariable Long petId)
            throws EntityNotFoundException, IllegalOperationException {
        PetEntity pet = petService.getPet(petId);
        if (pet == null) {
            throw new EntityNotFoundException("Pet not found with ID: " + petId);
        }
        List<MedicalEventEntity> medicalEvents = medicalEventService.getAllMedicalEvents();
        medicalEvents.removeIf(event -> !event.getPet().getId().equals(petId));
        if (medicalEvents.isEmpty()) {
            throw new IllegalOperationException("No medical events found for pet");
        }
        return modelMapper.map(medicalEvents, new TypeToken<List<MedicalEventDetailDTO>>() {
        }.getType());
    }

    /**
     * Get a specific medical event by ID for a pet.
     * 
     * @param petId
     * @param eventId
     * @return MedicalEventDetailDTO
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @GetMapping("/{petId}/medical-events/{eventId}")
    @ResponseStatus(code = HttpStatus.OK)
    public MedicalEventDetailDTO findById(@PathVariable Long petId, @PathVariable Long eventId)
            throws EntityNotFoundException, IllegalOperationException {
        PetEntity pet = petService.getPet(petId);
        if (pet == null) {
            throw new EntityNotFoundException("Pet not found with ID: " + petId);
        }
        MedicalEventEntity medicalEvent = medicalEventService.getMedicalEventById(eventId);
        if (medicalEvent == null) {
            throw new EntityNotFoundException("Medical event not found with ID: " + eventId);
        }
        if (medicalEvent.getPet() == null) {
            throw new IllegalOperationException("Medical event with ID: " + eventId + " is not associated with any pet.");
        }
        if (!medicalEvent.getPet().getId().equals(petId)) {
            throw new EntityNotFoundException("Medical event not found with ID: " + eventId + " for pet with ID: " + petId);
        }
        return modelMapper.map(medicalEvent, MedicalEventDetailDTO.class);
    }

    /**
     * Create a new medical event for a specific pet.
     * 
     * @param petId
     * @param medicalEventId
     * @return MedicalEventDetailDTO
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @PostMapping("/{petId}/medical-events/{medicalEventId}")
    @ResponseStatus(code = HttpStatus.OK)
    public MedicalEventDTO create(@PathVariable Long petId, @PathVariable Long medicalEventId)
            throws EntityNotFoundException, IllegalOperationException {
        PetEntity pet = petService.getPet(petId);
        if (pet == null) {
            throw new EntityNotFoundException("Pet not found with ID: " + petId);
        }
        MedicalEventEntity medicalEventEntity = medicalEventService.getMedicalEventById(medicalEventId);
        if (medicalEventEntity == null) {
            throw new EntityNotFoundException("Medical event not found with ID: " + medicalEventId);
        }
        medicalEventEntity.setPet(pet);
        medicalEventEntity = medicalEventService.updateMedicalEvent(medicalEventEntity.getId(), medicalEventEntity);
        if (medicalEventEntity == null) {
            throw new IllegalOperationException("Failed to create medical event.");
        }
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        return modelMapper.map(medicalEventEntity, MedicalEventDTO.class);
    }

    /**
     * Assign a medical event list to a pet.
     * 
     * @param petId
     * @param medicalEventId
     * @return MedicalEventDTO
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @PutMapping("/{petId}/medical-events")
    @ResponseStatus(code = HttpStatus.OK)
    public List<MedicalEventDetailDTO> assignMedicalEvents(
            @PathVariable Long petId,
            @RequestBody List<MedicalEventDTO> medicalEventDTOs)
            throws EntityNotFoundException, IllegalOperationException {
        PetEntity pet = petService.getPet(petId);
        if (pet == null) {
            throw new EntityNotFoundException("Pet not found with ID: " + petId);
        }
        List<MedicalEventEntity> medicalEvents = modelMapper.map(medicalEventDTOs,
                new TypeToken<List<MedicalEventEntity>>() {
                }.getType());
        for (MedicalEventEntity medicalEvent : medicalEvents) {
            medicalEvent.setPet(pet);
            try {
                medicalEvent = medicalEventService.updateMedicalEvent(medicalEvent.getId(), medicalEvent);
            } catch (IllegalArgumentException e) {
                throw new EntityNotFoundException("Failed to update medical event with ID: " + medicalEvent.getId());
            }
        }
        medicalEvents = medicalEventService.getAllMedicalEvents();
        return modelMapper.map(medicalEvents, new TypeToken<List<MedicalEventDetailDTO>>() {
        }.getType());
    }

    /**
     * Delete a specific medical event for a pet.
     * 
     * @param petId
     * @param eventId
     * @throws EntityNotFoundException
     */
    @DeleteMapping("/{petId}/medical-events/{eventId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long petId, @PathVariable Long eventId)
            throws EntityNotFoundException, IllegalOperationException {
        PetEntity pet;
        try {
            pet = petService.getPet(petId);
        } catch (IllegalArgumentException e) {
            throw new IllegalOperationException("Pet not found with ID: " + petId);
        }
        if (pet == null) {
            throw new IllegalOperationException("Pet not found with ID: " + petId);
        }
        MedicalEventEntity medicalEvent = medicalEventService.getMedicalEventById(eventId);
        if (medicalEvent == null) {
            throw new EntityNotFoundException("Medical event not found with ID: " + eventId);
        }
        medicalEventService.deleteMedicalEvent(eventId);
    }
}
