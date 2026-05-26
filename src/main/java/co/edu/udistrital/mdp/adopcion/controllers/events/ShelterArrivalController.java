package co.edu.udistrital.mdp.adopcion.controllers.events;

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

import co.edu.udistrital.mdp.adopcion.dto.events.ShelterArrivalDTO;
import co.edu.udistrital.mdp.adopcion.dto.events.ShelterArrivalDetailDTO;
import co.edu.udistrital.mdp.adopcion.entities.events.ShelterArrivalEntity;
import co.edu.udistrital.mdp.adopcion.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.adopcion.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.adopcion.services.events.ShelterArrivalService;


@RestController
@RequestMapping("/shelter-arrivals")
public class ShelterArrivalController {
    @Autowired
    private ShelterArrivalService shelterArrivalService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Get all shelter arrivals.
     * 
     * @return
     */
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<ShelterArrivalDetailDTO> findAll() throws IllegalOperationException {
        List<ShelterArrivalEntity> shelterArrivals = shelterArrivalService.getAllShelterArrivals();
        if (shelterArrivals.isEmpty()) {
            throw new IllegalOperationException("No shelter arrivals found.");
        }
        return modelMapper.map(shelterArrivals, new TypeToken<List<ShelterArrivalDetailDTO>>() {
        }.getType());
    }

    /**
     * Get shelter arrival by ID.
     * 
     * @param shelter_arrival_id
     * @return
     * @throws EntityNotFoundException
     */
    @GetMapping(value = "/{shelter_arrival_id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ShelterArrivalDetailDTO findOne(@PathVariable Long shelter_arrival_id) throws EntityNotFoundException {
        ShelterArrivalEntity shelterArrivalEntity = shelterArrivalService.getShelterArrivalById(shelter_arrival_id);
        if (shelterArrivalEntity == null) {
            throw new EntityNotFoundException("Shelter arrival not found with ID: " + shelter_arrival_id);
        }
        return modelMapper.map(shelterArrivalEntity, ShelterArrivalDetailDTO.class);
    }

    /**
     * Create a new shelter arrival.
     *
     * @param shelterArrivalDTO
     * @return
     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ShelterArrivalDTO create(@RequestBody ShelterArrivalDTO shelterArrivalDTO) throws EntityNotFoundException, IllegalOperationException {
        ShelterArrivalEntity shelterArrivalEntity = shelterArrivalService.createShelterArrival(modelMapper.map(shelterArrivalDTO, ShelterArrivalEntity.class));
        return modelMapper.map(shelterArrivalEntity, ShelterArrivalDTO.class);
    }

    /**
     * Update an existing shelter arrival.
     *
     * @param shelter_arrival_id
     * @param shelterArrivalDTO
     * @return
     */
    @PutMapping(value = "/{shelter_arrival_id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ShelterArrivalDTO update(@PathVariable Long shelter_arrival_id, @RequestBody ShelterArrivalDTO shelterArrivalDTO) 
            throws EntityNotFoundException, IllegalOperationException {
        ShelterArrivalEntity shelterArrivalEntity = shelterArrivalService.updateShelterArrival(shelter_arrival_id, modelMapper.map(shelterArrivalDTO, ShelterArrivalEntity.class));
        if (shelterArrivalEntity == null) {
            throw new EntityNotFoundException("Shelter arrival not found with ID: " + shelter_arrival_id);
        }
        return modelMapper.map(shelterArrivalEntity, ShelterArrivalDTO.class);
    }

    /**
     * Delete a shelter arrival by ID.
     *
     * @param shelter_arrival_id
     */
    @DeleteMapping(value = "/{shelter_arrival_id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long shelter_arrival_id) throws EntityNotFoundException, IllegalOperationException {
        if (shelterArrivalService.getShelterArrivalById(shelter_arrival_id) == null) {
            throw new EntityNotFoundException("Shelter arrival not found with ID: " + shelter_arrival_id);
        }
        shelterArrivalService.deleteShelterArrival(shelter_arrival_id);
    }

}