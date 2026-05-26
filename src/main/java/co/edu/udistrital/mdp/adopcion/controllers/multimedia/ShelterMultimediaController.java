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
import co.edu.udistrital.mdp.adopcion.entities.ShelterEntity;
import co.edu.udistrital.mdp.adopcion.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.adopcion.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.adopcion.services.multimedia.MultimediaService;
import co.edu.udistrital.mdp.adopcion.services.ShelterService;

@RestController
@RequestMapping("/shelters")
public class ShelterMultimediaController {

    @Autowired
    private ShelterService shelterService;

    @Autowired
    private MultimediaService multimediaService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/{shelterId}/multimedia")
    @ResponseStatus(code = HttpStatus.OK)
    public List<MultimediaDetailDTO> findAll(@PathVariable Long shelterId)
            throws EntityNotFoundException, IllegalOperationException {
        ShelterEntity shelter = shelterService.getShelterById(shelterId);
        if (shelter == null) {
            throw new EntityNotFoundException("Shelter not found with ID: " + shelterId);
        }
        List<MultimediaEntity> mediaList = multimediaService.getAllMultimedia();
        mediaList.removeIf(media -> media.getShelter() == null || !media.getShelter().getId().equals(shelterId));
        if (mediaList.isEmpty()) {
            throw new IllegalOperationException("No multimedia found for shelter with ID: " + shelterId);
        }
        return modelMapper.map(mediaList, new TypeToken<List<MultimediaDetailDTO>>() {}.getType());
    }

    @GetMapping("/{shelterId}/multimedia/{mediaId}")
    @ResponseStatus(code = HttpStatus.OK)
    public MultimediaDetailDTO findOne(@PathVariable Long shelterId, @PathVariable Long mediaId)
            throws EntityNotFoundException, IllegalOperationException {
        ShelterEntity shelter = shelterService.getShelterById(shelterId);
        if (shelter == null) {
            throw new EntityNotFoundException("Shelter not found with ID: " + shelterId);
        }
        MultimediaEntity media = multimediaService.getMultimediaById(mediaId);
        if (media == null || media.getShelter() == null || !media.getShelter().getId().equals(shelterId)) {
            throw new IllegalOperationException("In this shelter, multimedia not found with ID: " + mediaId);
        }
        return modelMapper.map(media, MultimediaDetailDTO.class);
    }

    @PutMapping("/{shelterId}/multimedia")
    @ResponseStatus(code = HttpStatus.OK)
    public List<MultimediaDetailDTO> replaceAll(@PathVariable Long shelterId,
                                                @RequestBody List<MultimediaDTO> multimediaDTOs)
            throws EntityNotFoundException, IllegalOperationException {
        ShelterEntity shelter = shelterService.getShelterById(shelterId);
        if (shelter == null) {
            throw new EntityNotFoundException("Shelter not found with ID: " + shelterId);
        }

        List<MultimediaEntity> mediaEntities = modelMapper.map(multimediaDTOs,
                new TypeToken<List<MultimediaEntity>>() {}.getType());
        for (MultimediaEntity media : mediaEntities) {
            media.setShelter(shelter);
            try {
                multimediaService.updateMultimedia(media.getId(), media);
            } catch (IllegalArgumentException e) {
                throw new EntityNotFoundException("Failed to update multimedia with ID: " + media.getId());
            }
        }

        mediaEntities = multimediaService.getAllMultimedia();
        mediaEntities.removeIf(media -> media.getShelter() == null || !media.getShelter().getId().equals(shelterId));

        return modelMapper.map(mediaEntities, new TypeToken<List<MultimediaDetailDTO>>() {}.getType());
    }

    @PostMapping("/{shelterId}/multimedia/{mediaId}")
    @ResponseStatus(code = HttpStatus.OK)
    public MultimediaDetailDTO associateMultimedia(@PathVariable Long shelterId, @PathVariable Long mediaId)
            throws EntityNotFoundException, IllegalOperationException {
        ShelterEntity shelter = shelterService.getShelterById(shelterId);
        if (shelter == null) {
            throw new EntityNotFoundException("Shelter not found with ID: " + shelterId);
        }

        MultimediaEntity media = multimediaService.getMultimediaById(mediaId);
        if (media == null) {
            throw new EntityNotFoundException("Multimedia not found with ID: " + mediaId);
        }

        media.setShelter(shelter);
        media = multimediaService.updateMultimedia(mediaId, media);
        return modelMapper.map(media, MultimediaDetailDTO.class);
    }
}