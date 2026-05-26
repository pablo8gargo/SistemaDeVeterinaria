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

import co.edu.udistrital.mdp.adopcion.dto.adoption.AdoptionDTO;
import co.edu.udistrital.mdp.adopcion.dto.adoption.AdoptionDetailDTO;
import co.edu.udistrital.mdp.adopcion.entities.adoption.AdoptionEntity;
import co.edu.udistrital.mdp.adopcion.entities.person.OwnerEntity;
import co.edu.udistrital.mdp.adopcion.services.person.OwnerService;
import co.edu.udistrital.mdp.adopcion.services.adoption.AdoptionService;

@RestController
@RequestMapping("/owners")
public class OwnerAdoptionController {

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private AdoptionService adoptionService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     *
     * @param ownerId
     * @return List of AdoptionDetailDTO
     * @throws IllegalOperationException
     * @throws EntityNotFoundException
     */
    @GetMapping("/{ownerId}/adoptions")
    @ResponseStatus(code = HttpStatus.OK)
    public List<AdoptionDetailDTO> findAll(@PathVariable Long ownerId)
            throws IllegalOperationException, EntityNotFoundException {
        OwnerEntity owner = ownerService.getOwner(ownerId);
        if (owner == null) {
            throw new EntityNotFoundException("Owner not found with ID: " + ownerId);
        }
        List<AdoptionEntity> adoptions = adoptionService.getAllAdoptions();
        adoptions.removeIf(adoption -> !adoption.getOwner().getId().equals(ownerId));
        if (adoptions.isEmpty()) {
            throw new IllegalOperationException("No adoptions found.");
        }
        return modelMapper.map(adoptions, new TypeToken<List<AdoptionDetailDTO>>() {
        }.getType());
    }

    /**
     *
     * @param ownerId
     * @param adoptionId
     * @return AdoptionDetailDTO
     * @throws EntityNotFoundException
     */
    @GetMapping("/{ownerId}/adoptions/{adoptionId}")
    @ResponseStatus(code = HttpStatus.OK)
    public AdoptionDetailDTO findOne(@PathVariable Long ownerId, @PathVariable Long adoptionId)
            throws EntityNotFoundException, IllegalOperationException {
        OwnerEntity owner = ownerService.getOwner(ownerId);
        if (owner == null) {
            throw new IllegalOperationException("Owner not found with ID: " + ownerId);
        }
        AdoptionEntity adoption = adoptionService.getAdoptionById(adoptionId);
        if (adoption == null || !adoption.getOwner().getId().equals(ownerId)) {
            throw new EntityNotFoundException("Adoption not found with ID: " + adoptionId);
        }
        return modelMapper.map(adoption, AdoptionDetailDTO.class);
    }

    /**
     * 
     * @param ownerId
     * @param adoptionId
     * @return AdoptionDTO
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @PostMapping("/{ownerId}/adoptions/{adoptionId}")
    @ResponseStatus(code = HttpStatus.OK)
    public AdoptionDTO create(@PathVariable Long ownerId, @PathVariable Long adoptionId)
            throws EntityNotFoundException, IllegalOperationException {
        OwnerEntity owner = ownerService.getOwner(ownerId);
        if (owner == null) {
            throw new IllegalOperationException("Owner not found with ID: " + ownerId);
        }
        AdoptionEntity adoptionEntity = adoptionService.getAdoptionById(adoptionId);
        if (adoptionEntity == null) {
            throw new EntityNotFoundException("Adoption not found with ID: " + adoptionId);
        }
        adoptionEntity.setOwner(owner);
        adoptionEntity = adoptionService.updateAdoption(adoptionId, adoptionEntity);
        if (adoptionEntity == null) {
            throw new IllegalOperationException("Failed to assign adoption to owner.");
        }
        return modelMapper.map(adoptionEntity, AdoptionDTO.class);
    }

    /**
     *
     * @param ownerId
     * @param adoptionDTO
     * @return AdoptionDTO
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @PostMapping("/{ownerId}/adoptions")
    @ResponseStatus(code = HttpStatus.CREATED)
    public AdoptionDTO createNew(@PathVariable Long ownerId, @RequestBody AdoptionDTO adoptionDTO)
            throws EntityNotFoundException, IllegalOperationException {
        OwnerEntity owner = ownerService.getOwner(ownerId);
        if (owner == null) {
            throw new EntityNotFoundException("Owner not found with ID: " + ownerId);
        }
        
        AdoptionEntity adoptionEntity = modelMapper.map(adoptionDTO, AdoptionEntity.class);
        adoptionEntity.setOwner(owner);
        adoptionEntity = adoptionService.createAdoption(adoptionEntity);
        if (adoptionEntity == null) {
            throw new IllegalOperationException("Failed to create adoption.");
        }
        return modelMapper.map(adoptionEntity, AdoptionDTO.class);
    }

    /**
     *
     * @param ownerId
     * @param listAdoptionDTO
     * @return List<AdoptionDetailDTO>
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @PutMapping("/{ownerId}/adoptions")
    @ResponseStatus(code = HttpStatus.OK)
    public List<AdoptionDetailDTO> update(@PathVariable Long ownerId,
            @RequestBody List<AdoptionDTO> listAdoptionDTO)
            throws EntityNotFoundException, IllegalOperationException {
        OwnerEntity owner = ownerService.getOwner(ownerId);
        if (owner == null) {
            throw new IllegalOperationException("Owner not found with ID: " + ownerId);
        }
        List<AdoptionEntity> adoptionEntities = modelMapper.map(listAdoptionDTO,
                new TypeToken<List<AdoptionEntity>>() {
                }.getType());
        adoptionEntities.forEach(adoption -> adoption.setOwner(owner));
        for (AdoptionEntity adoption : adoptionEntities) {
            try {
                adoption = adoptionService.updateAdoption(adoption.getId(), adoption);
            } catch (IllegalArgumentException e) {
                throw new EntityNotFoundException(
                        "Failed to update, not found adoption with ID: " + adoption.getId());
            }
        }
        return findAll(ownerId);
    }

    /**
     *
     * @param ownerId
     * @param adoptionId
     * @throws EntityNotFoundException
     */
    @DeleteMapping("/{ownerId}/adoptions/{adoptionId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long ownerId, @PathVariable Long adoptionId)
            throws EntityNotFoundException, IllegalOperationException {
        OwnerEntity owner = ownerService.getOwner(ownerId);
        if (owner == null) {
            throw new IllegalOperationException("Owner not found with ID: " + ownerId);
        }
        AdoptionEntity adoption = adoptionService.getAdoptionById(adoptionId);
        if (adoption == null || !adoption.getOwner().getId().equals(ownerId)) {
            throw new EntityNotFoundException("Adoption not found with ID: " + adoptionId);
        }
        adoptionService.deleteAdoption(adoptionId);
    }
}