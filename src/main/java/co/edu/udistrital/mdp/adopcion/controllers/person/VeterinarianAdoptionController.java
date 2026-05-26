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

import co.edu.udistrital.mdp.adopcion.dto.adoption.AdoptionDTO;
import co.edu.udistrital.mdp.adopcion.dto.adoption.AdoptionDetailDTO;
import co.edu.udistrital.mdp.adopcion.entities.adoption.AdoptionEntity;
import co.edu.udistrital.mdp.adopcion.entities.person.VeterinarianEntity;
import co.edu.udistrital.mdp.adopcion.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.adopcion.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.adopcion.services.adoption.AdoptionService;
import co.edu.udistrital.mdp.adopcion.services.person.VeterinarianService;

@RestController
@RequestMapping("/veterinarians")
public class VeterinarianAdoptionController {
    @Autowired
    private VeterinarianService veterinarianService;
    @Autowired
    private AdoptionService adoptionService;
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Get all adoptions for a specific veterinarian.
     *
     * @param veterinarianId
     * @return
     * @throws EntityNotFoundException
     */
    @GetMapping("/{veterinarianId}/adoption")
    @ResponseStatus(code = HttpStatus.OK)
    public List<AdoptionDetailDTO> findAll(@PathVariable Long veterinarianId)
            throws EntityNotFoundException, IllegalOperationException {
        VeterinarianEntity veterinarian = veterinarianService.getVeterinarianById(veterinarianId);
        if (veterinarian == null) {
            throw new EntityNotFoundException("Veterinarian not found with ID: " + veterinarianId);
        }
        List<AdoptionEntity> adoptions = adoptionService.getAllAdoptions();
        adoptions.removeIf(adoption -> !adoption.getVeterinarian().getId().equals(veterinarianId));
        if (adoptions.isEmpty()) {
            throw new IllegalOperationException("No adoptions found for veterinarian with ID: " + veterinarianId);
        }
        return modelMapper.map(adoptions, new TypeToken<List<AdoptionDetailDTO>>() {
        }.getType());
    }

    /**
     * Get adoption by ID for a specific veterinarian.
     *
     * @param veterinarianId
     * @param adoptionId
     * @return
     * @throws EntityNotFoundException
     */
    @GetMapping("/{veterinarianId}/adoption/{adoptionId}")
    @ResponseStatus(code = HttpStatus.OK)
    public AdoptionDetailDTO findOne(@PathVariable Long veterinarianId, @PathVariable Long adoptionId)
            throws EntityNotFoundException, IllegalOperationException {
        VeterinarianEntity veterinarian = veterinarianService.getVeterinarianById(veterinarianId);
        if (veterinarian == null) {
            throw new EntityNotFoundException("Veterinarian not found with ID: " + veterinarianId);
        }
        AdoptionEntity adoptionEntity = adoptionService.getAdoptionById(adoptionId);
        if (adoptionEntity == null || !adoptionEntity.getVeterinarian().getId().equals(veterinarianId)) {
            throw new IllegalOperationException(
                    "In this veterinarian, adoption not found with ID: " + adoptionId);
        }
        return modelMapper.map(adoptionEntity, AdoptionDetailDTO.class);
    }

    /**
     * Replace all adoptions for a specific veterinarian.
     *
     * @param veterinarianId
     * @param listAdoptionDTO
     * @return
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @PutMapping("/{veterinarianId}/adoption")
    @ResponseStatus(code = HttpStatus.OK)
    public List<AdoptionDetailDTO> replaceAdoptions(@RequestBody List<AdoptionDTO> listAdoptionDTO,
            @PathVariable Long veterinarianId)
            throws EntityNotFoundException, IllegalOperationException {
        VeterinarianEntity veterinarian = veterinarianService.getVeterinarianById(veterinarianId);
        if (veterinarian == null) {
            throw new EntityNotFoundException("Veterinarian not found with ID: " + veterinarianId);
        }
        List<AdoptionEntity> adoptionEntities = modelMapper.map(listAdoptionDTO,
                new TypeToken<List<AdoptionEntity>>() {
                }.getType());
        adoptionEntities.forEach(adoption -> adoption.setVeterinarian(veterinarian));
        for (AdoptionEntity adoption : adoptionEntities) {
            try {
                adoption = adoptionService.updateAdoption(adoption.getId(), adoption);
            } catch (IllegalArgumentException e) {
                throw new EntityNotFoundException(
                        "Failed to update, not found adoption with ID: " + adoption.getId());
            }
        }
        adoptionEntities = adoptionService.getAllAdoptions();
        return modelMapper.map(adoptionEntities, new TypeToken<List<AdoptionDetailDTO>>() {
        }.getType());
    }

    /**
     * Add an existing adoption to a specific veterinarian.
     *
     * @param veterinarianId
     * @param adoptionId
     * @return
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @PostMapping("/{veterinarianId}/adoption/{adoptionId}")
    @ResponseStatus(code = HttpStatus.OK)
    public AdoptionDetailDTO addAdoption(@PathVariable Long veterinarianId, @PathVariable Long adoptionId)
            throws EntityNotFoundException, IllegalOperationException {
        VeterinarianEntity veterinarian = veterinarianService.getVeterinarianById(veterinarianId);
        if (veterinarian == null) {
            throw new EntityNotFoundException("Veterinarian not found with ID: " + veterinarianId);
        }
        AdoptionEntity adoptionEntity = adoptionService.getAdoptionById(adoptionId);
        if (adoptionEntity == null || !adoptionEntity.getVeterinarian().getId().equals(veterinarianId)) {
            throw new EntityNotFoundException("Adoption not found with ID: " + adoptionId);
        }
        adoptionEntity.setVeterinarian(veterinarian);
        adoptionEntity = adoptionService.updateAdoption(adoptionId, adoptionEntity);
        if (adoptionEntity == null) {
            throw new IllegalOperationException(
                    "Failed to update, not found adoption with ID: " + adoptionId);
        }
        return modelMapper.map(adoptionEntity, AdoptionDetailDTO.class);
    }

    /**
     * Delete an adoption by ID for a specific veterinarian.
     *
     * @param veterinarianId
     * @param adoptionId
     * @throws EntityNotFoundException
     */
    @DeleteMapping("/{veterinarianId}/adoption/{adoptionId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long veterinarianId, @PathVariable Long adoptionId)
            throws EntityNotFoundException, IllegalOperationException {
        VeterinarianEntity veterinarian = veterinarianService.getVeterinarianById(veterinarianId);
        if (veterinarian == null) {
            throw new EntityNotFoundException("Veterinarian not found with ID: " + veterinarianId);
        }
        AdoptionEntity adoptionEntity = adoptionService.getAdoptionById(adoptionId);
        if (adoptionEntity == null || !adoptionEntity.getVeterinarian().getId().equals(veterinarianId)) {
            throw new IllegalOperationException(
                    "In this veterinarian, adoption not found with ID: " + adoptionId);
        }
        adoptionService.deleteAdoption(adoptionId);
    }
}