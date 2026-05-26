package co.edu.udistrital.mdp.adopcion.services.person;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.adopcion.entities.person.VeterinarianEntity;
import co.edu.udistrital.mdp.adopcion.repositories.adoption.AdoptionAplicationRepository;
import co.edu.udistrital.mdp.adopcion.repositories.adoption.AdoptionFollowUpRepository;
import co.edu.udistrital.mdp.adopcion.repositories.adoption.AdoptionRepository;
import co.edu.udistrital.mdp.adopcion.repositories.adoption.AdoptionTestRepository;
import co.edu.udistrital.mdp.adopcion.repositories.event.MedicalEventRepository;
import co.edu.udistrital.mdp.adopcion.repositories.person.VeterinarianRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VeterinarianService {
    @Autowired
    private VeterinarianRepository veterinarianRepository;

    @Autowired
    private MedicalEventRepository medicalEventRepository;
    @Autowired
    private AdoptionAplicationRepository adoptionApplicationRepository;
    @Autowired
    private AdoptionFollowUpRepository adoptionFollowUpRepository;
    @Autowired
    private AdoptionRepository adoptionRepository;
    @Autowired
    private AdoptionTestRepository adoptionTestRepository;

    @Transactional
    public VeterinarianEntity createVeterinarian(VeterinarianEntity veterinarian) {
        if (veterinarian.getDisponibilities() == null || veterinarian.getDisponibilities().isEmpty()) {
            throw new IllegalArgumentException("Disponibilities must not be empty");
        }
        if (veterinarian.getLicenseNumber() == null) {
            throw new IllegalArgumentException("License number must not be null");
        }
        if (veterinarian.getSpeciality() == null) {
            throw new IllegalArgumentException("Speciality must not be null");
        }
        return veterinarianRepository.save(veterinarian);
    }
    @Transactional
    public List<VeterinarianEntity> getAllVeterinarians() {
        return veterinarianRepository.findAll();
    }

    @Transactional
    public VeterinarianEntity getVeterinarianById(Long id) {
        return veterinarianRepository.findById(id).orElse(null);
    }

    @Transactional
    public VeterinarianEntity updateVeterinarian(Long id, VeterinarianEntity veterinarian) throws Exception {
        if (veterinarian.getLicenseNumber() == null) {
            throw new IllegalArgumentException("License number must not be null");
        }
        if (veterinarian.getSpeciality() == null) {
            throw new IllegalArgumentException("Speciality must not be null");
        }
        if (veterinarianRepository.existsById(id)) {
            veterinarian.setId(id);
            return veterinarianRepository.save(veterinarian);
        } else {
            return null;
        }
    }

    
    public void deleteVeterinarian(Long id) {
        veterinarianRepository.deleteById(id);
        }
    public boolean existsById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existsById'");
    }
    }