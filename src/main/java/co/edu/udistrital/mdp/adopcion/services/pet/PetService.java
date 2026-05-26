package co.edu.udistrital.mdp.adopcion.services.pet;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.adopcion.entities.events.medical.VaccineCardEntity;
import co.edu.udistrital.mdp.adopcion.entities.pet.PetEntity;
import co.edu.udistrital.mdp.adopcion.repositories.adoption.AdoptionAplicationRepository;
import co.edu.udistrital.mdp.adopcion.repositories.adoption.AdoptionFollowUpRepository;
import co.edu.udistrital.mdp.adopcion.repositories.adoption.AdoptionRepository;
import co.edu.udistrital.mdp.adopcion.repositories.adoption.AdoptionTestRepository;
import co.edu.udistrital.mdp.adopcion.repositories.event.MedicalEventRepository;
import co.edu.udistrital.mdp.adopcion.repositories.event.medical.VaccineCardRepository;
import co.edu.udistrital.mdp.adopcion.repositories.person.VeterinarianRepository;
import co.edu.udistrital.mdp.adopcion.repositories.pet.PetRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PetService {
    @Autowired
    private PetRepository petRepository;

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
    @Autowired
    private VeterinarianRepository veterinarianRepository;
    @Autowired
    private VaccineCardRepository vaccineCardRepository;

    @Transactional
    public PetEntity createPet(PetEntity pet) {
        if (pet.getName() == null || pet.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("The name of the pet must not be empty");
        }
        if (pet.getBirthDate() == null) {
            throw new IllegalArgumentException("The birth date of the pet must not be empty");
        }
        if (pet.getVaccineCard() == null) {
            VaccineCardEntity vaccineCard = new VaccineCardEntity();
            vaccineCard = vaccineCardRepository.save(vaccineCard);
            pet.setVaccineCard(vaccineCard);
        }

        pet.setOwners(null);

        return petRepository.save(pet);
    }

    @Transactional
    public PetEntity updatePet(Long id, PetEntity pet) {
        PetEntity existingPet = petRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pet not found"));
        if (pet.getName() != null && !pet.getName().trim().isEmpty()) {
            existingPet.setName(pet.getName());
        } else {
            throw new IllegalArgumentException("The name of the pet must not be empty");
        }
        if (pet.getBreed() != null && !pet.getBreed().trim().isEmpty()) {
            existingPet.setBreed(pet.getBreed());
        } else {
            throw new IllegalArgumentException("The breed of the pet must not be empty");
        }
        if (pet.getSize() != null) {
            existingPet.setSize(pet.getSize());
        } else {
            throw new IllegalArgumentException("The size of the pet must not be empty");
        }
        if (pet.getGender() != null) {
            existingPet.setGender(pet.getGender());
        } else {
            throw new IllegalArgumentException("The gender of the pet must not be empty");
        }
        if (pet.getBehaviorProfile() != null && !pet.getBehaviorProfile().trim().isEmpty()) {
            existingPet.setBehaviorProfile(pet.getBehaviorProfile());
        } else {
            throw new IllegalArgumentException("The behavior profile of the pet must not be empty");
        }
        if (pet.getBirthDate() != null) {
            existingPet.setBirthDate(pet.getBirthDate());
        } else {
            throw new IllegalArgumentException("The birth date of the pet must not be empty");
        }
        if (pet.getShelter() != null) {
            existingPet.setShelter(pet.getShelter());
        } else {
            throw new IllegalArgumentException("The shelter of the pet must not be empty");
        }
        if (pet.getShelterArrival() != null) {
            existingPet.setShelterArrival(pet.getShelterArrival());
        } else {
            throw new IllegalArgumentException("The shelter arrival of the pet must not be empty");
        }
        if (pet.getVaccineCard() != null) {
            existingPet.setVaccineCard(pet.getVaccineCard());
        } else {
            throw new IllegalArgumentException("The vaccine card of the pet must not be empty");
        }
        return petRepository.save(existingPet);
    }

    @Transactional
    public PetEntity getPet(Long id) {
        return petRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Pet not found"));
    }

    @Transactional
    public List<PetEntity> getPets() {
        return petRepository.findAll();
    }

    @Transactional
    public void deletePet(Long id) {
        if (!petRepository.existsById(id)) {
            throw new IllegalArgumentException("The pet with the given ID does not exist");
        }

        PetEntity pet = petRepository.findById(id).orElse(null);
        if (pet == null) {
            throw new IllegalArgumentException("Pet not found");
        }

        // Verificar si la mascota tiene adopciones activas
        if (pet.getAdoption() != null) {
            throw new IllegalArgumentException("Cannot delete a pet that has an active adoption");
        }

        // Verificar si la mascota tiene aplicaciones de adopción pendientes
        if (!pet.getAdoptionApplications().isEmpty()) {
            throw new IllegalArgumentException("Cannot delete a pet that has pending adoption applications");
        }

        petRepository.deleteById(id);
    }

}