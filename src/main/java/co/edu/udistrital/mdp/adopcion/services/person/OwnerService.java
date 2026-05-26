package co.edu.udistrital.mdp.adopcion.services.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.adopcion.entities.person.OwnerEntity;
import co.edu.udistrital.mdp.adopcion.repositories.adoption.AdoptionAplicationRepository;
import co.edu.udistrital.mdp.adopcion.repositories.adoption.AdoptionFollowUpRepository;
import co.edu.udistrital.mdp.adopcion.repositories.adoption.AdoptionRepository;
import co.edu.udistrital.mdp.adopcion.repositories.adoption.AdoptionTestRepository;
import co.edu.udistrital.mdp.adopcion.repositories.event.MedicalEventRepository;
import co.edu.udistrital.mdp.adopcion.repositories.person.OwnerRepository;
import lombok.extern.slf4j.Slf4j;
import java.util.List;


@Slf4j
@Service
public class OwnerService {
    @Autowired
    private OwnerRepository ownerRepository;

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
    public OwnerEntity createOwner(OwnerEntity owner) {
        if (owner.getFirstName() == null || owner.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("The name must not be empty");
        }
        
        if (owner.getLastName() == null || owner.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("The last name must not be empty");
        }
        
        if (owner.getPhoneNumber() == null || owner.getPhoneNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("The phone number must not be empty");
        }
        
        if (owner.getEmail() == null || owner.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("The email must not be empty");
        }
        
        if (owner.getAddress() == null || owner.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("The address must not be empty");
        }
        
        if (owner.getHouseType() == null) {
            throw new IllegalArgumentException("The house type must not be null");
        }
        
        return ownerRepository.save(owner);
    }
    @Transactional
    public OwnerEntity updateOwner(Long id, OwnerEntity owner, boolean isAdmin) {
        if (!ownerRepository.existsById(id)) {
            return null;
        }
        
        OwnerEntity existingOwner = ownerRepository.findById(id).orElse(null);
        if (existingOwner == null) {
            return null;
        }
        

        owner.setId(id);
        owner.setAdoptions(existingOwner.getAdoptions());
        owner.setAdoptionTests(existingOwner.getAdoptionTests());
        owner.setAdoptionApplications(existingOwner.getAdoptionApplications());
        owner.setPets(existingOwner.getPets());
        
        return ownerRepository.save(owner);
    }
    
    @Transactional
    public void deleteOwner(Long id, boolean isAdmin) {
        if (!isAdmin) {
            throw new SecurityException("Only an admin can delete an owner");
        }
        if (!ownerRepository.existsById(id)) {
            throw new IllegalArgumentException("The owner with the given ID does not exist");
        }
        
        ownerRepository.deleteById(id);
    }
    @Transactional
    public OwnerEntity getOwner(Long id) {
        return ownerRepository.findById(id).orElse(null);
    }

    @Transactional
    public List<OwnerEntity> getOwners() {
        return ownerRepository.findAll();
    }
}