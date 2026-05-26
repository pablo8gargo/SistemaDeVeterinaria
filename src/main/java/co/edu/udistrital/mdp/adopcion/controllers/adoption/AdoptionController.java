package co.edu.udistrital.mdp.adopcion.controllers.adoption;

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
import co.edu.udistrital.mdp.adopcion.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.adopcion.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.adopcion.services.adoption.AdoptionService;

@RestController
@RequestMapping("/adoptions")
public class AdoptionController {

    @Autowired
    private AdoptionService adoptionService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<AdoptionDetailDTO> findAll() {
        List<AdoptionEntity> adoptions = adoptionService.getAllAdoptions();
        return modelMapper.map(adoptions, new TypeToken<List<AdoptionDetailDTO>>() {
        }.getType());
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public AdoptionDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        AdoptionEntity adoptionEntity = adoptionService.getAdoptionById(id);
        if (adoptionEntity == null) {
            throw new EntityNotFoundException("The adoption with the given id was not found");
        }
        return modelMapper.map(adoptionEntity, AdoptionDetailDTO.class);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public AdoptionDTO create(@RequestBody AdoptionDTO adoptionDTO) throws IllegalOperationException, EntityNotFoundException {
        // Mapeo manual para evitar recursión y ambigüedad
        AdoptionEntity adoptionEntity = new AdoptionEntity();
        adoptionEntity.setDescription(adoptionDTO.getDescription());
        adoptionEntity.setObservations(adoptionDTO.getObservations());
        adoptionEntity.setAdoptionStatus(adoptionDTO.getAdoptionStatus());
        if (adoptionDTO.getOwner() != null && adoptionDTO.getOwner().getId() != null) {
            var owner = new co.edu.udistrital.mdp.adopcion.entities.person.OwnerEntity();
            owner.setId(adoptionDTO.getOwner().getId());
            adoptionEntity.setOwner(owner);
        }
        if (adoptionDTO.getVeterinarian() != null && adoptionDTO.getVeterinarian().getId() != null) {
            var vet = new co.edu.udistrital.mdp.adopcion.entities.person.VeterinarianEntity();
            vet.setId(adoptionDTO.getVeterinarian().getId());
            adoptionEntity.setVeterinarian(vet);
        }
        if (adoptionDTO.getPet() != null && adoptionDTO.getPet().getId() != null) {
            var pet = new co.edu.udistrital.mdp.adopcion.entities.pet.PetEntity();
            pet.setId(adoptionDTO.getPet().getId());
            adoptionEntity.setPet(pet);
        }
        if (adoptionDTO.getAdoptionApplication() != null && adoptionDTO.getAdoptionApplication().getId() != null) {
            var app = new co.edu.udistrital.mdp.adopcion.entities.adoption.AdoptionApplicationEntity();
            app.setId(adoptionDTO.getAdoptionApplication().getId());
            adoptionEntity.setAdoptionApplication(app);
        }
        AdoptionEntity saved = adoptionService.createAdoption(adoptionEntity);
        return modelMapper.map(saved, AdoptionDTO.class);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public AdoptionDTO update(@PathVariable Long id, @RequestBody AdoptionDTO adoptionDTO)
            throws EntityNotFoundException, IllegalOperationException {
        // Mapeo manual para evitar recursión y ambigüedad
        AdoptionEntity adoptionEntity = new AdoptionEntity();
        adoptionEntity.setId(id);
        adoptionEntity.setDescription(adoptionDTO.getDescription());
        adoptionEntity.setObservations(adoptionDTO.getObservations());
        adoptionEntity.setAdoptionStatus(adoptionDTO.getAdoptionStatus());
        if (adoptionDTO.getOwner() != null && adoptionDTO.getOwner().getId() != null) {
            var owner = new co.edu.udistrital.mdp.adopcion.entities.person.OwnerEntity();
            owner.setId(adoptionDTO.getOwner().getId());
            adoptionEntity.setOwner(owner);
        }
        if (adoptionDTO.getVeterinarian() != null && adoptionDTO.getVeterinarian().getId() != null) {
            var vet = new co.edu.udistrital.mdp.adopcion.entities.person.VeterinarianEntity();
            vet.setId(adoptionDTO.getVeterinarian().getId());
            adoptionEntity.setVeterinarian(vet);
        }
        if (adoptionDTO.getPet() != null && adoptionDTO.getPet().getId() != null) {
            var pet = new co.edu.udistrital.mdp.adopcion.entities.pet.PetEntity();
            pet.setId(adoptionDTO.getPet().getId());
            adoptionEntity.setPet(pet);
        }
        if (adoptionDTO.getAdoptionApplication() != null && adoptionDTO.getAdoptionApplication().getId() != null) {
            var app = new co.edu.udistrital.mdp.adopcion.entities.adoption.AdoptionApplicationEntity();
            app.setId(adoptionDTO.getAdoptionApplication().getId());
            adoptionEntity.setAdoptionApplication(app);
        }
        AdoptionEntity updated = null;
        try {
            updated = adoptionService.updateAdoption(id, adoptionEntity);
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("The adoption with the given id was not found");
        }
        if (updated == null) {
            throw new EntityNotFoundException("The adoption with the given id was not found");
        }
        return modelMapper.map(updated, AdoptionDTO.class);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        if (adoptionService.getAdoptionById(id) == null) {
            throw new EntityNotFoundException("The adoption with the given id was not found");
        }
        adoptionService.deleteAdoption(id);
    }
}