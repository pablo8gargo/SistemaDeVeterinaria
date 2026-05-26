package co.edu.udistrital.mdp.adopcion.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.adopcion.entities.ShelterEntity;
import co.edu.udistrital.mdp.adopcion.repositories.ShelterRepository;
import co.edu.udistrital.mdp.adopcion.repositories.event.ShelterEventRepository;
import co.edu.udistrital.mdp.adopcion.repositories.pet.PetRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ShelterService {
    @Autowired
    private ShelterRepository shelterRepository;
    @Autowired
    private ShelterEventRepository shelterEventRepository;
    @Autowired
    private PetRepository petRepository;

    @Transactional
    public ShelterEntity createShelter(ShelterEntity shelter) {
        if (shelter.getName() == null) {
            throw new IllegalArgumentException("Name must not be null");
        }
        if (shelter.getAddress() == null) {
            throw new IllegalArgumentException("Address must not be null");
        }
        if (shelter.getPhone() == null) {
            throw new IllegalArgumentException("Phone must not be null");
        }
        if (shelter.getEmail() == null) {
            throw new IllegalArgumentException("Email must not be null");
        }

        return shelterRepository.save(shelter);
    }
    @Transactional
    public List<ShelterEntity> getAllShelters() {
        return shelterRepository.findAll();
    }
    @Transactional
    public ShelterEntity getShelterById(Long id) {
        return shelterRepository.findById(id).orElse(null);
    }
    @Transactional
    public ShelterEntity updateShelter(Long id, ShelterEntity shelter) {
        if(shelterRepository.existsById(id)){
            shelter.setId(id);
            return shelterRepository.save(shelter);
        } else {
            return null;
        }
    }
    
    @Transactional
    public void deleteShelter(Long id) {
        shelterRepository.deleteById(id);
    }
    

}