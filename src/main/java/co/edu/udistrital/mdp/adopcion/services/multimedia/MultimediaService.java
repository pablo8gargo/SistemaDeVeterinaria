package co.edu.udistrital.mdp.adopcion.services.multimedia;

import co.edu.udistrital.mdp.adopcion.entities.multimedia.MultimediaEntity;
import co.edu.udistrital.mdp.adopcion.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.adopcion.repositories.multimedia.MultimediaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MultimediaService {

    private final MultimediaRepository multimediaRepository;

    public MultimediaService(MultimediaRepository multimediaRepository) {
        this.multimediaRepository = multimediaRepository;
    }

    public MultimediaEntity createMultimedia(MultimediaEntity multimedia) {
        return multimediaRepository.save(multimedia);
    }

    public List<MultimediaEntity> getAllMultimedia() {
        return multimediaRepository.findAll();
    }

    public MultimediaEntity getMultimediaById(Long id) throws EntityNotFoundException {
        return multimediaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Multimedia not found with id: " + id));
    }

    public MultimediaEntity updateMultimedia(Long id, MultimediaEntity multimediaDetails) throws EntityNotFoundException {
        MultimediaEntity multimedia = getMultimediaById(id);
        multimedia.setUrl(multimediaDetails.getUrl());
        multimedia.setDescription(multimediaDetails.getDescription());
        multimedia.setMultimediaType(multimediaDetails.getMultimediaType());
        multimedia.setPet(multimediaDetails.getPet());
        return multimediaRepository.save(multimedia);
    }

    public void deleteMultimedia(Long id) throws EntityNotFoundException {
        MultimediaEntity multimedia = getMultimediaById(id);
        multimediaRepository.delete(multimedia);
    }
}
