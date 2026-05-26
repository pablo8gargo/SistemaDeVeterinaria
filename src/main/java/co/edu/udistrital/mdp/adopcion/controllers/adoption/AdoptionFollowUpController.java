package co.edu.udistrital.mdp.adopcion.controllers.adoption;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.adopcion.dto.adoption.AdoptionFollowUpDTO;
import co.edu.udistrital.mdp.adopcion.dto.adoption.AdoptionFollowUpDetailDTO;
import co.edu.udistrital.mdp.adopcion.entities.adoption.AdoptionFollowUpEntity;
import co.edu.udistrital.mdp.adopcion.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.adopcion.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.adopcion.services.adoption.AdoptionFollowUpService;

@RestController
@RequestMapping("/adoption-follow-ups")
public class AdoptionFollowUpController {

    @Autowired
    private AdoptionFollowUpService followUpService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AdoptionFollowUpDetailDTO> findAll() {
        List<AdoptionFollowUpEntity> entities = followUpService.getAllFollowUps();
        return modelMapper.map(entities, new TypeToken<List<AdoptionFollowUpDetailDTO>>() {}.getType());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AdoptionFollowUpDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        AdoptionFollowUpEntity entity = followUpService.getFollowUpById(id);
        return modelMapper.map(entity, AdoptionFollowUpDetailDTO.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdoptionFollowUpDTO create(@RequestBody AdoptionFollowUpDTO dto)
            throws IllegalOperationException, EntityNotFoundException {
        AdoptionFollowUpEntity entity = followUpService.createFollowUp(
                modelMapper.map(dto, AdoptionFollowUpEntity.class));
        return modelMapper.map(entity, AdoptionFollowUpDTO.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AdoptionFollowUpDTO update(@PathVariable Long id, @RequestBody AdoptionFollowUpDTO dto)
            throws EntityNotFoundException, IllegalOperationException {
        AdoptionFollowUpEntity entity = followUpService.updateFollowUp(id,
                modelMapper.map(dto, AdoptionFollowUpEntity.class));
        return modelMapper.map(entity, AdoptionFollowUpDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        followUpService.deleteFollowUp(id);
    }
}
