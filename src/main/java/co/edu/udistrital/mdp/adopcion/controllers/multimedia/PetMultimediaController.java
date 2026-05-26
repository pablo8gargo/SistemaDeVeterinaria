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
import co.edu.udistrital.mdp.adopcion.entities.pet.PetEntity;
import co.edu.udistrital.mdp.adopcion.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.adopcion.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.adopcion.services.multimedia.MultimediaService;
import co.edu.udistrital.mdp.adopcion.services.pet.PetService;

@RestController
@RequestMapping("/pets")
public class PetMultimediaController {

    @Autowired
    private PetService petService;

    @Autowired
    private MultimediaService multimediaService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/{petId}/multimedia")
    @ResponseStatus(code = HttpStatus.OK)
    public List<MultimediaDetailDTO> findAll(@PathVariable Long petId)
            throws EntityNotFoundException, IllegalOperationException {
        PetEntity pet = petService.getPet(petId);
        if (pet == null) {
            throw new EntityNotFoundException("Pet not found with ID: " + petId);
        }
        List<MultimediaEntity> mediaList = multimediaService.getAllMultimedia();
        mediaList.removeIf(media -> media.getPet() == null || !media.getPet().getId().equals(petId));
        if (mediaList.isEmpty()) {
            throw new IllegalOperationException("No multimedia found for pet with ID: " + petId);
        }
        return modelMapper.map(mediaList, new TypeToken<List<MultimediaDetailDTO>>() {}.getType());
    }

    @GetMapping("/{petId}/multimedia/{mediaId}")
    @ResponseStatus(code = HttpStatus.OK)
    public MultimediaDetailDTO findOne(@PathVariable Long petId, @PathVariable Long mediaId)
            throws EntityNotFoundException, IllegalOperationException {
        PetEntity pet = petService.getPet(petId);
        if (pet == null) {
            throw new EntityNotFoundException("Pet not found with ID: " + petId);
        }
        MultimediaEntity media = multimediaService.getMultimediaById(mediaId);
        if (media == null || media.getPet() == null || !media.getPet().getId().equals(petId)) {
            throw new IllegalOperationException("In this pet, multimedia not found with ID: " + mediaId);
        }
        return modelMapper.map(media, MultimediaDetailDTO.class);
    }

    @PutMapping("/{petId}/multimedia")
    @ResponseStatus(code = HttpStatus.OK)
    public List<MultimediaDetailDTO> replaceAll(@PathVariable Long petId,
                                                @RequestBody List<MultimediaDTO> multimediaDTOs)
            throws EntityNotFoundException, IllegalOperationException {
        PetEntity pet = petService.getPet(petId);
        if (pet == null) {
            throw new EntityNotFoundException("Pet not found with ID: " + petId);
        }

        List<MultimediaEntity> mediaEntities = modelMapper.map(multimediaDTOs,
                new TypeToken<List<MultimediaEntity>>() {}.getType());
        for (MultimediaEntity media : mediaEntities) {
            media.setPet(pet);
            try {
                multimediaService.updateMultimedia(media.getId(), media);
            } catch (IllegalArgumentException e) {
                throw new EntityNotFoundException("Failed to update multimedia with ID: " + media.getId());
            }
        }

        mediaEntities = multimediaService.getAllMultimedia();
        mediaEntities.removeIf(media -> media.getPet() == null || !media.getPet().getId().equals(petId));

        return modelMapper.map(mediaEntities, new TypeToken<List<MultimediaDetailDTO>>() {}.getType());
    }

    @PostMapping("/{petId}/multimedia/{mediaId}")
    @ResponseStatus(code = HttpStatus.OK)
    public MultimediaDetailDTO associateMultimedia(@PathVariable Long petId, @PathVariable Long mediaId)
            throws EntityNotFoundException, IllegalOperationException {
        PetEntity pet = petService.getPet(petId);
        if (pet == null) {
            throw new EntityNotFoundException("Pet not found with ID: " + petId);
        }

        MultimediaEntity media = multimediaService.getMultimediaById(mediaId);
        if (media == null) {
            throw new EntityNotFoundException("Multimedia not found with ID: " + mediaId);
        }

        media.setPet(pet);
        media = multimediaService.updateMultimedia(mediaId, media);

        return modelMapper.map(media, MultimediaDetailDTO.class);
    }

    @DeleteMapping("/{petId}/multimedia/{mediaId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long petId, @PathVariable Long mediaId)
            throws EntityNotFoundException, IllegalOperationException {
        PetEntity pet = petService.getPet(petId);
        if (pet == null) {
            throw new EntityNotFoundException("Pet not found with ID: " + petId);
        }

        MultimediaEntity media = multimediaService.getMultimediaById(mediaId);
        if (media == null || media.getPet() == null || !media.getPet().getId().equals(petId)) {
            throw new IllegalOperationException("In this pet, multimedia not found with ID: " + mediaId);
        }

        multimediaService.deleteMultimedia(mediaId);
    }
}

