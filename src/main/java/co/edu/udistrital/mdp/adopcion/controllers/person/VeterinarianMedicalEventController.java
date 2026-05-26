package co.edu.udistrital.mdp.adopcion.controllers.person;

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

import co.edu.udistrital.mdp.adopcion.dto.events.MedicalEventDTO;
import co.edu.udistrital.mdp.adopcion.dto.events.MedicalEventDetailDTO;
import co.edu.udistrital.mdp.adopcion.entities.events.MedicalEventEntity;
import co.edu.udistrital.mdp.adopcion.entities.person.VeterinarianEntity;
import co.edu.udistrital.mdp.adopcion.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.adopcion.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.adopcion.services.events.MedicalEventService;
import co.edu.udistrital.mdp.adopcion.services.person.VeterinarianService;

@RestController
@RequestMapping("/veterinarians")
public class VeterinarianMedicalEventController {
    @Autowired
    private VeterinarianService veterinarianService;
    @Autowired
    private MedicalEventService medicalEventService;
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Get all medical events for a specific veterinarian.
     *
     * @param veterinarianId
     * @return
     * @throws EntityNotFoundException
     */
    @GetMapping("/{veterinarianId}/medical-events")
    @ResponseStatus(code = HttpStatus.OK)
    public List<MedicalEventDetailDTO> findAll(@PathVariable Long veterinarianId)
            throws EntityNotFoundException, IllegalOperationException {
        VeterinarianEntity veterinarian = veterinarianService.getVeterinarianById(veterinarianId);
        if (veterinarian == null) {
            throw new IllegalOperationException("Veterinarian not found with ID: " + veterinarianId);
        }
        List<MedicalEventEntity> medicalEvents = medicalEventService.getAllMedicalEvents();
        medicalEvents.removeIf(medicalEvent -> !medicalEvent.getVeterinarian().getId().equals(veterinarianId));
        if (medicalEvents.isEmpty()) {
            throw new EntityNotFoundException("No medical events found for veterinarian with ID: " + veterinarianId);
        }
        return modelMapper.map(medicalEvents, new TypeToken<List<MedicalEventDetailDTO>>() {
        }.getType());
    }

    /**
     * Get medical event by ID for a specific veterinarian.
     *
     * @param veterinarianId
     * @param medicalEventId
     * @return
     * @throws EntityNotFoundException
     */
    @GetMapping("/{veterinarianId}/medical-events/{medicalEventId}")
    @ResponseStatus(code = HttpStatus.OK)
    public MedicalEventDetailDTO findOne(@PathVariable Long veterinarianId, @PathVariable Long medicalEventId)
            throws EntityNotFoundException, IllegalOperationException {
        VeterinarianEntity veterinarian = veterinarianService.getVeterinarianById(veterinarianId);
        if (veterinarian == null) {
            throw new IllegalOperationException("Veterinarian not found with ID: " + veterinarianId);
        }
        MedicalEventEntity medicalEventEntity = medicalEventService.getMedicalEventById(medicalEventId);
        if (medicalEventEntity == null || !medicalEventEntity.getVeterinarian().getId().equals(veterinarianId)) {
            throw new EntityNotFoundException(
                    "In this veterinarian, medical event not found with ID: " + medicalEventId);
        }
        return modelMapper.map(medicalEventEntity, MedicalEventDetailDTO.class);
    }

    /**
     * Replace all medical events for a specific veterinarian.
     *
     * @param veterinarianId
     * @param listMedicalEventDTO
     * @return
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @PutMapping("/{veterinarianId}/medical-events")
    @ResponseStatus(code = HttpStatus.OK)
    public List<MedicalEventDetailDTO> replaceMedicalEvents(@RequestBody List<MedicalEventDTO> listMedicalEventDTO,
            @PathVariable Long veterinarianId)
            throws EntityNotFoundException, IllegalOperationException {
        VeterinarianEntity veterinarian = veterinarianService.getVeterinarianById(veterinarianId);
        if (veterinarian == null) {
            throw new IllegalOperationException("Veterinarian not found with ID: " + veterinarianId);
        }
        List<MedicalEventEntity> medicalEventEntities = modelMapper.map(listMedicalEventDTO,
                new TypeToken<List<MedicalEventEntity>>() {
                }.getType());
        medicalEventEntities.forEach(medicalEvent -> medicalEvent.setVeterinarian(veterinarian));
        for (MedicalEventEntity medicalEvent : medicalEventEntities) {
            try {
                medicalEvent = medicalEventService.updateMedicalEvent(medicalEvent.getId(), medicalEvent);
            } catch (IllegalArgumentException e) {
                throw new EntityNotFoundException(
                        "Failed to update, not found medical event with ID: " + medicalEvent.getId());
            }
        }
        medicalEventEntities = medicalEventService.getAllMedicalEvents();
        return modelMapper.map(medicalEventEntities, new TypeToken<List<MedicalEventDetailDTO>>() {
        }.getType());
    }

    /**
     * Add an existing medical event to a specific veterinarian.
     *
     * @param veterinarianId
     * @param medicalEventId
     * @return
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @PostMapping("/{veterinarianId}/medical-events/{medicalEventId}")
    @ResponseStatus(code = HttpStatus.OK)
    public MedicalEventDetailDTO addMedicalEvent(@PathVariable Long veterinarianId, @PathVariable Long medicalEventId)
            throws EntityNotFoundException, IllegalOperationException {
        VeterinarianEntity veterinarian = veterinarianService.getVeterinarianById(veterinarianId);
        if (veterinarian == null) {
            throw new EntityNotFoundException("Veterinarian not found with ID: " + veterinarianId);
        }
        MedicalEventEntity medicalEventEntity = medicalEventService.getMedicalEventById(medicalEventId);
        if (medicalEventEntity == null || !medicalEventEntity.getVeterinarian().getId().equals(veterinarianId)) {
            throw new EntityNotFoundException("Medical event not found with ID: " + medicalEventId);
        }
        medicalEventEntity.setVeterinarian(veterinarian);
        medicalEventEntity = medicalEventService.updateMedicalEvent(medicalEventId, medicalEventEntity);
        if (medicalEventEntity == null) {
            throw new IllegalOperationException(
                    "Failed to update, not found medical event with ID: " + medicalEventId);
        }
        return modelMapper.map(medicalEventEntity, MedicalEventDetailDTO.class);
    }

/**
 * Delete a medical event by ID for a specific veterinarian.
 *
 * @param veterinarianId
 * @param medicalEventId
 * @throws EntityNotFoundException
 */
@DeleteMapping("/{veterinarianId}/medical-events/{medicalEventId}")
@ResponseStatus(code = HttpStatus.NO_CONTENT)
public void delete(@PathVariable Long veterinarianId, @PathVariable Long medicalEventId)
        throws EntityNotFoundException, IllegalOperationException {
    VeterinarianEntity veterinarian = veterinarianService.getVeterinarianById(veterinarianId);
    if (veterinarian == null) {
        throw new EntityNotFoundException("Veterinarian not found with ID: " + veterinarianId);
    }
    MedicalEventEntity medicalEventEntity = medicalEventService.getMedicalEventById(medicalEventId);
    if (medicalEventEntity == null) {
        throw new EntityNotFoundException("Medical event not found with ID: " + medicalEventId);
    }
    if (!medicalEventEntity.getVeterinarian().getId().equals(veterinarianId)) {
        throw new EntityNotFoundException(
                "Medical event with ID " + medicalEventId + " is not associated with veterinarian ID " + veterinarianId);
    }
    medicalEventService.deleteMedicalEvent(medicalEventId);
}
}