package co.edu.udistrital.mdp.adopcion.services.events;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.adopcion.repositories.ShelterRepository;
import co.edu.udistrital.mdp.adopcion.repositories.event.ShelterArrivalRepository;
import co.edu.udistrital.mdp.adopcion.repositories.person.VeterinarianRepository;
import co.edu.udistrital.mdp.adopcion.repositories.pet.PetRepository;
import co.edu.udistrital.mdp.adopcion.entities.events.ShelterArrivalEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ShelterArrivalService {
    @Autowired
    private ShelterArrivalRepository shelterArrivalRepository;
    @Autowired
    private ShelterRepository shelterRepository;
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private VeterinarianRepository veterinarianRepository;
    
    @Transactional
    public ShelterArrivalEntity createShelterArrival(ShelterArrivalEntity shelterArrival) {
        if (shelterArrival.getShelter() == null) {
            throw new IllegalArgumentException("Shelter must not be null");
        }
        if (shelterArrival.getPet() == null) {
            throw new IllegalArgumentException("Pet must not be null");
        }
        if (shelterArrival.getVeterinarian() == null) {
            throw new IllegalArgumentException("Veterinarian must not be null");
        }
        if (shelterArrival.getDate() == null) {
            throw new IllegalArgumentException("Date must not be null");
        }
        if (shelterArrival.getPetCondition() == null) {
            throw new IllegalArgumentException("Pet condition must not be null");
        }
        if (shelterArrival.getShelter() == null) {
            throw new IllegalArgumentException("Shelter must not be null");
        }
        if (shelterArrival.getVeterinarian() == null) {
            throw new IllegalArgumentException("Veterinarian must not be null");
        }
        return shelterArrivalRepository.save(shelterArrival);
    }

    @Transactional
    public ShelterArrivalEntity updateShelterArrival(Long id, ShelterArrivalEntity shelterArrival) {
        if (shelterArrival.getId() == null || id == null) {
            throw new IllegalArgumentException("ShelterArrival ID must not be null");
        }
        if (!shelterArrivalRepository.existsById(id)) {
            throw new IllegalArgumentException("ShelterArrival with ID " + id + " does not exist");
        }
        return shelterArrivalRepository.save(shelterArrival);
    }

    @Transactional
    public void deleteShelterArrival(Long id) {
        if (!shelterArrivalRepository.existsById(id)) {
            throw new IllegalArgumentException("ShelterArrival ID does not exist");
        }
        shelterArrivalRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public ShelterArrivalEntity getShelterArrivalById(Long id) {
        return shelterArrivalRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<ShelterArrivalEntity> getAllShelterArrivals() {
        return shelterArrivalRepository.findAll();
    }
}
