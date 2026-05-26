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

import co.edu.udistrital.mdp.adopcion.dto.adoption.AdoptionTestDTO;
import co.edu.udistrital.mdp.adopcion.dto.adoption.AdoptionTestDetailDTO;
import co.edu.udistrital.mdp.adopcion.entities.person.VeterinarianEntity;
import co.edu.udistrital.mdp.adopcion.entities.adoption.AdoptionTestEntity;
import co.edu.udistrital.mdp.adopcion.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.adopcion.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.adopcion.services.person.VeterinarianService;
import co.edu.udistrital.mdp.adopcion.services.adoption.AdoptionTestService;

@RestController
@RequestMapping("/veterinarians")
public class VeterinarianAdoptionTestController {

    @Autowired
    private VeterinarianService veterinarianService;
    
    @Autowired
    private AdoptionTestService adoptionTestService;
    
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Get all adoption tests for a specific veterinarian.
     *
     * @param veterinarianId
     * @return
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @GetMapping("/{veterinarianId}/adoption-tests")
    @ResponseStatus(code = HttpStatus.OK)
    public List<AdoptionTestDetailDTO> findAll(@PathVariable Long veterinarianId)
            throws EntityNotFoundException, IllegalOperationException {
        VeterinarianEntity veterinarian = veterinarianService.getVeterinarianById(veterinarianId);
        if (veterinarian == null) {
            throw new EntityNotFoundException("Veterinarian not found with ID: " + veterinarianId);
        }
        List<AdoptionTestEntity> adoptionTests = adoptionTestService.getAllTests();
        adoptionTests.removeIf(adoptionTest -> !adoptionTest.getVeterinarian().getId().equals(veterinarianId));
        if (adoptionTests.isEmpty()) {
            throw new IllegalOperationException("No adoption tests found for veterinarian with ID: " + veterinarianId);
        }
        return modelMapper.map(adoptionTests, new TypeToken<List<AdoptionTestDetailDTO>>() {
        }.getType());
    }

    /**
     * Get adoption test by ID for a specific veterinarian.
     *
     * @param veterinarianId
     * @param adoptionTestId
     * @return
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @GetMapping("/{veterinarianId}/adoption-tests/{adoptionTestId}")
    @ResponseStatus(code = HttpStatus.OK)
    public AdoptionTestDetailDTO findOne(@PathVariable Long veterinarianId, @PathVariable Long adoptionTestId)
            throws EntityNotFoundException, IllegalOperationException {
        VeterinarianEntity veterinarian = veterinarianService.getVeterinarianById(veterinarianId);
        if (veterinarian == null) {
            throw new EntityNotFoundException("Veterinarian not found with ID: " + veterinarianId);
        }
        AdoptionTestEntity adoptionTestEntity = adoptionTestService.getTestById(adoptionTestId);
        if (adoptionTestEntity == null || !adoptionTestEntity.getVeterinarian().getId().equals(veterinarianId)) {
            throw new IllegalOperationException(
                    "For this veterinarian, adoption test not found with ID: " + adoptionTestId);
        }
        return modelMapper.map(adoptionTestEntity, AdoptionTestDetailDTO.class);
    }

    /**
     * Update the list of adoption tests for a specific veterinarian.
     *
     * @param veterinarianId
     * @param listAdoptionTestDTO
     * @return
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @PutMapping("/{veterinarianId}/adoption-tests")
    @ResponseStatus(code = HttpStatus.OK)
    public List<AdoptionTestDetailDTO> replaceAdoptionTests(@RequestBody List<AdoptionTestDTO> listAdoptionTestDTO,
            @PathVariable Long veterinarianId)
            throws EntityNotFoundException, IllegalOperationException {
        VeterinarianEntity veterinarian = veterinarianService.getVeterinarianById(veterinarianId);
        if (veterinarian == null) {
            throw new EntityNotFoundException("Veterinarian not found with ID: " + veterinarianId);
        }
        List<AdoptionTestEntity> adoptionTestEntities = modelMapper.map(listAdoptionTestDTO,
                new TypeToken<List<AdoptionTestEntity>>() {
                }.getType());
        adoptionTestEntities.forEach(adoptionTest -> adoptionTest.setVeterinarian(veterinarian));
        for (AdoptionTestEntity adoptionTest : adoptionTestEntities) {
            try {
                adoptionTest = adoptionTestService.updateTest(adoptionTest.getId(), adoptionTest);
            } catch (IllegalArgumentException e) {
                throw new EntityNotFoundException(
                        "Failed to update, not found adoption test with ID: " + adoptionTest.getId());
            }
        }
        adoptionTestEntities = adoptionTestService.getAllTests();
        return modelMapper.map(adoptionTestEntities, new TypeToken<List<AdoptionTestDetailDTO>>() {
        }.getType());
    }

    /**
     * Associate an existing adoption test with a specific veterinarian.
     *
     * @param veterinarianId
     * @param adoptionTestId
     * @return
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @PostMapping("/{veterinarianId}/adoption-tests/{adoptionTestId}")
    @ResponseStatus(code = HttpStatus.OK)
    public AdoptionTestDetailDTO addAdoptionTest(@PathVariable Long veterinarianId, @PathVariable Long adoptionTestId)
            throws EntityNotFoundException, IllegalOperationException {
        VeterinarianEntity veterinarian = veterinarianService.getVeterinarianById(veterinarianId);
        if (veterinarian == null) {
            throw new EntityNotFoundException("Veterinarian not found with ID: " + veterinarianId);
        }
        AdoptionTestEntity adoptionTestEntity = adoptionTestService.getTestById(adoptionTestId);
        if (adoptionTestEntity == null || !adoptionTestEntity.getVeterinarian().getId().equals(veterinarianId)) {
            throw new EntityNotFoundException("Adoption test not found with ID: " + adoptionTestId);
        }
        adoptionTestEntity.setVeterinarian(veterinarian);
        adoptionTestEntity = adoptionTestService.updateTest(adoptionTestId, adoptionTestEntity);
        if (adoptionTestEntity == null) {
            throw new IllegalOperationException(
                    "Failed to update, not found adoption test with ID: " + adoptionTestId);
        }
        return modelMapper.map(adoptionTestEntity, AdoptionTestDetailDTO.class);
    }

    /**
     * Delete an adoption test by ID for a specific veterinarian.
     *
     * @param veterinarianId
     * @param adoptionTestId
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @DeleteMapping("/{veterinarianId}/adoption-tests/{adoptionTestId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long veterinarianId, @PathVariable Long adoptionTestId)
            throws EntityNotFoundException, IllegalOperationException {
        VeterinarianEntity veterinarian = veterinarianService.getVeterinarianById(veterinarianId);
        if (veterinarian == null) {
            throw new EntityNotFoundException("Veterinarian not found with ID: " + veterinarianId);
        }
        AdoptionTestEntity adoptionTestEntity = adoptionTestService.getTestById(adoptionTestId);
        if (adoptionTestEntity == null || !adoptionTestEntity.getVeterinarian().getId().equals(veterinarianId)) {
            throw new IllegalOperationException(
                    "For this veterinarian, adoption test not found with ID: " + adoptionTestId);
        }
        adoptionTestService.deleteTest(adoptionTestId);
    }
}