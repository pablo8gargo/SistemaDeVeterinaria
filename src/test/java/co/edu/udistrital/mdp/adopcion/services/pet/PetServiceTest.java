package co.edu.udistrital.mdp.adopcion.services.pet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.adopcion.entities.ShelterEntity;
import co.edu.udistrital.mdp.adopcion.entities.adoption.AdoptionApplicationEntity;
import co.edu.udistrital.mdp.adopcion.entities.adoption.AdoptionEntity;
import co.edu.udistrital.mdp.adopcion.entities.events.MedicalEventEntity;
import co.edu.udistrital.mdp.adopcion.entities.events.ShelterArrivalEntity;
import co.edu.udistrital.mdp.adopcion.entities.events.medical.VaccineCardEntity;
import co.edu.udistrital.mdp.adopcion.entities.multimedia.MultimediaEntity;
import co.edu.udistrital.mdp.adopcion.entities.person.OwnerEntity;
import co.edu.udistrital.mdp.adopcion.entities.pet.GenderEnum;
import co.edu.udistrital.mdp.adopcion.entities.pet.PetEntity;
import co.edu.udistrital.mdp.adopcion.entities.pet.SizeEnum;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


@DataJpaTest
@Transactional
@Import(PetService.class)
public class PetServiceTest {
    @Autowired
    private PetService petService;
    @Autowired
    private TestEntityManager entityManager;
    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<PetEntity> petList = new ArrayList<>();
    private final List<String> nameList = new ArrayList<>();
    private final List<Date> birthDateList = new ArrayList<>();
    private final List<String> breedList = new ArrayList<>();
    private final List<SizeEnum> sizeList = new ArrayList<>();
    private final List<GenderEnum> genderList = new ArrayList<>();
    private final List<String> behaviorProfileList = new ArrayList<>();

    private final List<OwnerEntity> ownerList = new ArrayList<>();
    private final List<AdoptionApplicationEntity> adoptionApplicationList = new ArrayList<>();
    private final List<MedicalEventEntity> medicalEventList = new ArrayList<>();
    private final List<MultimediaEntity> multimediaList = new ArrayList<>();
    private final List<AdoptionEntity> adoptionList = new ArrayList<>();
    private final List<ShelterArrivalEntity> shelterArrivalList = new ArrayList<>();
    private final List<ShelterEntity> shelterList = new ArrayList<>();
    private final List<VaccineCardEntity> vaccineCardList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }
    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from PetEntity");
        entityManager.getEntityManager().createQuery("delete from OwnerEntity");
        entityManager.getEntityManager().createQuery("delete from AdoptionApplicationEntity");
        entityManager.getEntityManager().createQuery("delete from MedicalEventEntity");
        entityManager.getEntityManager().createQuery("delete from MultimediaEntity");
        entityManager.getEntityManager().createQuery("delete from AdoptionEntity");
        entityManager.getEntityManager().createQuery("delete from ShelterArrivalEntity");
        entityManager.getEntityManager().createQuery("delete from ShelterEntity");
        entityManager.getEntityManager().createQuery("delete from VaccineCardEntity");    }
    private void insertData() {
        int n = 5;
        for (int i = 0; i < n; i++) {
            String name = factory.manufacturePojo(String.class);
            nameList.add(name);
            Date birthDate = factory.manufacturePojo(Date.class);
            birthDateList.add(birthDate);
            String breed = factory.manufacturePojo(String.class);
            breedList.add(breed);
            SizeEnum size = factory.manufacturePojo(SizeEnum.class);
            sizeList.add(size);
            GenderEnum gender = factory.manufacturePojo(GenderEnum.class);
            genderList.add(gender);
            String behaviorProfile = factory.manufacturePojo(String.class);
            behaviorProfileList.add(behaviorProfile);

            OwnerEntity owner = factory.manufacturePojo(OwnerEntity.class);
            entityManager.persist(owner);
            ownerList.add(owner);
            AdoptionApplicationEntity adoptionApplication = factory.manufacturePojo(AdoptionApplicationEntity.class);
            entityManager.persist(adoptionApplication);
            adoptionApplicationList.add(adoptionApplication);
            MedicalEventEntity medicalEvent = factory.manufacturePojo(MedicalEventEntity.class);
            entityManager.persist(medicalEvent);
            medicalEventList.add(medicalEvent);
            MultimediaEntity multimedia = factory.manufacturePojo(MultimediaEntity.class);
            entityManager.persist(multimedia);
            multimediaList.add(multimedia);
            AdoptionEntity adoption = factory.manufacturePojo(AdoptionEntity.class);
            entityManager.persist(adoption);
            adoptionList.add(adoption);
            ShelterArrivalEntity shelterArrival = factory.manufacturePojo(ShelterArrivalEntity.class);
            entityManager.persist(shelterArrival);
            shelterArrivalList.add(shelterArrival);
            ShelterEntity shelter = factory.manufacturePojo(ShelterEntity.class);
            entityManager.persist(shelter);
            shelterList.add(shelter);
            VaccineCardEntity vaccineCard = factory.manufacturePojo(VaccineCardEntity.class);
            entityManager.persist(vaccineCard);
            vaccineCardList.add(vaccineCard);
            PetEntity pet = factory.manufacturePojo(PetEntity.class);

            pet.setName(name);
            pet.setBirthDate(birthDate);
            pet.setBreed(breed);
            pet.setSize(size);
            pet.setGender(gender);
            pet.setBehaviorProfile(behaviorProfile);
            pet.setOwners(ownerList);
            pet.setAdoptionApplications(adoptionApplicationList);
            pet.setMedicalEvents(medicalEventList);
            pet.setMultimedia(multimediaList);
            pet.setAdoption(adoptionList.get(i));
            pet.setShelterArrival(shelterArrivalList.get(i));
            pet.setShelter(shelterList.get(i));
            pet.setVaccineCard(vaccineCardList.get(i));
            entityManager.persist(pet);
            petList.add(pet);
        }
    }
    @Test
    void testCreatePet() {
        PetEntity pet = factory.manufacturePojo(PetEntity.class);
        pet.setShelter(shelterList.get(0));
        VaccineCardEntity vaccineCard = factory.manufacturePojo(VaccineCardEntity.class);
        pet.setVaccineCard(vaccineCard);
        PetEntity createdPet = petService.createPet(pet);
        assertNotNull(createdPet.getId());
        assertEquals(pet.getName(), createdPet.getName());
        assertEquals(pet.getBirthDate(), createdPet.getBirthDate());
        assertEquals(pet.getVaccineCard(), createdPet.getVaccineCard());
    }
    @Test
    void testCreatePetWithEmptyName() {
        PetEntity pet = factory.manufacturePojo(PetEntity.class);
        pet.setName(null);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            petService.createPet(pet);
        });
        String expectedMessage = "The name of the pet must not be empty";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    void testCreatePetWithEmptyBirthDate() {
        PetEntity pet = factory.manufacturePojo(PetEntity.class);
        pet.setBirthDate(null);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            petService.createPet(pet);
        });
        String expectedMessage = "The birth date of the pet must not be empty";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    void testCreatePetWithStringBirthDate() {
        PetEntity pet = factory.manufacturePojo(PetEntity.class);
        pet.setShelter(shelterList.get(0));
        pet.setVaccineCard(vaccineCardList.get(0));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            java.lang.reflect.Method setBirthDate = PetEntity.class.getMethod("setBirthDate", Date.class);
            setBirthDate.invoke(pet, "2020-01-01");
        });
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("argument type mismatch"));
    }
        
    @Test
    void testUpdatePet() {
        PetEntity pet = petList.get(0);
        PetEntity updatedPet = factory.manufacturePojo(PetEntity.class);
        updatedPet.setShelter(shelterList.get(0));
        updatedPet.setAdoption(adoptionList.get(0));
        updatedPet.setAdoptionApplications(adoptionApplicationList);
        updatedPet.setMedicalEvents(medicalEventList);
        updatedPet.setMultimedia(multimediaList);
        updatedPet.setOwners(ownerList);
        updatedPet.setName(nameList.get(0));
        updatedPet.setBirthDate(birthDateList.get(0));
        updatedPet.setBreed(breedList.get(0));
        updatedPet.setSize(sizeList.get(0));
        updatedPet.setVaccineCard(vaccineCardList.get(0));
        updatedPet.setId(pet.getId());
        updatedPet.setShelterArrival(shelterArrivalList.get(0));
        PetEntity result = petService.updatePet(pet.getId(), updatedPet);
        assertNotNull(result);
        assertEquals(updatedPet.getName(), result.getName());
        assertEquals((Date) updatedPet.getBirthDate(), (Date) result.getBirthDate());
        assertEquals((VaccineCardEntity) updatedPet.getVaccineCard(), (VaccineCardEntity) result.getVaccineCard());
    }
    @Test
    void testUpdatePetWithNullName() {
        PetEntity pet = petList.get(0);
        PetEntity updatedPet = factory.manufacturePojo(PetEntity.class);
        updatedPet.setId(pet.getId());
        updatedPet.setName(null);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            petService.updatePet(pet.getId(), updatedPet);
        });
        String expectedMessage = "The name of the pet must not be empty";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    void testUpdatePetWithNullBirthDate() {
        PetEntity pet = petList.get(0);
        PetEntity updatedPet = factory.manufacturePojo(PetEntity.class);
        updatedPet.setId(pet.getId());
        updatedPet.setBirthDate(null);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            petService.updatePet(pet.getId(), updatedPet);
        });
        String expectedMessage = "The birth date of the pet must not be empty";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    void testUpdatePetWithNullVaccineCard() {
        PetEntity pet = petList.get(0);
        PetEntity updatedPet = factory.manufacturePojo(PetEntity.class);
        updatedPet.setId(pet.getId());
        updatedPet.setVaccineCard(null);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            petService.updatePet(pet.getId(), updatedPet);
        });
        String expectedMessage = "The Vaccine card of the pet must not be empty";
        String actualMessage = exception.getMessage();
        assertFalse(actualMessage.contains(expectedMessage));
    }
    @Test
    void testUpdatePetWithAdmin() {
        PetEntity pet = petList.get(0);
        PetEntity updatedPet = factory.manufacturePojo(PetEntity.class);
        updatedPet.setId(pet.getId());
        updatedPet.setShelter(shelterList.get(0));
        updatedPet.setAdoption(adoptionList.get(0));
        updatedPet.setAdoptionApplications(adoptionApplicationList);
        updatedPet.setMedicalEvents(medicalEventList);
        updatedPet.setMultimedia(multimediaList);
        updatedPet.setOwners(ownerList);
        updatedPet.setName(nameList.get(0));
        updatedPet.setBirthDate(birthDateList.get(0));
        updatedPet.setBreed(breedList.get(0));
        updatedPet.setSize(sizeList.get(0));
        updatedPet.setVaccineCard(vaccineCardList.get(0));
        updatedPet.setId(pet.getId());
        updatedPet.setShelterArrival(shelterArrivalList.get(0));
        PetEntity result = petService.updatePet(pet.getId(), updatedPet);
        assertNotNull(result);
        assertEquals(updatedPet.getName(), result.getName());
        assertEquals((Date) updatedPet.getBirthDate(), (Date) result.getBirthDate());
        assertEquals(updatedPet.getVaccineCard(), result.getVaccineCard());
    }
}
