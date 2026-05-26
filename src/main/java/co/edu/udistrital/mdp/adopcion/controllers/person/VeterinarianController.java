package co.edu.udistrital.mdp.adopcion.controllers.person;

import java.util.List;

import org.h2.engine.Mode;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.adopcion.dto.person.VeterinarianDTO;
import co.edu.udistrital.mdp.adopcion.dto.person.VeterinarianDetailDTO;
import co.edu.udistrital.mdp.adopcion.entities.person.VeterinarianEntity;
import co.edu.udistrital.mdp.adopcion.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.adopcion.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.adopcion.services.person.VeterinarianService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;


@RestController
@RequestMapping ("/veterinarians")
public class VeterinarianController {
    @Autowired
    private VeterinarianService veterinarianService;
    @Autowired
    private ModelMapper modelMapper;
    @GetMapping

    @ResponseStatus(code = HttpStatus.OK)
    public List<VeterinarianDTO> findAll(){
        
        List<VeterinarianEntity> veterinarians = veterinarianService.getAllVeterinarians();

        return modelMapper.map(veterinarians,new TypeToken<List<VeterinarianDetailDTO>>(){

        }.getType());

    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public VeterinarianDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        
        VeterinarianEntity veterinarianEntity = veterinarianService.getVeterinarianById(id);
        if (veterinarianEntity == null) {
            throw new EntityNotFoundException("The veterinarian with the given id was not found");
        }
        return modelMapper.map(veterinarianEntity, VeterinarianDetailDTO.class);
    }
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public VeterinarianDTO create(@RequestBody VeterinarianDTO veterinarianDTO)throws IllegalOperationException, EntityNotFoundException {
        VeterinarianEntity veterinarianEntity = veterinarianService.createVeterinarian(modelMapper.map(veterinarianDTO, VeterinarianEntity.class));
        if (veterinarianEntity == null) {
            throw new IllegalOperationException("The veterinarian with the given id was not found");
        }
        return modelMapper.map(veterinarianEntity, VeterinarianDTO.class);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public VeterinarianDTO update(@PathVariable Long id, @RequestBody VeterinarianDTO veterinarianDTO) throws Exception, EntityNotFoundException, IllegalOperationException {
        VeterinarianEntity veterinarianEntity = veterinarianService.updateVeterinarian(id, modelMapper.map(veterinarianDTO, VeterinarianEntity.class));
        if (veterinarianEntity == null) {
            throw new EntityNotFoundException("The veterinarian with the given id was not found");
        }
        return modelMapper.map(veterinarianEntity, VeterinarianDTO.class);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id)throws EntityNotFoundException, IllegalOperationException {
    if (veterinarianService.getVeterinarianById(id) == null) {
        throw new EntityNotFoundException("The veterinarian with the given id was not found");
    }
        veterinarianService.deleteVeterinarian(id);
        
        }
    }





