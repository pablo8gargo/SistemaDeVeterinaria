package co.edu.udistrital.mdp.adopcion.services.events;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.adopcion.entities.events.ShelterEventEntity;
import co.edu.udistrital.mdp.adopcion.repositories.ShelterRepository;
import co.edu.udistrital.mdp.adopcion.repositories.event.ShelterEventRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ShelterEventService {
    @Autowired
    private ShelterEventRepository shelterEventRepository;
    @Autowired
    private ShelterRepository shelterRepository;
    
    @Transactional
    public ShelterEventEntity createShelterEvent(ShelterEventEntity shelterEvent) {
        if (shelterEvent.getShelter() == null) {
            throw new IllegalArgumentException("Shelter must not be null");
        }
        if (shelterEvent.getName() == null) {
            throw new IllegalArgumentException("Name must not be null");
        }
        if (shelterEvent.getDate() == null) {
            throw new IllegalArgumentException("Event date must not be null");
        }
        return shelterEventRepository.save(shelterEvent);
    }

    @Transactional
    public List<ShelterEventEntity> getAllShelterEvents() {
        return shelterEventRepository.findAll();
    }

    @Transactional
    public ShelterEventEntity getShelterEventById(Long id) {
        return shelterEventRepository.findById(id).orElse(null);
    }

    @Transactional
    public ShelterEventEntity updateShelterEvent(Long id, ShelterEventEntity shelterEvent) {
        if (shelterEventRepository.existsById(id)) {
            shelterEvent.setId(id);
            return shelterEventRepository.save(shelterEvent);
        } else {
            return null;
        }
    }

    @Transactional
    public void deleteShelterEvent(Long id) {
        if (shelterEventRepository.existsById(id)) {
            shelterEventRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Shelter event not found");
        }
    }
}
