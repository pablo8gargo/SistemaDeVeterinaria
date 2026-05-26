package co.edu.udistrital.mdp.adopcion.services.events;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.adopcion.entities.events.MedicalEventEntity;
import co.edu.udistrital.mdp.adopcion.repositories.event.MedicalEventRepository;
import co.edu.udistrital.mdp.adopcion.repositories.person.VeterinarianRepository;
import co.edu.udistrital.mdp.adopcion.repositories.pet.PetRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MedicalEventService {
    @Autowired
    private MedicalEventRepository medicalEventRepository;

    @Autowired
    private VeterinarianRepository veterinarianRepository;

    @Autowired
    private PetRepository petRepository;

    @Transactional
    public MedicalEventEntity createMedicalEvent(MedicalEventEntity medicalEvent) {
        if (medicalEvent.getVeterinarian() == null) {
            throw new IllegalArgumentException("Veterinarian must not be null");
        }
        if (medicalEvent.getPet() == null) {
            throw new IllegalArgumentException("Pet must not be null");
        }
        if (medicalEvent.getDate() == null) {
            throw new IllegalArgumentException("Date must not be null");
        }
        if (medicalEvent.getDescription() == null) {
            throw new IllegalArgumentException("Description must not be null");
        }
        return medicalEventRepository.save(medicalEvent);
    }

    @Transactional
    public List<MedicalEventEntity> getAllMedicalEvents() {
        return medicalEventRepository.findAll();
    }

    @Transactional
    public MedicalEventEntity getMedicalEventById(Long id) {
        return medicalEventRepository.findById(id).orElse(null);
    }

    @Transactional
    public MedicalEventEntity updateMedicalEvent(Long id, MedicalEventEntity medicalEvent)
            throws IllegalArgumentException {
        if (medicalEventRepository.existsById(id)) {
            medicalEvent.setId(id);
            return medicalEventRepository.save(medicalEvent);
        } else {
            throw new IllegalArgumentException("Medical event with ID " + id + " does not exist");
        }
    }

    @Transactional
    public void deleteMedicalEvent(Long id) {
        medicalEventRepository.deleteById(id);
    }
}
