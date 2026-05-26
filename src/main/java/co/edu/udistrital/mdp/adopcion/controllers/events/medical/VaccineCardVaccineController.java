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

import co.edu.udistrital.mdp.adopcion.dto.events.medical.VaccineDTO;
import co.edu.udistrital.mdp.adopcion.dto.events.medical.VaccineDetailDTO;
import co.edu.udistrital.mdp.adopcion.entities.events.medical.VaccineCardEntity;
import co.edu.udistrital.mdp.adopcion.entities.events.medical.VaccineEntity;
import co.edu.udistrital.mdp.adopcion.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.adopcion.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.adopcion.services.events.medical.VaccineCardService;
import co.edu.udistrital.mdp.adopcion.services.events.medical.VaccineService;

@RestController
@RequestMapping("/vaccine-cards")
public class VaccineCardVaccineController {

    @Autowired
    private VaccineCardService vaccineCardService;
    
    @Autowired
    private VaccineService vaccineService;
    
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Get all vaccines for a specific vaccine card.
     * 
     * @param vaccineCardId
     * @return
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @GetMapping("/{vaccineCardId}/vaccines")
    @ResponseStatus(code = HttpStatus.OK)
    public List<VaccineDetailDTO> findAll(@PathVariable Long vaccineCardId)
            throws EntityNotFoundException, IllegalOperationException {
        VaccineCardEntity vaccineCard = vaccineCardService.getVaccineCardById(vaccineCardId);
        if (vaccineCard == null) {
            throw new EntityNotFoundException("Vaccine card not found with ID: " + vaccineCardId);
        }
        List<VaccineEntity> vaccines = vaccineService.getAllVaccines();
        vaccines.removeIf(vaccine -> !vaccine.getVaccineCard().getId().equals(vaccineCardId));
        if (vaccines.isEmpty()) {
            throw new IllegalOperationException("No vaccines found for vaccine card with ID: " + vaccineCardId);
        }
        return modelMapper.map(vaccines, new TypeToken<List<VaccineDetailDTO>>() {
        }.getType());
    }

    /**
     * Get vaccine by ID for a specific vaccine card.
     * 
     * @param vaccineCardId
     * @param vaccineId
     * @return
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @GetMapping("/{vaccineCardId}/vaccines/{vaccineId}")
    @ResponseStatus(code = HttpStatus.OK)
    public VaccineDetailDTO findOne(@PathVariable Long vaccineCardId, @PathVariable Long vaccineId)
            throws EntityNotFoundException, IllegalOperationException {
        VaccineCardEntity vaccineCard = vaccineCardService.getVaccineCardById(vaccineCardId);
        if (vaccineCard == null) {
            throw new EntityNotFoundException("Vaccine card not found with ID: " + vaccineCardId);
        }
        VaccineEntity vaccineEntity = vaccineService.getVaccineById(vaccineId);
        if (vaccineEntity == null || !vaccineEntity.getVaccineCard().getId().equals(vaccineCardId)) {
            throw new IllegalOperationException(
                    "In this vaccine card, vaccine not found with ID: " + vaccineId);
        }
        return modelMapper.map(vaccineEntity, VaccineDetailDTO.class);
    }

    /**
     * Update the list of vaccines for a specific vaccine card.
     * 
     * @param vaccineCardId
     * @param listVaccineDTO
     * @return
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @PutMapping("/{vaccineCardId}/vaccines")
    @ResponseStatus(code = HttpStatus.OK)
    public List<VaccineDetailDTO> replaceVaccines(@RequestBody List<VaccineDTO> listVaccineDTO,
            @PathVariable Long vaccineCardId)
            throws EntityNotFoundException, IllegalOperationException {
        VaccineCardEntity vaccineCard = vaccineCardService.getVaccineCardById(vaccineCardId);
        if (vaccineCard == null) {
            throw new EntityNotFoundException("Vaccine card not found with ID: " + vaccineCardId);
        }
        List<VaccineEntity> vaccineEntities = modelMapper.map(listVaccineDTO,
                new TypeToken<List<VaccineEntity>>() {
                }.getType());
        vaccineEntities.forEach(vaccine -> vaccine.setVaccineCard(vaccineCard));
        for (VaccineEntity vaccine : vaccineEntities) {
            try {
                vaccine = vaccineService.updateVaccine(vaccine.getId(), vaccine);
            } catch (IllegalArgumentException e) {
                throw new EntityNotFoundException(
                        "Failed to update, not found vaccine with ID: " + vaccine.getId());
            }
        }
        vaccineEntities = vaccineService.getAllVaccines();
        return modelMapper.map(vaccineEntities, new TypeToken<List<VaccineDetailDTO>>() {
        }.getType());
    }

    /**
     * Associate an existing vaccine with a specific vaccine card.
     * 
     * @param vaccineCardId
     * @param vaccineId
     * @return
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @PostMapping("/{vaccineCardId}/vaccines/{vaccineId}")
    @ResponseStatus(code = HttpStatus.OK)
    public VaccineDetailDTO addVaccine(@PathVariable Long vaccineCardId, @PathVariable Long vaccineId)
            throws EntityNotFoundException, IllegalOperationException {
        VaccineCardEntity vaccineCard = vaccineCardService.getVaccineCardById(vaccineCardId);
        if (vaccineCard == null) {
            throw new EntityNotFoundException("Vaccine card not found with ID: " + vaccineCardId);
        }
        VaccineEntity vaccineEntity = vaccineService.getVaccineById(vaccineId);
        if (vaccineEntity == null || !vaccineEntity.getVaccineCard().getId().equals(vaccineCardId)) {
            throw new EntityNotFoundException("Vaccine not found with ID: " + vaccineId);
        }
        vaccineEntity.setVaccineCard(vaccineCard);
        vaccineEntity = vaccineService.updateVaccine(vaccineId, vaccineEntity);
        if (vaccineEntity == null) {
            throw new IllegalOperationException(
                    "Failed to update, not found vaccine with ID: " + vaccineId);
        }
        return modelMapper.map(vaccineEntity, VaccineDetailDTO.class);
    }

    /**
     * Delete a vaccine by ID for a specific vaccine card.
     * 
     * @param vaccineCardId
     * @param vaccineId
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @DeleteMapping("/{vaccineCardId}/vaccines/{vaccineId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long vaccineCardId, @PathVariable Long vaccineId)
            throws EntityNotFoundException, IllegalOperationException {
        VaccineCardEntity vaccineCard = vaccineCardService.getVaccineCardById(vaccineCardId);
        if (vaccineCard == null) {
            throw new EntityNotFoundException("Vaccine card not found with ID: " + vaccineCardId);
        }
        VaccineEntity vaccineEntity = vaccineService.getVaccineById(vaccineId);
        if (vaccineEntity == null || !vaccineEntity.getVaccineCard().getId().equals(vaccineCardId)) {
            throw new IllegalOperationException(
                    "In this vaccine card, vaccine not found with ID: " + vaccineId);
        }
        vaccineService.deleteVaccine(vaccineId);
    }
}