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

import co.edu.udistrital.mdp.adopcion.dto.adoption.AdoptionTestDTO;
import co.edu.udistrital.mdp.adopcion.dto.adoption.AdoptionTestDetailDTO;
import co.edu.udistrital.mdp.adopcion.entities.adoption.AdoptionTestEntity;
import co.edu.udistrital.mdp.adopcion.entities.person.OwnerEntity;
import co.edu.udistrital.mdp.adopcion.services.person.OwnerService;
import co.edu.udistrital.mdp.adopcion.services.adoption.AdoptionTestService;

@RestController
@RequestMapping("/owners")
public class OwnerAdoptionTestController {

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private AdoptionTestService adoptionTestService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     *
     * @param ownerId
     * @return List of AdoptionTestDetailDTO
     * @throws IllegalOperationException
     * @throws EntityNotFoundException
     */
    @GetMapping("/{ownerId}/adoption-tests")
    @ResponseStatus(code = HttpStatus.OK)
    public List<AdoptionTestDetailDTO> findAll(@PathVariable Long ownerId)
            throws IllegalOperationException, EntityNotFoundException {
        OwnerEntity owner = ownerService.getOwner(ownerId);
        if (owner == null) {
            throw new EntityNotFoundException("Owner not found with ID: " + ownerId);
        }
        List<AdoptionTestEntity> adoptionTests = adoptionTestService.getAllTests();
        adoptionTests.removeIf(test -> !test.getOwner().getId().equals(ownerId));
        if (adoptionTests.isEmpty()) {
            throw new IllegalOperationException("No adoption tests found.");
        }
        return modelMapper.map(adoptionTests, new TypeToken<List<AdoptionTestDetailDTO>>() {
        }.getType());
    }

    /**
     *
     * @param ownerId
     * @param adoptionTestId
     * @return AdoptionTestDetailDTO
     * @throws EntityNotFoundException
     */
    @GetMapping("/{ownerId}/adoption-tests/{adoptionTestId}")
    @ResponseStatus(code = HttpStatus.OK)
    public AdoptionTestDetailDTO findOne(@PathVariable Long ownerId, @PathVariable Long adoptionTestId)
            throws EntityNotFoundException, IllegalOperationException {
        OwnerEntity owner = ownerService.getOwner(ownerId);
        if (owner == null) {
            throw new IllegalOperationException("Owner not found with ID: " + ownerId);
        }
        AdoptionTestEntity adoptionTest = adoptionTestService.getTestById(adoptionTestId);
        if (adoptionTest == null || !adoptionTest.getOwner().getId().equals(ownerId)) {
            throw new EntityNotFoundException("Adoption test not found with ID: " + adoptionTestId);
        }
        return modelMapper.map(adoptionTest, AdoptionTestDetailDTO.class);
    }

    /**
     * 
     * @param ownerId
     * @param adoptionTestId
     * @return AdoptionTestDetailDTO
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @PostMapping("/{ownerId}/adoption-tests/{adoptionTestId}")
    @ResponseStatus(code = HttpStatus.OK)
    public AdoptionTestDTO create(@PathVariable Long ownerId, @PathVariable Long adoptionTestId)
            throws EntityNotFoundException, IllegalOperationException {
        OwnerEntity owner = ownerService.getOwner(ownerId);
        if (owner == null) {
            throw new IllegalOperationException("Owner not found with ID: " + ownerId);
        }
        AdoptionTestEntity adoptionTestEntity = adoptionTestService.getTestById(adoptionTestId);
        if (adoptionTestEntity == null) {
            throw new EntityNotFoundException("Adoption test not found with ID: " + adoptionTestId);
        }
        adoptionTestEntity.setOwner(owner);
        adoptionTestEntity = adoptionTestService.updateTest(adoptionTestId, adoptionTestEntity);
        if (adoptionTestEntity == null) {
            throw new IllegalOperationException("Failed to assign adoption test to owner.");
        }
        return modelMapper.map(adoptionTestEntity, AdoptionTestDTO.class);
    }

    /**
     *
     * @param ownerId
     * @param adoptionTestDTO
     * @return AdoptionTestDTO
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @PostMapping("/{ownerId}/adoption-tests")
    @ResponseStatus(code = HttpStatus.CREATED)
    public AdoptionTestDTO createNew(@PathVariable Long ownerId, @RequestBody AdoptionTestDTO adoptionTestDTO)
            throws EntityNotFoundException, IllegalOperationException {
        OwnerEntity owner = ownerService.getOwner(ownerId);
        if (owner == null) {
            throw new EntityNotFoundException("Owner not found with ID: " + ownerId);
        }
        
        AdoptionTestEntity adoptionTestEntity = modelMapper.map(adoptionTestDTO, AdoptionTestEntity.class);
        adoptionTestEntity.setOwner(owner);
        adoptionTestEntity = adoptionTestService.createTest(adoptionTestEntity);
        if (adoptionTestEntity == null) {
            throw new IllegalOperationException("Failed to create adoption test.");
        }
        return modelMapper.map(adoptionTestEntity, AdoptionTestDTO.class);
    }

    /**
     * Assign an adoption test list to a specific owner.
     *
     * @param ownerId
     * @param listAdoptionTestDTO
     * @return List<AdoptionTestDetailDTO>
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @PutMapping("/{ownerId}/adoption-tests")
    @ResponseStatus(code = HttpStatus.OK)
    public List<AdoptionTestDetailDTO> update(@PathVariable Long ownerId,
            @RequestBody List<AdoptionTestDTO> listAdoptionTestDTO)
            throws EntityNotFoundException, IllegalOperationException {
        OwnerEntity owner = ownerService.getOwner(ownerId);
        if (owner == null) {
            throw new IllegalOperationException("Owner not found with ID: " + ownerId);
        }
        List<AdoptionTestEntity> adoptionTestEntities = modelMapper.map(listAdoptionTestDTO,
                new TypeToken<List<AdoptionTestEntity>>() {
                }.getType());
        adoptionTestEntities.forEach(test -> test.setOwner(owner));
        for (AdoptionTestEntity test : adoptionTestEntities) {
            try {
                test = adoptionTestService.updateTest(test.getId(), test);
            } catch (IllegalArgumentException e) {
                throw new EntityNotFoundException(
                        "Failed to update, not found adoption test with ID: " + test.getId());
            }
        }
        return findAll(ownerId);
    }

    /**
     * Delete an adoption test by ID for a specific owner.
     *
     * @param ownerId
     * @param adoptionTestId
     * @throws EntityNotFoundException
     */
    @DeleteMapping("/{ownerId}/adoption-tests/{adoptionTestId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long ownerId, @PathVariable Long adoptionTestId)
            throws EntityNotFoundException, IllegalOperationException {
        OwnerEntity owner = ownerService.getOwner(ownerId);
        if (owner == null) {
            throw new IllegalOperationException("Owner not found with ID: " + ownerId);
        }
        AdoptionTestEntity adoptionTest = adoptionTestService.getTestById(adoptionTestId);
        if (adoptionTest == null || !adoptionTest.getOwner().getId().equals(ownerId)) {
            throw new EntityNotFoundException("Adoption test not found with ID: " + adoptionTestId);
        }
        adoptionTestService.deleteTest(adoptionTestId);
    }
}
