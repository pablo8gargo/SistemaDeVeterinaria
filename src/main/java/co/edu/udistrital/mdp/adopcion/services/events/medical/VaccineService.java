package co.edu.udistrital.mdp.adopcion.services.events.medical;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.adopcion.entities.events.medical.VaccineEntity;
import co.edu.udistrital.mdp.adopcion.repositories.event.ShelterEventRepository;
import co.edu.udistrital.mdp.adopcion.repositories.event.medical.VaccineCardRepository;
import co.edu.udistrital.mdp.adopcion.repositories.event.medical.VaccineRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VaccineService {
    
    @Autowired
    private VaccineRepository vaccineRepository;

    @Autowired
    private VaccineCardRepository vaccineCardRepository;

    @Transactional
    public VaccineEntity createVaccine(VaccineEntity vaccine) {
        if (vaccine.getName() == null) {
            throw new IllegalArgumentException("Name must not be null");
        }
        if (vaccine.getBrandName() == null) {
            throw new IllegalArgumentException("Brand name must not be null");
        }
        if (vaccine.getNextDate() == null) {
            throw new IllegalArgumentException("Next date must not be null");
        }
        if (vaccine.getDosis() == null) {
            throw new IllegalArgumentException("Dosis must not be null");
        }
        if(vaccine.getVaccineCard() == null) {
            throw new IllegalArgumentException("Vaccine card must not be null");
        }
        return vaccineRepository.save(vaccine);
    }

    @Transactional
    public List<VaccineEntity> getAllVaccines() {
        return vaccineRepository.findAll();
    }

    @Transactional
    public VaccineEntity getVaccineById(Long id) {
        return vaccineRepository.findById(id).orElse(null);
    }

    @Transactional
    public VaccineEntity updateVaccine(Long id, VaccineEntity vaccine) {
        if (vaccineRepository.existsById(id)) {
            vaccine.setId(id);
            return vaccineRepository.save(vaccine);
        } else {
            return null;
        }
    }

    @Transactional
    public void deleteVaccine(Long id) {
        vaccineRepository.deleteById(id);
    }
}
