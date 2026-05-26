package co.edu.udistrital.mdp.adopcion.controllers.adoption;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.adopcion.dto.adoption.AdoptionTestDTO;
import co.edu.udistrital.mdp.adopcion.entities.adoption.AdoptionTestEntity;
import co.edu.udistrital.mdp.adopcion.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.adopcion.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.adopcion.services.adoption.AdoptionTestService;

@RestController
@RequestMapping("/adoption-tests")
public class AdoptionTestController {

    @Autowired
    private AdoptionTestService adoptionTestService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<AdoptionTestDTO> findAll() {
        List<AdoptionTestEntity> tests = adoptionTestService.getAllTests();
        return modelMapper.map(tests, new TypeToken<List<AdoptionTestDTO>>() {}.getType());
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public AdoptionTestDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        AdoptionTestEntity testEntity = adoptionTestService.getTestById(id);
        return modelMapper.map(testEntity, AdoptionTestDTO.class);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public AdoptionTestDTO create(@RequestBody AdoptionTestDTO testDTO) throws IllegalOperationException {
        AdoptionTestEntity testEntity = modelMapper.map(testDTO, AdoptionTestEntity.class);
        AdoptionTestEntity created = adoptionTestService.createTest(testEntity);
        return modelMapper.map(created, AdoptionTestDTO.class);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public AdoptionTestDTO update(@PathVariable Long id, @RequestBody AdoptionTestDTO testDTO)
            throws EntityNotFoundException {
        AdoptionTestEntity testEntity = modelMapper.map(testDTO, AdoptionTestEntity.class);
        AdoptionTestEntity updated = adoptionTestService.updateTest(id, testEntity);
        return modelMapper.map(updated, AdoptionTestDTO.class);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException {
        adoptionTestService.deleteTest(id);
    }
}


