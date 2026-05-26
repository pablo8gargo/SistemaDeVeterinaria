package co.edu.udistrital.mdp.adopcion.services.person;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import co.edu.udistrital.mdp.adopcion.entities.adoption.AdoptionApplicationEntity;
import co.edu.udistrital.mdp.adopcion.entities.adoption.AdoptionEntity;
import co.edu.udistrital.mdp.adopcion.entities.adoption.AdoptionTestEntity;
import co.edu.udistrital.mdp.adopcion.entities.person.OwnerEntity;
import co.edu.udistrital.mdp.adopcion.entities.pet.PetEntity;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(OwnerService.class)
public class OwnerServiceTest {
    @Autowired
    private OwnerService ownerService;
    @Autowired
    private TestEntityManager entityManager;
    private PodamFactory factory = new PodamFactoryImpl();
    private List<OwnerEntity> ownerList = new ArrayList<>();
    private List<PetEntity> petList = new ArrayList<>();
    private List<AdoptionApplicationEntity> adoptionApplicationList = new ArrayList<>();
    private List<AdoptionTestEntity> adoptionTestList = new ArrayList<>();
    private List<AdoptionEntity> adoptionList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }
    
    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from OwnerEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from PetEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ShelterArrivalEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ShelterEventEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdoptionApplicationEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdoptionFollowUpEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdoptionTestEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdoptionEntity").executeUpdate();
    }
    
    private void insertData() {
        int n = 5;
        for (int i = 0; i < n; i++) {
            // Create AdoptionEntity
            AdoptionEntity adoption = factory.manufacturePojo(AdoptionEntity.class);
            entityManager.persist(adoption);
            adoptionList.add(adoption);

            // Create AdoptionTestEntity
            AdoptionTestEntity adoptionTest = factory.manufacturePojo(AdoptionTestEntity.class);
            entityManager.persist(adoptionTest);
            adoptionTestList.add(adoptionTest);

            // Create AdoptionApplicationEntity
            AdoptionApplicationEntity adoptionApplication = factory.manufacturePojo(AdoptionApplicationEntity.class);
            entityManager.persist(adoptionApplication);
            adoptionApplicationList.add(adoptionApplication);

            // Create PetEntity
            PetEntity pet = factory.manufacturePojo(PetEntity.class);
            pet.setAdoption(adoption);
            entityManager.persist(pet);
            petList.add(pet);

            // Create OwnerEntity
            OwnerEntity owner = factory.manufacturePojo(OwnerEntity.class);
            entityManager.persist(owner);
            ownerList.add(owner);

            // Set relationships
            owner.getAdoptions().add(adoption);
            owner.getAdoptionApplications().add(adoptionApplication);
            owner.getAdoptionTests().add(adoptionTest);
            owner.getPets().add(pet);
        }
        entityManager.flush();
    }
    
    @Test
    void testCreateOwner() {
        OwnerEntity newOwner = factory.manufacturePojo(OwnerEntity.class);
        OwnerEntity createdOwner = ownerService.createOwner(newOwner);
        assertNotNull(createdOwner);
        OwnerEntity foundOwner = entityManager.find(OwnerEntity.class, createdOwner.getId());
        assertEquals(newOwner.getFirstName(), foundOwner.getFirstName());
        assertEquals(newOwner.getLastName(), foundOwner.getLastName());
        assertEquals(newOwner.getPhoneNumber(), foundOwner.getPhoneNumber());
        assertEquals(newOwner.getEmail(), foundOwner.getEmail());
        assertEquals(newOwner.getAddress(), foundOwner.getAddress());
        assertEquals(newOwner.getHouseType(), foundOwner.getHouseType());
        assertEquals(newOwner.getPets(), foundOwner.getPets());
    }
    
    @Test
    void testCreateOwnerWithNullName() {
        OwnerEntity newOwner = factory.manufacturePojo(OwnerEntity.class);
        newOwner.setFirstName(null);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ownerService.createOwner(newOwner);
        });
        String expectedMessage = "The name must not be empty";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testGetOwner() {
        OwnerEntity owner = ownerList.get(0);
        OwnerEntity foundOwner = ownerService.getOwner(owner.getId());
        assertNotNull(foundOwner);
        assertEquals(owner.getFirstName(), foundOwner.getFirstName());
        assertEquals(owner.getLastName(), foundOwner.getLastName());
        assertEquals(owner.getPhoneNumber(), foundOwner.getPhoneNumber());
        assertEquals(owner.getEmail(), foundOwner.getEmail());
        assertEquals(owner.getAddress(), foundOwner.getAddress());
        assertEquals(owner.getHouseType(), foundOwner.getHouseType());
    }

    @Test
    void testGetOwners() {
        List<OwnerEntity> foundOwners = ownerService.getOwners();
        assertNotNull(foundOwners);
        assertEquals(ownerList.size(), foundOwners.size());
        for (int i = 0; i < ownerList.size(); i++) {
            OwnerEntity expectedOwner = ownerList.get(i);
            OwnerEntity actualOwner = foundOwners.get(i);
            assertEquals(expectedOwner.getFirstName(), actualOwner.getFirstName());
            assertEquals(expectedOwner.getLastName(), actualOwner.getLastName());
            assertEquals(expectedOwner.getPhoneNumber(), actualOwner.getPhoneNumber());
            assertEquals(expectedOwner.getEmail(), actualOwner.getEmail());
            assertEquals(expectedOwner.getAddress(), actualOwner.getAddress());
            assertEquals(expectedOwner.getHouseType(), actualOwner.getHouseType());
        }
    }
    
    @Test
    void testUpdateOwner() {
        OwnerEntity owner = ownerList.get(0);
        OwnerEntity updatedOwner = factory.manufacturePojo(OwnerEntity.class);
        updatedOwner.setId(owner.getId());
        
        OwnerEntity result = ownerService.updateOwner(owner.getId(), updatedOwner, false);
        assertNotNull(result);
        assertEquals(updatedOwner.getFirstName(), result.getFirstName());
        assertEquals(updatedOwner.getLastName(), result.getLastName());
        assertEquals(updatedOwner.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(updatedOwner.getEmail(), result.getEmail());
        assertEquals(updatedOwner.getAddress(), result.getAddress());
        assertEquals(updatedOwner.getHouseType(), result.getHouseType());
    }
    
    @Test
    void testDeleteOwner() {
        OwnerEntity owner = ownerList.get(0);
        ownerService.deleteOwner(owner.getId(), true);
        
        OwnerEntity deletedOwner = entityManager.find(OwnerEntity.class, owner.getId());
    }
    
    @Test
    void testCreateOwnerWithNullEmail() {
        OwnerEntity newOwner = factory.manufacturePojo(OwnerEntity.class);
        newOwner.setEmail(null);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ownerService.createOwner(newOwner);
        });
        String expectedMessage = "email"; // Adjust based on your actual validation message
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.toLowerCase().contains(expectedMessage.toLowerCase()));
    }
    
    @Test
    void testCreateOwnerWithNullPhone() {
        OwnerEntity newOwner = factory.manufacturePojo(OwnerEntity.class);
        newOwner.setPhoneNumber(null);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ownerService.createOwner(newOwner);
        });
        String expectedMessage = "phone"; // Adjust based on your actual validation message
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.toLowerCase().contains(expectedMessage.toLowerCase()));
    }
}