package co.edu.udistrital.mdp.adopcion.controllers.events.medical;

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

import co.edu.udistrital.mdp.adopcion.dto.events.medical.DewormingDTO;
import co.edu.udistrital.mdp.adopcion.dto.events.medical.DewormingDetailDTO;
import co.edu.udistrital.mdp.adopcion.entities.events.medical.VaccineCardEntity;
import co.edu.udistrital.mdp.adopcion.entities.events.medical.DewormingEntity;
import co.edu.udistrital.mdp.adopcion.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.adopcion.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.adopcion.services.events.medical.VaccineCardService;
import co.edu.udistrital.mdp.adopcion.services.events.medical.DewormingService;

@RestController
@RequestMapping("/vaccine-cards")
public class VaccineCardDewormingController {

    @Autowired
    private VaccineCardService vaccineCardService;
    
    @Autowired
    private DewormingService dewormingService;
    
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Get all dewormings for a specific vaccine card.
     * 
     * @param vaccineCardId
     * @return
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @GetMapping("/{vaccineCardId}/dewormings")
    @ResponseStatus(code = HttpStatus.OK)
    public List<DewormingDetailDTO> findAll(@PathVariable Long vaccineCardId)
            throws EntityNotFoundException, IllegalOperationException {
        VaccineCardEntity vaccineCard = vaccineCardService.getVaccineCardById(vaccineCardId);
        if (vaccineCard == null) {
            throw new EntityNotFoundException("Vaccine card not found with ID: " + vaccineCardId);
        }
        List<DewormingEntity> dewormings = dewormingService.getAllDewormings();
        dewormings.removeIf(deworming -> !deworming.getVaccineCard().getId().equals(vaccineCardId));
        if (dewormings.isEmpty()) {
            throw new IllegalOperationException("No dewormings found for vaccine card with ID: " + vaccineCardId);
        }
        return modelMapper.map(dewormings, new TypeToken<List<DewormingDetailDTO>>() {
        }.getType());
    }

    /**
     * Get deworming by ID for a specific vaccine card.
     * 
     * @param vaccineCardId
     * @param dewormingId
     * @return
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @GetMapping("/{vaccineCardId}/dewormings/{dewormingId}")
    @ResponseStatus(code = HttpStatus.OK)
    public DewormingDetailDTO findOne(@PathVariable Long vaccineCardId, @PathVariable Long dewormingId)
            throws EntityNotFoundException, IllegalOperationException {
        VaccineCardEntity vaccineCard = vaccineCardService.getVaccineCardById(vaccineCardId);
        if (vaccineCard == null) {
            throw new EntityNotFoundException("Vaccine card not found with ID: " + vaccineCardId);
        }
        DewormingEntity dewormingEntity = dewormingService.getDewormingById(dewormingId);
        if (dewormingEntity == null || !dewormingEntity.getVaccineCard().getId().equals(vaccineCardId)) {
            throw new IllegalOperationException(
                    "In this vaccine card, deworming not found with ID: " + dewormingId);
        }
        return modelMapper.map(dewormingEntity, DewormingDetailDTO.class);
    }

    /**
     * Update the list of dewormings for a specific vaccine card.
     * 
     * @param vaccineCardId
     * @param listDewormingDTO
     * @return
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @PutMapping("/{vaccineCardId}/dewormings")
    @ResponseStatus(code = HttpStatus.OK)
    public List<DewormingDetailDTO> replaceDewormings(@RequestBody List<DewormingDTO> listDewormingDTO,
            @PathVariable Long vaccineCardId)
            throws EntityNotFoundException, IllegalOperationException {
        VaccineCardEntity vaccineCard = vaccineCardService.getVaccineCardById(vaccineCardId);
        if (vaccineCard == null) {
            throw new EntityNotFoundException("Vaccine card not found with ID: " + vaccineCardId);
        }
        List<DewormingEntity> dewormingEntities = modelMapper.map(listDewormingDTO,
                new TypeToken<List<DewormingEntity>>() {
                }.getType());
        dewormingEntities.forEach(deworming -> deworming.setVaccineCard(vaccineCard));
        for (DewormingEntity deworming : dewormingEntities) {
            try {
                deworming = dewormingService.updateDeworming(deworming.getId(), deworming);
            } catch (IllegalArgumentException e) {
                throw new EntityNotFoundException(
                        "Failed to update, not found deworming with ID: " + deworming.getId());
            }
        }
        dewormingEntities = dewormingService.getAllDewormings();
        return modelMapper.map(dewormingEntities, new TypeToken<List<DewormingDetailDTO>>() {
        }.getType());
    }

    /**
     * Associate an existing deworming with a specific vaccine card.
     * 
     * @param vaccineCardId
     * @param dewormingId
     * @return
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @PostMapping("/{vaccineCardId}/dewormings/{dewormingId}")
    @ResponseStatus(code = HttpStatus.OK)
    public DewormingDetailDTO addDeworming(@PathVariable Long vaccineCardId, @PathVariable Long dewormingId)
            throws EntityNotFoundException, IllegalOperationException {
        VaccineCardEntity vaccineCard = vaccineCardService.getVaccineCardById(vaccineCardId);
        if (vaccineCard == null) {
            throw new EntityNotFoundException("Vaccine card not found with ID: " + vaccineCardId);
        }
        DewormingEntity dewormingEntity = dewormingService.getDewormingById(dewormingId);
        if (dewormingEntity == null || !dewormingEntity.getVaccineCard().getId().equals(vaccineCardId)) {
            throw new EntityNotFoundException("Deworming not found with ID: " + dewormingId);
        }
        dewormingEntity.setVaccineCard(vaccineCard);
        dewormingEntity = dewormingService.updateDeworming(dewormingId, dewormingEntity);
        if (dewormingEntity == null) {
            throw new IllegalOperationException(
                    "Failed to update, not found deworming with ID: " + dewormingId);
        }
        return modelMapper.map(dewormingEntity, DewormingDetailDTO.class);
    }

    /**
     * Delete a deworming by ID for a specific vaccine card.
     * 
     * @param vaccineCardId
     * @param dewormingId
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @DeleteMapping("/{vaccineCardId}/dewormings/{dewormingId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long vaccineCardId, @PathVariable Long dewormingId)
            throws EntityNotFoundException, IllegalOperationException {
        VaccineCardEntity vaccineCard = vaccineCardService.getVaccineCardById(vaccineCardId);
        if (vaccineCard == null) {
            throw new EntityNotFoundException("Vaccine card not found with ID: " + vaccineCardId);
        }
        DewormingEntity dewormingEntity = dewormingService.getDewormingById(dewormingId);
        if (dewormingEntity == null || !dewormingEntity.getVaccineCard().getId().equals(vaccineCardId)) {
            throw new IllegalOperationException(
                    "In this vaccine card, deworming not found with ID: " + dewormingId);
        }
        dewormingService.deleteDeworming(dewormingId);
    }
}