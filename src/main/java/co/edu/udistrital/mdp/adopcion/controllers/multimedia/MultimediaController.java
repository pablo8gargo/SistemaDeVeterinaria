package co.edu.udistrital.mdp.adopcion.controllers.multimedia;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.adopcion.dto.multimedia.MultimediaDTO;
import co.edu.udistrital.mdp.adopcion.dto.multimedia.MultimediaDetailDTO;
import co.edu.udistrital.mdp.adopcion.entities.multimedia.MultimediaEntity;
import co.edu.udistrital.mdp.adopcion.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.adopcion.services.multimedia.MultimediaService;

@RestController
@RequestMapping("/multimedia")
public class MultimediaController {

    @Autowired
    private MultimediaService multimediaService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MultimediaDetailDTO> findAll() {
        List<MultimediaEntity> entities = multimediaService.getAllMultimedia();
        return modelMapper.map(entities, new TypeToken<List<MultimediaDetailDTO>>() {}.getType());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MultimediaDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        MultimediaEntity entity = multimediaService.getMultimediaById(id);
        return modelMapper.map(entity, MultimediaDetailDTO.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MultimediaDTO create(@RequestBody MultimediaDTO dto) {
        MultimediaEntity entity = multimediaService.createMultimedia(
                modelMapper.map(dto, MultimediaEntity.class));
        return modelMapper.map(entity, MultimediaDTO.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MultimediaDTO update(@PathVariable Long id, @RequestBody MultimediaDTO dto)
            throws EntityNotFoundException {
        MultimediaEntity entity = multimediaService.updateMultimedia(id,
                modelMapper.map(dto, MultimediaEntity.class));
        return modelMapper.map(entity, MultimediaDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException {
        multimediaService.deleteMultimedia(id);
    }
}
