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

import co.edu.udistrital.mdp.adopcion.dto.events.ShelterArrivalDTO;
import co.edu.udistrital.mdp.adopcion.dto.events.ShelterArrivalDetailDTO;
import co.edu.udistrital.mdp.adopcion.entities.events.ShelterArrivalEntity;
import co.edu.udistrital.mdp.adopcion.entities.person.VeterinarianEntity;
import co.edu.udistrital.mdp.adopcion.services.person.VeterinarianService;
import co.edu.udistrital.mdp.adopcion.services.events.ShelterArrivalService;

@RestController
@RequestMapping("/veterinarians")
public class VeterinarianShelterArrivalsController {

    @Autowired
    private VeterinarianService veterinarianService;

    @Autowired
    private ShelterArrivalService shelterArrivalService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     *
     * @param veterinarianId
     * @return List of ShelterArrivalDetailDTO
     * @throws IllegalOperationException
     * @throws EntityNotFoundException
     */
    @GetMapping("/{veterinarianId}/shelter-arrivals")
    @ResponseStatus(code = HttpStatus.OK)
    public List<ShelterArrivalDetailDTO> findAll(@PathVariable Long veterinarianId)
            throws IllegalOperationException, EntityNotFoundException {
        VeterinarianEntity veterinarian = veterinarianService.getVeterinarianById(veterinarianId);
        if (veterinarian == null) {
            throw new EntityNotFoundException("Veterinarian not found with ID: " + veterinarianId);
        }
        List<ShelterArrivalEntity> shelterArrivals = shelterArrivalService.getAllShelterArrivals();
        shelterArrivals.removeIf(arrival -> !arrival.getVeterinarian().getId().equals(veterinarianId));
        if (shelterArrivals.isEmpty()) {
            throw new IllegalOperationException("No shelter arrivals found.");
        }
        return modelMapper.map(shelterArrivals, new TypeToken<List<ShelterArrivalDetailDTO>>() {
        }.getType());
    }

    /**
     *
     * @param veterinarianId
     * @param shelterArrivalId
     * @return ShelterArrivalDetailDTO
     * @throws EntityNotFoundException
     */
    @GetMapping("/{veterinarianId}/shelter-arrivals/{shelterArrivalId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ShelterArrivalDetailDTO findOne(@PathVariable Long veterinarianId, @PathVariable Long shelterArrivalId)
            throws EntityNotFoundException, IllegalOperationException {
        VeterinarianEntity veterinarian = veterinarianService.getVeterinarianById(veterinarianId);
        if (veterinarian == null) {
            throw new IllegalOperationException("Veterinarian not found with ID: " + veterinarianId);
        }
        ShelterArrivalEntity shelterArrival = shelterArrivalService.getShelterArrivalById(shelterArrivalId);
        if (shelterArrival == null || !shelterArrival.getVeterinarian().getId().equals(veterinarianId)) {
            throw new EntityNotFoundException("Shelter arrival not found with ID: " + shelterArrivalId);
        }
        return modelMapper.map(shelterArrival, ShelterArrivalDetailDTO.class);
    }

    /**
     * 
     * @param veterinarianId
     * @param shelterArrivalId
     * @return ShelterArrivalDTO
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @PostMapping("/{veterinarianId}/shelter-arrivals/{shelterArrivalId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ShelterArrivalDTO create(@PathVariable Long veterinarianId, @PathVariable Long shelterArrivalId)
            throws EntityNotFoundException, IllegalOperationException {
        VeterinarianEntity veterinarian = veterinarianService.getVeterinarianById(veterinarianId);
        if (veterinarian == null) {
            throw new IllegalOperationException("Veterinarian not found with ID: " + veterinarianId);
        }
        ShelterArrivalEntity shelterArrivalEntity = shelterArrivalService.getShelterArrivalById(shelterArrivalId);
        if (shelterArrivalEntity == null) {
            throw new EntityNotFoundException("Shelter arrival not found with ID: " + shelterArrivalId);
        }
        shelterArrivalEntity.setVeterinarian(veterinarian);
        shelterArrivalEntity = shelterArrivalService.updateShelterArrival(shelterArrivalId, shelterArrivalEntity);
        if (shelterArrivalEntity == null) {
            throw new IllegalOperationException("Failed to assign shelter arrival to veterinarian.");
        }
        return modelMapper.map(shelterArrivalEntity, ShelterArrivalDTO.class);
    }

    /**
     *
     * @param veterinarianId
     * @param shelterArrivalDTO
     * @return ShelterArrivalDTO
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @PostMapping("/{veterinarianId}/shelter-arrivals")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ShelterArrivalDTO createNew(@PathVariable Long veterinarianId, @RequestBody ShelterArrivalDTO shelterArrivalDTO)
            throws EntityNotFoundException, IllegalOperationException {
        VeterinarianEntity veterinarian = veterinarianService.getVeterinarianById(veterinarianId);
        if (veterinarian == null) {
            throw new EntityNotFoundException("Veterinarian not found with ID: " + veterinarianId);
        }
        
        ShelterArrivalEntity shelterArrivalEntity = modelMapper.map(shelterArrivalDTO, ShelterArrivalEntity.class);
        shelterArrivalEntity.setVeterinarian(veterinarian);
        shelterArrivalEntity = shelterArrivalService.createShelterArrival(shelterArrivalEntity);
        if (shelterArrivalEntity == null) {
            throw new IllegalOperationException("Failed to create shelter arrival.");
        }
        return modelMapper.map(shelterArrivalEntity, ShelterArrivalDTO.class);
    }

    /**
     *
     * @param veterinarianId
     * @param listShelterArrivalDTO
     * @return List<ShelterArrivalDetailDTO>
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @PutMapping("/{veterinarianId}/shelter-arrivals")
    @ResponseStatus(code = HttpStatus.OK)
    public List<ShelterArrivalDetailDTO> update(@PathVariable Long veterinarianId,
            @RequestBody List<ShelterArrivalDTO> listShelterArrivalDTO)
            throws EntityNotFoundException, IllegalOperationException {
        VeterinarianEntity veterinarian = veterinarianService.getVeterinarianById(veterinarianId);
        if (veterinarian == null) {
            throw new IllegalOperationException("Veterinarian not found with ID: " + veterinarianId);
        }
        List<ShelterArrivalEntity> shelterArrivalEntities = modelMapper.map(listShelterArrivalDTO,
                new TypeToken<List<ShelterArrivalEntity>>() {
                }.getType());
        shelterArrivalEntities.forEach(arrival -> arrival.setVeterinarian(veterinarian));
        for (ShelterArrivalEntity arrival : shelterArrivalEntities) {
            try {
                arrival = shelterArrivalService.updateShelterArrival(arrival.getId(), arrival);
            } catch (IllegalArgumentException e) {
                throw new EntityNotFoundException(
                        "Failed to update, not found shelter arrival with ID: " + arrival.getId());
            }
        }
        return findAll(veterinarianId);
    }

    /**
     *
     * @param veterinarianId
     * @param shelterArrivalId
     * @throws EntityNotFoundException
     */
    @DeleteMapping("/{veterinarianId}/shelter-arrivals/{shelterArrivalId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long veterinarianId, @PathVariable Long shelterArrivalId)
            throws EntityNotFoundException, IllegalOperationException {
        VeterinarianEntity veterinarian = veterinarianService.getVeterinarianById(veterinarianId);
        if (veterinarian == null) {
            throw new IllegalOperationException("Veterinarian not found with ID: " + veterinarianId);
        }
        ShelterArrivalEntity shelterArrival = shelterArrivalService.getShelterArrivalById(shelterArrivalId);
        if (shelterArrival == null || !shelterArrival.getVeterinarian().getId().equals(veterinarianId)) {
            throw new EntityNotFoundException("Shelter arrival not found with ID: " + shelterArrivalId);
        }
        shelterArrivalService.deleteShelterArrival(shelterArrivalId);
    }
}