package co.edu.udistrital.mdp.adopcion.services.adoption;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.adopcion.entities.adoption.AdoptionApplicationEntity;
import co.edu.udistrital.mdp.adopcion.entities.adoption.AdoptionEntity;
import co.edu.udistrital.mdp.adopcion.entities.adoption.AdoptionStatusEnum;
import co.edu.udistrital.mdp.adopcion.entities.person.OwnerEntity;
import co.edu.udistrital.mdp.adopcion.entities.person.VeterinarianEntity;
import co.edu.udistrital.mdp.adopcion.entities.pet.PetEntity;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(AdoptionService.class)
public class AdoptionServiceTest {

    @Autowired
    private AdoptionService adoptionService;

    @Autowired
    private TestEntityManager entityManager;
    
    private PodamFactory factory = new PodamFactoryImpl();
    private List<AdoptionEntity> adoptionList = new ArrayList<>();
    private List<AdoptionApplicationEntity> adoptionApplicationList = new ArrayList<>();
    private List<OwnerEntity> ownerList = new ArrayList<>();
    private List<PetEntity> petList = new ArrayList<>();
    private List<VeterinarianEntity> veterinarianList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }
    
    private void clearData() {
        entityManager.getEntityManager().createQuery("DELETE FROM AdoptionEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("DELETE FROM AdoptionApplicationEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("DELETE FROM OwnerEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("DELETE FROM PetEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("DELETE FROM VeterinarianEntity").executeUpdate();
    }
    
    private void insertData() {
        int n = 5;
        for (int i = 0; i < n; i++) {
            // Create and persist Owner
            OwnerEntity owner = factory.manufacturePojo(OwnerEntity.class);
            owner.setId(null); // Let JPA assign the ID
            entityManager.persist(owner);
            ownerList.add(owner);

            // Create and persist Pet
            PetEntity pet = factory.manufacturePojo(PetEntity.class);
            pet.setId(null); // Let JPA assign the ID
            entityManager.persist(pet);
            petList.add(pet);

            // Create and persist Veterinarian
            VeterinarianEntity veterinarian = factory.manufacturePojo(VeterinarianEntity.class);
            veterinarian.setId(null); // Let JPA assign the ID
            entityManager.persist(veterinarian);
            veterinarianList.add(veterinarian);

            // Create and persist AdoptionApplication
            AdoptionApplicationEntity adoptionApplication = factory.manufacturePojo(AdoptionApplicationEntity.class);
            adoptionApplication.setId(null); // Let JPA assign the ID
            entityManager.persist(adoptionApplication);
            adoptionApplicationList.add(adoptionApplication);
            
            // Create and persist Adoption
            AdoptionEntity adoption = factory.manufacturePojo(AdoptionEntity.class);
            adoption.setId(null); // Let JPA assign the ID
            adoption.setOwner(owner);
            adoption.setPet(pet);
            adoption.setVeterinarian(veterinarian);
            adoption.setAdoptionApplication(adoptionApplication);
            // Set required fields that might be null from factory
            adoption.setDescription("Test description " + i);
            adoption.setObservations("Test observations " + i);
            adoption.setAdoptionStatus(AdoptionStatusEnum.PENDING);
            
        
            entityManager.persist(adoption);
            adoptionList.add(adoption);
        }
        entityManager.flush(); // Ensure all entities are persisted
    }
    
    /**
     * Test for createAdoption method
     */
    @Test
    void testCreateAdoption() {
        // Create and persist new entities
        PetEntity newPet = factory.manufacturePojo(PetEntity.class);
        newPet.setId(null);
        entityManager.persist(newPet);
        
        VeterinarianEntity newVeterinarian = factory.manufacturePojo(VeterinarianEntity.class);
        newVeterinarian.setId(null);
        entityManager.persist(newVeterinarian);
        
        AdoptionApplicationEntity newApplication = factory.manufacturePojo(AdoptionApplicationEntity.class);
        newApplication.setId(null);
        entityManager.persist(newApplication);
        
        entityManager.flush(); // Ensure persistence before creating adoption
        
        // Create new adoption
        AdoptionEntity newAdoption = factory.manufacturePojo(AdoptionEntity.class);
        newAdoption.setId(null);
        newAdoption.setOwner(ownerList.get(0));
        newAdoption.setPet(newPet);
        newAdoption.setVeterinarian(newVeterinarian);
        newAdoption.setAdoptionApplication(newApplication);
        // Set required fields
        newAdoption.setDescription("New adoption description");
        newAdoption.setObservations("New adoption observations");
        newAdoption.setAdoptionStatus(AdoptionStatusEnum.PENDING);
        newAdoption.setDate(java.util.Date.from(java.time.LocalDateTime.now().atZone(java.time.ZoneId.systemDefault()).toInstant()));

        AdoptionEntity createdAdoption = adoptionService.createAdoption(newAdoption);

        assertNotNull(createdAdoption);
        assertNotNull(createdAdoption.getId());
        assertEquals(newAdoption.getDescription(), createdAdoption.getDescription());
        assertEquals(newAdoption.getObservations(), createdAdoption.getObservations());
        assertEquals(newAdoption.getOwner().getId(), createdAdoption.getOwner().getId());
        assertEquals(newAdoption.getPet().getId(), createdAdoption.getPet().getId());
        assertEquals(newAdoption.getVeterinarian().getId(), createdAdoption.getVeterinarian().getId());
        assertEquals(newAdoption.getAdoptionApplication().getId(), createdAdoption.getAdoptionApplication().getId());
    }
    
    /**
     * Test for getAllAdoptions method
     */
    @Test
    void testGetAllAdoptions() {
        List<AdoptionEntity> adoptions = adoptionService.getAllAdoptions();
        assertEquals(adoptionList.size(), adoptions.size());
    }
    
    /**
     * Test for getAdoptionById method
     */
    @Test
    void testGetAdoptionById() {
        AdoptionEntity adoption = adoptionList.get(0);
        AdoptionEntity foundAdoption = adoptionService.getAdoptionById(adoption.getId());
        assertNotNull(foundAdoption);
        assertEquals(adoption.getId(), foundAdoption.getId());
        assertEquals(adoption.getOwner().getId(), foundAdoption.getOwner().getId());
        assertEquals(adoption.getPet().getId(), foundAdoption.getPet().getId());
        assertEquals(adoption.getVeterinarian().getId(), foundAdoption.getVeterinarian().getId());
        assertEquals(adoption.getAdoptionApplication().getId(), foundAdoption.getAdoptionApplication().getId());
    }
    
    /**
     * Test for updateAdoption method
     */
    @Test
    void testUpdateAdoption() {
        AdoptionEntity adoption = adoptionList.get(0);
        AdoptionEntity updatedAdoption = factory.manufacturePojo(AdoptionEntity.class);
        updatedAdoption.setId(adoption.getId());
        updatedAdoption.setOwner(ownerList.get(1));
        updatedAdoption.setPet(petList.get(1));
        updatedAdoption.setVeterinarian(veterinarianList.get(1));
        updatedAdoption.setAdoptionApplication(adoptionApplicationList.get(1));
        // Set required fields
        updatedAdoption.setDescription("Updated description");
        updatedAdoption.setObservations("Updated observations");
        updatedAdoption.setAdoptionStatus(AdoptionStatusEnum.DONE);
        updatedAdoption.setDate(java.util.Date.from(java.time.LocalDateTime.now().atZone(java.time.ZoneId.systemDefault()).toInstant()));
        
        AdoptionEntity result = adoptionService.updateAdoption(adoption.getId(), updatedAdoption);
        assertNotNull(result);
        assertEquals(updatedAdoption.getId(), result.getId());
        assertEquals(updatedAdoption.getDescription(), result.getDescription());
        assertEquals(updatedAdoption.getObservations(), result.getObservations());
        assertEquals(updatedAdoption.getOwner().getId(), result.getOwner().getId());
        assertEquals(updatedAdoption.getPet().getId(), result.getPet().getId());
        assertEquals(updatedAdoption.getVeterinarian().getId(), result.getVeterinarian().getId());
        assertEquals(updatedAdoption.getAdoptionApplication().getId(), result.getAdoptionApplication().getId());
    }
    
    /**
     * Test for deleteAdoption method
     */
    @Test
    void testDeleteAdoption() {
        AdoptionEntity adoption = adoptionList.get(0);
        adoptionService.deleteAdoption(adoption.getId());
        entityManager.flush();
        AdoptionEntity deletedAdoption = entityManager.find(AdoptionEntity.class, adoption.getId());
        assertNull(deletedAdoption);
    }
    
    /**
     * Test for createAdoption with null description
     */
    @Test
    void testCreateAdoptionWithNullDescription() {
        AdoptionEntity adoption = factory.manufacturePojo(AdoptionEntity.class);
        adoption.setDescription(null);
        adoption.setObservations("Test observations");
        adoption.setAdoptionStatus(AdoptionStatusEnum.PENDING);
        adoption.setOwner(ownerList.get(0));
        adoption.setPet(petList.get(0));
        adoption.setVeterinarian(veterinarianList.get(0));
        adoption.setAdoptionApplication(adoptionApplicationList.get(0));
        
        assertThrows(IllegalArgumentException.class, () -> {
            adoptionService.createAdoption(adoption);
        });
    }
    
    /**
     * Test for createAdoption with null observations
     */
    @Test
    void testCreateAdoptionWithNullObservations() {
        AdoptionEntity adoption = factory.manufacturePojo(AdoptionEntity.class);
        adoption.setDescription("Test description");
        adoption.setObservations(null);
        adoption.setAdoptionStatus(AdoptionStatusEnum.PENDING);
        adoption.setOwner(ownerList.get(0));
        adoption.setPet(petList.get(0));
        adoption.setVeterinarian(veterinarianList.get(0));
        adoption.setAdoptionApplication(adoptionApplicationList.get(0));
        
        assertThrows(IllegalArgumentException.class, () -> {
            adoptionService.createAdoption(adoption);
        });
    }
    
    /**
     * Test for getAdoptionById with non-existing id
     */
    @Test
    void testGetAdoptionByIdWithNonExistingId() {
        AdoptionEntity foundAdoption = adoptionService.getAdoptionById(999L);
        assertNull(foundAdoption);
    }
    
    /**
     * Test for updateAdoption with non-existing id
     */
    @Test
    void testUpdateAdoptionWithNonExistingId() {
        AdoptionEntity adoption = factory.manufacturePojo(AdoptionEntity.class);
        adoption.setId(999L);
        adoption.setDescription("Test description");
        adoption.setObservations("Test observations");
        adoption.setAdoptionStatus(AdoptionStatusEnum.PENDING);
        
        assertThrows(IllegalArgumentException.class, () -> {
            adoptionService.updateAdoption(999L, adoption);
        });
    }
    
    /**
     * Test for deleteAdoption with non-existing id
     */
    @Test
    void testDeleteAdoptionWithNonExistingId() {
        assertThrows(IllegalArgumentException.class, () -> {
            adoptionService.deleteAdoption(999L);
        });
    }
}