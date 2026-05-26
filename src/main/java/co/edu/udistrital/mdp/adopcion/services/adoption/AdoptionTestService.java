package co.edu.udistrital.mdp.adopcion.services.adoption;

import co.edu.udistrital.mdp.adopcion.entities.adoption.AdoptionTestEntity;
import co.edu.udistrital.mdp.adopcion.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.adopcion.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.adopcion.repositories.adoption.AdoptionTestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AdoptionTestService {

    @Autowired
    private AdoptionTestRepository adoptionTestRepository;

    @Transactional
    public AdoptionTestEntity createTest(AdoptionTestEntity test) throws IllegalOperationException {
        if (test.getDescription() == null || test.getDescription().isBlank()) {
            throw new IllegalOperationException("Description must not be null or empty");
        }
        if (test.getTestEnd() == null) {
            throw new IllegalOperationException("Test end date must not be null");
        }
        if (test.getTestObservations() == null) {
            throw new IllegalOperationException("Test observations must not be null");
        }
        if (test.getTypeTest() == null) {
            throw new IllegalOperationException("Test result type must not be null");
        }
        if (test.getOwner() == null) {
            throw new IllegalOperationException("Owner must not be null");
        }
        if (test.getVeterinarian() == null) {
            throw new IllegalOperationException("Veterinarian must not be null");
        }
        return adoptionTestRepository.save(test);
    }

    @Transactional(readOnly = true)
    public List<AdoptionTestEntity> getAllTests() {
        return adoptionTestRepository.findAll();
    }

    @Transactional(readOnly = true)
    public AdoptionTestEntity getTestById(Long id) throws EntityNotFoundException {
        Optional<AdoptionTestEntity> test = adoptionTestRepository.findById(id);
        if (test.isEmpty()) {
            throw new EntityNotFoundException("Adoption Test with id " + id + " not found");
        }
        return test.get();
    }

    @Transactional
    public AdoptionTestEntity updateTest(Long id, AdoptionTestEntity test) throws EntityNotFoundException {
        if (!adoptionTestRepository.existsById(id)) {
            throw new EntityNotFoundException("Adoption Test with id " + id + " not found");
        }
        test.setId(id);
        return adoptionTestRepository.save(test);
    }

    @Transactional
    public void deleteTest(Long id) throws EntityNotFoundException {
        if (!adoptionTestRepository.existsById(id)) {
            throw new EntityNotFoundException("Adoption Test with id " + id + " not found");
        }
        adoptionTestRepository.deleteById(id);
    }
}

