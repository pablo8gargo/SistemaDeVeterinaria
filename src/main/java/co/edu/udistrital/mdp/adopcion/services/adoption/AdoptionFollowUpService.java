package co.edu.udistrital.mdp.adopcion.services.adoption;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.adopcion.entities.adoption.AdoptionFollowUpEntity;
import co.edu.udistrital.mdp.adopcion.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.adopcion.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.adopcion.repositories.adoption.AdoptionFollowUpRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdoptionFollowUpService {

    @Autowired
    private AdoptionFollowUpRepository adoptionFollowUpRepository;

    @Transactional
    public AdoptionFollowUpEntity createFollowUp(AdoptionFollowUpEntity followUp) throws IllegalOperationException {
        if (followUp.getDescription() == null || followUp.getDescription().isBlank()) {
            throw new IllegalOperationException("Description must not be null or empty");
        }
        if (followUp.getObservations() == null) {
            throw new IllegalOperationException("Observations must not be null");
        }
        if (followUp.getFollowUpStatus() == null) {
            throw new IllegalOperationException("Follow-up status must not be null");
        }
        if (followUp.getPetCondition() == null) {
            throw new IllegalOperationException("Pet condition must not be null");
        }
        if (followUp.getVeterinarian() == null) {
            throw new IllegalOperationException("Veterinarian must not be null");
        }
        return adoptionFollowUpRepository.save(followUp);
    }

    @Transactional
    public List<AdoptionFollowUpEntity> getAllFollowUps() {
        return adoptionFollowUpRepository.findAll();
    }

    @Transactional
    public AdoptionFollowUpEntity getFollowUpById(Long id) throws EntityNotFoundException {
        Optional<AdoptionFollowUpEntity> followUp = adoptionFollowUpRepository.findById(id);
        if (followUp.isEmpty()) {
            throw new EntityNotFoundException("Adoption Follow-Up with id " + id + " not found");
        }
        return followUp.get();
    }

    @Transactional
    public AdoptionFollowUpEntity updateFollowUp(Long id, AdoptionFollowUpEntity followUp) throws EntityNotFoundException {
        if (!adoptionFollowUpRepository.existsById(id)) {
            throw new EntityNotFoundException("Adoption Follow-Up with id " + id + " not found");
        }
        followUp.setId(id);
        return adoptionFollowUpRepository.save(followUp);
    }

    @Transactional
    public void deleteFollowUp(Long id) throws EntityNotFoundException {
        if (!adoptionFollowUpRepository.existsById(id)) {
            throw new EntityNotFoundException("Adoption Follow-Up with id " + id + " not found");
        }
        adoptionFollowUpRepository.deleteById(id);
    }
}
