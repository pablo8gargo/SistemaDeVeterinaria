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

import co.edu.udistrital.mdp.adopcion.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.adopcion.exceptions.IllegalOperationException;

import co.edu.udistrital.mdp.adopcion.dto.adoption.AdoptionApplicationDTO;
import co.edu.udistrital.mdp.adopcion.dto.adoption.AdoptionApplicationDetailDTO;
import co.edu.udistrital.mdp.adopcion.entities.adoption.AdoptionApplicationEntity;
import co.edu.udistrital.mdp.adopcion.entities.person.OwnerEntity;
import co.edu.udistrital.mdp.adopcion.services.person.OwnerService;
import co.edu.udistrital.mdp.adopcion.services.adoption.AdoptionApplicationService;

@RestController
@RequestMapping("/owners")
public class OwnerAdoptionApplicationController {

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private AdoptionApplicationService adoptionApplicationService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Get all adoption applications for a specific owner.
     *
     * @param ownerId
     * @return List of AdoptionApplicationDetailDTO
     * @throws IllegalOperationException
     * @throws EntityNotFoundException
     */
    @GetMapping("/{ownerId}/adoption-applications")
    @ResponseStatus(code = HttpStatus.OK)
    public List<AdoptionApplicationDetailDTO> findAll(@PathVariable Long ownerId)
            throws IllegalOperationException, EntityNotFoundException {
        OwnerEntity owner = ownerService.getOwner(ownerId);
        if (owner == null) {
            throw new EntityNotFoundException("Owner not found with ID: " + ownerId);
        }
        List<AdoptionApplicationEntity> adoptionApplications = adoptionApplicationService.getAllApplications();
        adoptionApplications.removeIf(app -> !app.getOwner().getId().equals(ownerId));
        if (adoptionApplications.isEmpty()) {
            throw new IllegalOperationException("No adoption applications found.");
        }
        return modelMapper.map(adoptionApplications, new TypeToken<List<AdoptionApplicationDetailDTO>>() {
        }.getType());
    }

    /**
     * Get adoption application by ID for a specific owner.
     *
     * @param ownerId
     * @param adoptionApplicationId
     * @return AdoptionApplicationDetailDTO
     * @throws EntityNotFoundException
     */
    @GetMapping("/{ownerId}/adoption-applications/{adoptionApplicationId}")
    @ResponseStatus(code = HttpStatus.OK)
    public AdoptionApplicationDetailDTO findOne(@PathVariable Long ownerId, @PathVariable Long adoptionApplicationId)
            throws EntityNotFoundException, IllegalOperationException {
        OwnerEntity owner = ownerService.getOwner(ownerId);
        if (owner == null) {
            throw new IllegalOperationException("Owner not found with ID: " + ownerId);
        }
        AdoptionApplicationEntity adoptionApplication = adoptionApplicationService.getApplicationById(adoptionApplicationId);
        if (adoptionApplication == null || !adoptionApplication.getOwner().getId().equals(ownerId)) {
            throw new EntityNotFoundException("Adoption application not found with ID: " + adoptionApplicationId);
        }
        return modelMapper.map(adoptionApplication, AdoptionApplicationDetailDTO.class);
    }

    /**
     * Assign an adoption application to a specific owner.
     * 
     * @param ownerId
     * @param adoptionApplicationId
     * @return AdoptionApplicationDTO
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @PostMapping("/{ownerId}/adoption-applications/{adoptionApplicationId}")
    @ResponseStatus(code = HttpStatus.OK)
    public AdoptionApplicationDTO create(@PathVariable Long ownerId, @PathVariable Long adoptionApplicationId)
            throws EntityNotFoundException, IllegalOperationException {
        OwnerEntity owner = ownerService.getOwner(ownerId);
        if (owner == null) {
            throw new IllegalOperationException("Owner not found with ID: " + ownerId);
        }
        AdoptionApplicationEntity adoptionApplicationEntity = adoptionApplicationService.getApplicationById(adoptionApplicationId);
        if (adoptionApplicationEntity == null) {
            throw new EntityNotFoundException("Adoption application not found with ID: " + adoptionApplicationId);
        }
        adoptionApplicationEntity.setOwner(owner);
        adoptionApplicationEntity = adoptionApplicationService.updateApplication(adoptionApplicationId, adoptionApplicationEntity);
        if (adoptionApplicationEntity == null) {
            throw new IllegalOperationException("Failed to assign adoption application to owner.");
        }
        return modelMapper.map(adoptionApplicationEntity, AdoptionApplicationDTO.class);
    }

    /**
     * Create a new adoption application for a specific owner.
     *
     * @param ownerId
     * @param adoptionApplicationDTO
     * @return AdoptionApplicationDTO
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @PostMapping("/{ownerId}/adoption-applications")
    @ResponseStatus(code = HttpStatus.CREATED)
    public AdoptionApplicationDTO createNew(@PathVariable Long ownerId, @RequestBody AdoptionApplicationDTO adoptionApplicationDTO)
            throws EntityNotFoundException, IllegalOperationException {
        OwnerEntity owner = ownerService.getOwner(ownerId);
        if (owner == null) {
            throw new EntityNotFoundException("Owner not found with ID: " + ownerId);
        }
        
        AdoptionApplicationEntity adoptionApplicationEntity = modelMapper.map(adoptionApplicationDTO, AdoptionApplicationEntity.class);
        adoptionApplicationEntity.setOwner(owner);
        adoptionApplicationEntity = adoptionApplicationService.createApplication(adoptionApplicationEntity);
        if (adoptionApplicationEntity == null) {
            throw new IllegalOperationException("Failed to create adoption application.");
        }
        return modelMapper.map(adoptionApplicationEntity, AdoptionApplicationDTO.class);
    }

    /**
     * Assign an adoption application list to a specific owner.
     *
     * @param ownerId
     * @param listAdoptionApplicationDTO
     * @return List<AdoptionApplicationDetailDTO>
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @PutMapping("/{ownerId}/adoption-applications")
    @ResponseStatus(code = HttpStatus.OK)
    public List<AdoptionApplicationDetailDTO> update(@PathVariable Long ownerId,
            @RequestBody List<AdoptionApplicationDTO> listAdoptionApplicationDTO)
            throws EntityNotFoundException, IllegalOperationException {
        OwnerEntity owner = ownerService.getOwner(ownerId);
        if (owner == null) {
            throw new IllegalOperationException("Owner not found with ID: " + ownerId);
        }
        List<AdoptionApplicationEntity> adoptionApplicationEntities = modelMapper.map(listAdoptionApplicationDTO,
                new TypeToken<List<AdoptionApplicationEntity>>() {
                }.getType());
        adoptionApplicationEntities.forEach(app -> app.setOwner(owner));
        for (AdoptionApplicationEntity app : adoptionApplicationEntities) {
            try {
                app = adoptionApplicationService.updateApplication(app.getId(), app);
            } catch (IllegalArgumentException e) {
                throw new EntityNotFoundException(
                        "Failed to update, not found adoption application with ID: " + app.getId());
            }
        }
        return findAll(ownerId);
    }

    /**
     * Delete an adoption application by ID for a specific owner.
     *
     * @param ownerId
     * @param adoptionApplicationId
     * @throws EntityNotFoundException
     */
    @DeleteMapping("/{ownerId}/adoption-applications/{adoptionApplicationId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long ownerId, @PathVariable Long adoptionApplicationId)
            throws EntityNotFoundException, IllegalOperationException {
        OwnerEntity owner = ownerService.getOwner(ownerId);
        if (owner == null) {
            throw new IllegalOperationException("Owner not found with ID: " + ownerId);
        }
        AdoptionApplicationEntity adoptionApplication = adoptionApplicationService.getApplicationById(adoptionApplicationId);
        if (adoptionApplication == null || !adoptionApplication.getOwner().getId().equals(ownerId)) {
            throw new EntityNotFoundException("Adoption application not found with ID: " + adoptionApplicationId);
        }
        adoptionApplicationService.deleteApplication(adoptionApplicationId);
    }
}