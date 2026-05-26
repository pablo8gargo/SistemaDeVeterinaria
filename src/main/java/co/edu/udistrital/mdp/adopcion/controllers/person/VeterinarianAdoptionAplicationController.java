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

import co.edu.udistrital.mdp.adopcion.dto.adoption.AdoptionApplicationDTO;
import co.edu.udistrital.mdp.adopcion.dto.adoption.AdoptionApplicationDetailDTO;
import co.edu.udistrital.mdp.adopcion.entities.adoption.AdoptionApplicationEntity;
import co.edu.udistrital.mdp.adopcion.entities.person.VeterinarianEntity;
import co.edu.udistrital.mdp.adopcion.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.adopcion.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.adopcion.services.adoption.AdoptionApplicationService;
import co.edu.udistrital.mdp.adopcion.services.person.VeterinarianService;

@RestController
@RequestMapping("/veterinarians")
public class VeterinarianAdoptionAplicationController {
    @Autowired
    private VeterinarianService veterinarianService;
    @Autowired
    private AdoptionApplicationService adoptionAplicationService;
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Get all adoption applications for a specific veterinarian.
     *
     * @param veterinarianId
     * @return
     * @throws EntityNotFoundException
     */
    @GetMapping("/{veterinarianId}/adoption-applications")
    @ResponseStatus(code = HttpStatus.OK)
    public List<AdoptionApplicationDetailDTO> findAll(@PathVariable Long veterinarianId)
            throws EntityNotFoundException, IllegalOperationException {
        VeterinarianEntity veterinarian = veterinarianService.getVeterinarianById(veterinarianId);
        if (veterinarian == null) {
            throw new EntityNotFoundException("Veterinarian not found with ID: " + veterinarianId);
        }
        List<AdoptionApplicationEntity> adoptionApplications = adoptionAplicationService.getAllApplications();
        adoptionApplications.removeIf(application -> !application.getVeterinarian().getId().equals(veterinarianId));
        if (adoptionApplications.isEmpty()) {
            throw new IllegalOperationException("No adoption applications found for veterinarian with ID: " + veterinarianId);
        }
        return modelMapper.map(adoptionApplications, new TypeToken<List<AdoptionApplicationDetailDTO>>() {
        }.getType());
    }

    /**
     * Get adoption application by ID for a specific veterinarian.
     *
     * @param veterinarianId
     * @param adoptionAplicationId
     * @return
     * @throws EntityNotFoundException
     */
    @GetMapping("/{veterinarianId}/adoption-applications/{adoptionAplicationId}")
    @ResponseStatus(code = HttpStatus.OK)
    public AdoptionApplicationDetailDTO findOne(@PathVariable Long veterinarianId, @PathVariable Long adoptionApplicationId)
            throws EntityNotFoundException, IllegalOperationException {
        VeterinarianEntity veterinarian = veterinarianService.getVeterinarianById(veterinarianId);
        if (veterinarian == null) {
            throw new EntityNotFoundException("Veterinarian not found with ID: " + veterinarianId);
        }
        AdoptionApplicationEntity adoptionApplicationEntity = adoptionAplicationService.getApplicationById(adoptionApplicationId);
        if (adoptionApplicationEntity == null || !adoptionApplicationEntity.getVeterinarian().getId().equals(veterinarianId)) {
            throw new IllegalOperationException(
                    "In this veterinarian, adoption application not found with ID: " + adoptionApplicationId);
        }
        return modelMapper.map(adoptionApplicationEntity, AdoptionApplicationDetailDTO.class);
    }

    /**
     * Create a new adoption application for a specific veterinarian.
     *
     * @param veterinarianId
     * @param listAdoptionAplicationDTO
     * @return
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @PutMapping("/{veterinarianId}/adoption-applications")
    @ResponseStatus(code = HttpStatus.OK)
    public List<AdoptionApplicationDetailDTO> replaceApplications(@RequestBody List<AdoptionApplicationDTO> listAdoptionApplicationDTO,
            @PathVariable Long veterinarianId)
            throws EntityNotFoundException, IllegalOperationException {
        VeterinarianEntity veterinarian = veterinarianService.getVeterinarianById(veterinarianId);
        if (veterinarian == null) {
            throw new EntityNotFoundException("Veterinarian not found with ID: " + veterinarianId);
        }
        List<AdoptionApplicationEntity> adoptionApplicationEntities = modelMapper.map(listAdoptionApplicationDTO,
                new TypeToken<List<AdoptionApplicationEntity>>() {
                }.getType());
        adoptionApplicationEntities.forEach(application -> application.setVeterinarian(veterinarian));
        for (AdoptionApplicationEntity application : adoptionApplicationEntities) {
            try {
                application = adoptionAplicationService.updateApplication(application.getId(), application);
            } catch (IllegalArgumentException e) {
                throw new EntityNotFoundException(
                        "Failed to update, not found adoption application with ID: " + application.getId());
            }
        }
        adoptionApplicationEntities = adoptionAplicationService.getAllApplications();
        return modelMapper.map(adoptionApplicationEntities, new TypeToken<List<AdoptionApplicationDetailDTO>>() {
        }.getType());
    }

    /**
     * Update an existing adoption application for a specific veterinarian.
     *
     * @param veterinarianId
     * @param adoptionAplicationId
     * @return
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @PostMapping("/{veterinarianId}/adoption-applications/{adoptionAplicationId}")
    @ResponseStatus(code = HttpStatus.OK)
    public AdoptionApplicationDetailDTO addApplication(@PathVariable Long veterinarianId, @PathVariable Long adoptionApplicationId)
            throws EntityNotFoundException, IllegalOperationException {
        VeterinarianEntity veterinarian = veterinarianService.getVeterinarianById(veterinarianId);
        if (veterinarian == null) {
            throw new EntityNotFoundException("Veterinarian not found with ID: " + veterinarianId);
        }
        AdoptionApplicationEntity adoptionApplicationEntity = adoptionAplicationService.getApplicationById(adoptionApplicationId);
        if (adoptionApplicationEntity == null || !adoptionApplicationEntity.getVeterinarian().getId().equals(veterinarianId)) {
            throw new EntityNotFoundException("Adoption application not found with ID: " + adoptionApplicationId);
        }
        adoptionApplicationEntity.setVeterinarian(veterinarian);
        adoptionApplicationEntity = adoptionAplicationService.updateApplication(adoptionApplicationId, adoptionApplicationEntity);
        if (adoptionApplicationEntity == null) {
            throw new IllegalOperationException(
                    "Failed to update, not found adoption application with ID: " + adoptionApplicationId);
        }
        return modelMapper.map(adoptionApplicationEntity, AdoptionApplicationDetailDTO.class);
    }

    /**
     * Delete an adoption application by ID for a specific veterinarian.
     *
     * @param veterinarianId
     * @param adoptionAplicationId
     * @throws EntityNotFoundException
     */
    @DeleteMapping("/{veterinarianId}/adoption-applications/{adoptionAplicationId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long veterinarianId, @PathVariable Long adoptionAplicationId)
            throws EntityNotFoundException, IllegalOperationException {
        VeterinarianEntity veterinarian = veterinarianService.getVeterinarianById(veterinarianId);
        if (veterinarian == null) {
            throw new EntityNotFoundException("Veterinarian not found with ID: " + veterinarianId);
        }
        AdoptionApplicationEntity adoptionAplicationEntity = adoptionAplicationService.getApplicationById(adoptionAplicationId);
        if (adoptionAplicationEntity == null || !adoptionAplicationEntity.getVeterinarian().getId().equals(veterinarianId)) {
            throw new IllegalOperationException(
                    "In this veterinarian, adoption application not found with ID: " + adoptionAplicationId);
        }
        adoptionAplicationService.deleteApplication(adoptionAplicationId);
    }
}