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

import co.edu.udistrital.mdp.adopcion.dto.person.OwnerDTO;
import co.edu.udistrital.mdp.adopcion.dto.person.OwnerDetailDTO;
import co.edu.udistrital.mdp.adopcion.entities.person.OwnerEntity;
import co.edu.udistrital.mdp.adopcion.services.person.OwnerService;

@RestController
@RequestMapping("/owners")
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Get all owners.
     *
     * @return List of OwnerDetailDTO
     * @throws IllegalOperationException
     * @throws EntityNotFoundException
     */
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<OwnerDetailDTO> findAll() throws IllegalOperationException, EntityNotFoundException {
        List<OwnerEntity> owners = ownerService.getOwners();
        if (owners.isEmpty()) {
            throw new IllegalOperationException("No owners found.");
        }
        return modelMapper.map(owners, new TypeToken<List<OwnerDetailDTO>>() {
        }.getType());
    }

    /**
     * Get owner by ID.
     *
     * @param ownerId
     * @return OwnerDetailDTO
     * @throws EntityNotFoundException
     */
    @GetMapping("/{ownerId}")
    @ResponseStatus(code = HttpStatus.OK)
    public OwnerDetailDTO findOne(@PathVariable Long ownerId) 
            throws EntityNotFoundException, IllegalOperationException {
        OwnerEntity ownerEntity = ownerService.getOwner(ownerId);
        if (ownerEntity == null) {
            throw new EntityNotFoundException("Owner not found with ID: " + ownerId);
        }
        return modelMapper.map(ownerEntity, OwnerDetailDTO.class);
    }

    /**
     * Create a new owner.
     * 
     * @param ownerDTO
     * @return OwnerDTO
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public OwnerDTO create(@RequestBody OwnerDTO ownerDTO) 
            throws EntityNotFoundException, IllegalOperationException {
        OwnerEntity ownerEntity = modelMapper.map(ownerDTO, OwnerEntity.class);
        ownerEntity = ownerService.createOwner(ownerEntity);
        if (ownerEntity == null) {
            throw new IllegalOperationException("Failed to create owner.");
        }
        return modelMapper.map(ownerEntity, OwnerDTO.class);
    }

    /**
     * Update an owner by ID.
     *
     * @param ownerId
     * @param ownerDTO
     * @return OwnerDTO
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @PutMapping("/{ownerId}")
    @ResponseStatus(code = HttpStatus.OK)
    public OwnerDTO update(@PathVariable Long ownerId, @RequestBody OwnerDTO ownerDTO) 
            throws EntityNotFoundException, IllegalOperationException {
        OwnerEntity existingOwner = ownerService.getOwner(ownerId);
        if (existingOwner == null) {
            throw new EntityNotFoundException("Owner not found with ID: " + ownerId);
        }
        
        OwnerEntity ownerEntity = modelMapper.map(ownerDTO, OwnerEntity.class);
        ownerEntity.setId(ownerId);
        
        try {
            ownerEntity = ownerService.updateOwner(ownerId, ownerEntity, false);
            if (ownerEntity == null) {
                throw new IllegalOperationException("Failed to update owner with ID: " + ownerId);
            }
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Failed to update, owner not found with ID: " + ownerId);
        }
        
        return modelMapper.map(ownerEntity, OwnerDTO.class);
    }

    /**
     * Delete an owner by ID.
     *
     * @param ownerId
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @DeleteMapping("/{ownerId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long ownerId) 
            throws EntityNotFoundException, IllegalOperationException {
        OwnerEntity owner = ownerService.getOwner(ownerId);
        if (owner == null) {
            throw new EntityNotFoundException("Owner not found with ID: " + ownerId);
        }
        
        try {
            ownerService.deleteOwner(ownerId, true);
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Owner not found with ID: " + ownerId);
        } catch (SecurityException e) {
            throw new IllegalOperationException("Cannot delete owner: " + e.getMessage());
        }
    }
}