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

import co.edu.udistrital.mdp.adopcion.dto.adoption.AdoptionFollowUpDTO;
import co.edu.udistrital.mdp.adopcion.dto.adoption.AdoptionFollowUpDetailDTO;
import co.edu.udistrital.mdp.adopcion.entities.adoption.AdoptionFollowUpEntity;
import co.edu.udistrital.mdp.adopcion.entities.person.VeterinarianEntity;
import co.edu.udistrital.mdp.adopcion.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.adopcion.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.adopcion.services.adoption.AdoptionFollowUpService;
import co.edu.udistrital.mdp.adopcion.services.person.VeterinarianService;

@RestController
@RequestMapping("/veterinarians")
public class VeterinarianAdoptionFollowUpController {
    @Autowired
    private VeterinarianService veterinarianService;
    @Autowired
    private AdoptionFollowUpService adoptionFollowUpService;
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Get all adoption follow ups for a specific veterinarian.
     *
     * @param veterinarianId
     * @return
     * @throws EntityNotFoundException
     */
    @GetMapping("/{veterinarianId}/adoptionfollowup")
    @ResponseStatus(code = HttpStatus.OK)
    public List<AdoptionFollowUpDetailDTO> findAll(@PathVariable Long veterinarianId)
            throws EntityNotFoundException, IllegalOperationException {
        VeterinarianEntity veterinarian = veterinarianService.getVeterinarianById(veterinarianId);
        if (veterinarian == null) {
            throw new EntityNotFoundException("Veterinarian not found with ID: " + veterinarianId);
        }
        List<AdoptionFollowUpEntity> adoptionFollowUps = adoptionFollowUpService.getAllFollowUps();
        adoptionFollowUps.removeIf(followUp -> !followUp.getVeterinarian().getId().equals(veterinarianId));
        if (adoptionFollowUps.isEmpty()) {
            throw new IllegalOperationException("No adoption follow ups found for veterinarian with ID: " + veterinarianId);
        }
        return modelMapper.map(adoptionFollowUps, new TypeToken<List<AdoptionFollowUpDetailDTO>>() {
        }.getType());
    }

    /**
     * Get adoption follow up by ID for a specific veterinarian.
     *
     * @param veterinarianId
     * @param followUpId
     * @return
     * @throws EntityNotFoundException
     */
    @GetMapping("/{veterinarianId}/adoptionfollowup/{followUpId}")
    @ResponseStatus(code = HttpStatus.OK)
    public AdoptionFollowUpDetailDTO findOne(@PathVariable Long veterinarianId, @PathVariable Long followUpId)
            throws EntityNotFoundException, IllegalOperationException {
        VeterinarianEntity veterinarian = veterinarianService.getVeterinarianById(veterinarianId);
        if (veterinarian == null) {
            throw new EntityNotFoundException("Veterinarian not found with ID: " + veterinarianId);
        }
        AdoptionFollowUpEntity adoptionFollowUpEntity = adoptionFollowUpService.getFollowUpById(followUpId);
        if (adoptionFollowUpEntity == null || !adoptionFollowUpEntity.getVeterinarian().getId().equals(veterinarianId)) {
            throw new IllegalOperationException(
                    "In this veterinarian, adoption follow up not found with ID: " + followUpId);
        }
        return modelMapper.map(adoptionFollowUpEntity, AdoptionFollowUpDetailDTO.class);
    }

    /**
     * Replace all adoption follow ups for a specific veterinarian.
     *
     * @param veterinarianId
     * @param listAdoptionFollowUpDTO
     * @return
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @PutMapping("/{veterinarianId}/adoptionfollowup")
    @ResponseStatus(code = HttpStatus.OK)
    public List<AdoptionFollowUpDetailDTO> replaceFollowUps(@RequestBody List<AdoptionFollowUpDTO> listAdoptionFollowUpDTO,
            @PathVariable Long veterinarianId)
            throws EntityNotFoundException, IllegalOperationException {
        VeterinarianEntity veterinarian = veterinarianService.getVeterinarianById(veterinarianId);
        if (veterinarian == null) {
            throw new EntityNotFoundException("Veterinarian not found with ID: " + veterinarianId);
        }
        List<AdoptionFollowUpEntity> adoptionFollowUpEntities = modelMapper.map(listAdoptionFollowUpDTO,
                new TypeToken<List<AdoptionFollowUpEntity>>() {
                }.getType());
        adoptionFollowUpEntities.forEach(followUp -> followUp.setVeterinarian(veterinarian));
        for (AdoptionFollowUpEntity followUp : adoptionFollowUpEntities) {
            try {
                followUp = adoptionFollowUpService.updateFollowUp(followUp.getId(), followUp);
            } catch (IllegalArgumentException e) {
                throw new EntityNotFoundException(
                        "Failed to update, not found adoption follow up with ID: " + followUp.getId());
            }
        }
        adoptionFollowUpEntities = adoptionFollowUpService.getAllFollowUps();
        return modelMapper.map(adoptionFollowUpEntities, new TypeToken<List<AdoptionFollowUpDetailDTO>>() {
        }.getType());
    }

    /**
     * Add an existing adoption follow up to a specific veterinarian.
     *
     * @param veterinarianId
     * @param followUpId
     * @return
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @PostMapping("/{veterinarianId}/adoptionfollowup/{followUpId}")
    @ResponseStatus(code = HttpStatus.OK)
    public AdoptionFollowUpDetailDTO addFollowUp(@PathVariable Long veterinarianId, @PathVariable Long followUpId)
            throws EntityNotFoundException, IllegalOperationException {
        VeterinarianEntity veterinarian = veterinarianService.getVeterinarianById(veterinarianId);
        if (veterinarian == null) {
            throw new EntityNotFoundException("Veterinarian not found with ID: " + veterinarianId);
        }
        AdoptionFollowUpEntity adoptionFollowUpEntity = adoptionFollowUpService.getFollowUpById(followUpId);
        if (adoptionFollowUpEntity == null || !adoptionFollowUpEntity.getVeterinarian().getId().equals(veterinarianId)) {
            throw new EntityNotFoundException("Adoption follow up not found with ID: " + followUpId);
        }
        adoptionFollowUpEntity.setVeterinarian(veterinarian);
        adoptionFollowUpEntity = adoptionFollowUpService.updateFollowUp(followUpId, adoptionFollowUpEntity);
        if (adoptionFollowUpEntity == null) {
            throw new IllegalOperationException(
                    "Failed to update, not found adoption follow up with ID: " + followUpId);
        }
        return modelMapper.map(adoptionFollowUpEntity, AdoptionFollowUpDetailDTO.class);
    }

    /**
     * Delete an adoption follow up by ID for a specific veterinarian.
     *
     * @param veterinarianId
     * @param followUpId
     * @throws EntityNotFoundException
     */
    @DeleteMapping("/{veterinarianId}/adoptionfollowup/{followUpId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long veterinarianId, @PathVariable Long followUpId)
            throws EntityNotFoundException, IllegalOperationException {
        VeterinarianEntity veterinarian = veterinarianService.getVeterinarianById(veterinarianId);
        if (veterinarian == null) {
            throw new EntityNotFoundException("Veterinarian not found with ID: " + veterinarianId);
        }
        AdoptionFollowUpEntity adoptionFollowUpEntity = adoptionFollowUpService.getFollowUpById(followUpId);
        if (adoptionFollowUpEntity == null || !adoptionFollowUpEntity.getVeterinarian().getId().equals(veterinarianId)) {
            throw new IllegalOperationException(
                    "In this veterinarian, adoption follow up not found with ID: " + followUpId);
        }
        adoptionFollowUpService.deleteFollowUp(followUpId);
    }
}