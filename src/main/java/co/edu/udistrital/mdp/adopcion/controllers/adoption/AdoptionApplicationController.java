package co.edu.udistrital.mdp.adopcion.controllers.adoption;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.adopcion.dto.adoption.AdoptionApplicationDTO;
import co.edu.udistrital.mdp.adopcion.dto.adoption.AdoptionApplicationDetailDTO;
import co.edu.udistrital.mdp.adopcion.entities.adoption.AdoptionApplicationEntity;
import co.edu.udistrital.mdp.adopcion.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.adopcion.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.adopcion.services.adoption.AdoptionApplicationService;

@RestController
@RequestMapping("/adoption-applications")
public class AdoptionApplicationController {

    @Autowired
    private AdoptionApplicationService applicationService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AdoptionApplicationDetailDTO> findAll() {
        List<AdoptionApplicationEntity> apps = applicationService.getAllApplications();
        return modelMapper.map(apps, new TypeToken<List<AdoptionApplicationDetailDTO>>() {}.getType());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AdoptionApplicationDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        AdoptionApplicationEntity entity = applicationService.getApplicationById(id);
        return modelMapper.map(entity, AdoptionApplicationDetailDTO.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdoptionApplicationDTO create(@RequestBody AdoptionApplicationDTO dto)
            throws IllegalOperationException, EntityNotFoundException {
        // Mapeo manual para evitar recursión y ambigüedad
        AdoptionApplicationEntity entity = new AdoptionApplicationEntity();
        entity.setApplicationDate(dto.getApplicationDate());
        entity.setApplicationEnd(dto.getApplicationEnd());
        entity.setObservations(dto.getObservations());
        entity.setApplicationStatus(dto.getApplicationStatus());
        entity.setResult(dto.getResult());
        // Asigna solo el id de las entidades relacionadas
        if (dto.getOwner() != null && dto.getOwner().getId() != null) {
            var owner = new co.edu.udistrital.mdp.adopcion.entities.person.OwnerEntity();
            owner.setId(dto.getOwner().getId());
            entity.setOwner(owner);
        }
        if (dto.getVeterinarian() != null && dto.getVeterinarian().getId() != null) {
            var vet = new co.edu.udistrital.mdp.adopcion.entities.person.VeterinarianEntity();
            vet.setId(dto.getVeterinarian().getId());
            entity.setVeterinarian(vet);
        }
        if (dto.getPet() != null && dto.getPet().getId() != null) {
            var pet = new co.edu.udistrital.mdp.adopcion.entities.pet.PetEntity();
            pet.setId(dto.getPet().getId());
            entity.setPet(pet);
        }
        // No se asigna la relación inversa (adoption) para evitar recursión
        AdoptionApplicationEntity saved = applicationService.createApplication(entity);
        return modelMapper.map(saved, AdoptionApplicationDTO.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AdoptionApplicationDTO update(@PathVariable Long id, @RequestBody AdoptionApplicationDTO dto)
            throws EntityNotFoundException, IllegalOperationException {
        AdoptionApplicationEntity entity = applicationService.updateApplication(id,
                modelMapper.map(dto, AdoptionApplicationEntity.class));
        return modelMapper.map(entity, AdoptionApplicationDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        applicationService.deleteApplication(id);
    }
}
