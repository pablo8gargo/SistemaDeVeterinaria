package co.edu.udistrital.mdp.adopcion.services.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.adopcion.entities.adoption.AdoptionEntity;
import co.edu.udistrital.mdp.adopcion.entities.events.MedicalEventEntity;
import co.edu.udistrital.mdp.adopcion.entities.person.VeterinarianEntity;
import co.edu.udistrital.mdp.adopcion.entities.pet.PetEntity;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


@DataJpaTest
@Transactional
@Import(MedicalEventService.class)
public class MedicalEventServiceTest {
    @Autowired
    private MedicalEventService medicalEventService;

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<MedicalEventEntity> medicalEventList = new ArrayList<>();
    private final List<VeterinarianEntity> veterinarianList = new ArrayList<>();
    private final List<PetEntity> petList = new ArrayList<>();
    private final List<Date> dateList = new ArrayList<>();
    private final List<String> descriptionList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from MedicalEventEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from PetEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from VeterinarianEntity").executeUpdate();
    }

    private void insertData() {
        int n = 5;
        for (int i = 0; i < n; i++) {
            VeterinarianEntity veterinarian = factory.manufacturePojo(VeterinarianEntity.class);
            entityManager.persist(veterinarian);
            veterinarianList.add(veterinarian);
    
            AdoptionEntity adoption = factory.manufacturePojo(AdoptionEntity.class);
            entityManager.persist(adoption);
            
            PetEntity pet = factory.manufacturePojo(PetEntity.class);
            pet.setAdoption(adoption);
            entityManager.persist(pet);
            petList.add(pet);
    
            Date date = factory.manufacturePojo(Date.class);
            dateList.add(date);
    
            String description = factory.manufacturePojo(String.class);
            descriptionList.add(description);
        }
    
        for (int i = 0; i < n; i++) {
            MedicalEventEntity medicalEvent = factory.manufacturePojo(MedicalEventEntity.class);
            medicalEvent.setVeterinarian(veterinarianList.get(i));
            medicalEvent.setPet(petList.get(i));
            medicalEvent.setDate(dateList.get(i));
            medicalEvent.setDescription(descriptionList.get(i));
            entityManager.persist(medicalEvent);
            medicalEventList.add(medicalEvent);
        }
    }

    /**
     * Test for createMedicalEvent method
     */
    @Test
    void testCreateMedicalEvent() {
        MedicalEventEntity medicalEvent = factory.manufacturePojo(MedicalEventEntity.class);
        medicalEvent.setVeterinarian(veterinarianList.get(0));
        medicalEvent.setPet(petList.get(0));
        medicalEvent.setDate(dateList.get(0));
        medicalEvent.setDescription(descriptionList.get(0));

        MedicalEventEntity createdMedicalEvent = medicalEventService.createMedicalEvent(medicalEvent);
        assertNotNull(createdMedicalEvent);
        MedicalEventEntity foundMedicalEvent = entityManager.find(MedicalEventEntity.class,
                createdMedicalEvent.getId());
        assertEquals(medicalEvent.getVeterinarian().getId(), foundMedicalEvent.getVeterinarian().getId());
        assertEquals(medicalEvent.getPet().getId(), foundMedicalEvent.getPet().getId());
        assertEquals(medicalEvent.getDate(), foundMedicalEvent.getDate());
        assertEquals(medicalEvent.getDescription(), foundMedicalEvent.getDescription());
    }

    @Test
    void testCreateMedicalEventWithNullVeterinarian() {
        MedicalEventEntity medicalEvent = factory.manufacturePojo(MedicalEventEntity.class);
        medicalEvent.setVeterinarian(null);
        medicalEvent.setPet(petList.get(0));
        medicalEvent.setDate(dateList.get(0));
        medicalEvent.setDescription(descriptionList.get(0));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            medicalEventService.createMedicalEvent(medicalEvent);
        });
        assertNotNull(exception.getMessage());
    }

    @Test
    void testCreateMedicalEventWithNullPet() {
        MedicalEventEntity medicalEvent = factory.manufacturePojo(MedicalEventEntity.class);
        medicalEvent.setVeterinarian(veterinarianList.get(0));
        medicalEvent.setPet(null);
        medicalEvent.setDate(dateList.get(0));
        medicalEvent.setDescription(descriptionList.get(0));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            medicalEventService.createMedicalEvent(medicalEvent);
        });
        assertNotNull(exception.getMessage());
    }

    @Test
    void testCreateMedicalEventWithValidParams() {
        MedicalEventEntity medicalEvent = factory.manufacturePojo(MedicalEventEntity.class);
        medicalEvent.setVeterinarian(veterinarianList.get(0));
        medicalEvent.setPet(petList.get(0));
        medicalEvent.setDate(dateList.get(0));
        medicalEvent.setDescription(descriptionList.get(0));

        MedicalEventEntity createdMedicalEvent = medicalEventService.createMedicalEvent(medicalEvent);
        assertNotNull(createdMedicalEvent);
        assertEquals(medicalEvent.getVeterinarian().getId(), createdMedicalEvent.getVeterinarian().getId());
        assertEquals(medicalEvent.getPet().getId(), createdMedicalEvent.getPet().getId());
        assertEquals(medicalEvent.getDate(), createdMedicalEvent.getDate());
        assertEquals(medicalEvent.getDescription(), createdMedicalEvent.getDescription());
    }

    /**
     * Test for getAllMedicalEvents method
     */
    @Test
    void testGetAllMedicalEvents() {
        List<MedicalEventEntity> foundMedicalEvents = medicalEventService.getAllMedicalEvents();
        assertNotNull(foundMedicalEvents);
        assertEquals(medicalEventList.size(), foundMedicalEvents.size());
        for (int i = 0; i < medicalEventList.size(); i++) {
            assertEquals(medicalEventList.get(i).getId(), foundMedicalEvents.get(i).getId());
        }
    }

    /**
     * Test for getMedicalEventById method
     */
    @Test
    void testGetMedicalEventById() {
        MedicalEventEntity medicalEvent = medicalEventList.get(0);
        MedicalEventEntity foundMedicalEvent = medicalEventService.getMedicalEventById(medicalEvent.getId());
        assertNotNull(foundMedicalEvent);
        assertEquals(medicalEvent.getId(), foundMedicalEvent.getId());
        assertEquals(medicalEvent.getVeterinarian().getId(), foundMedicalEvent.getVeterinarian().getId());
        assertEquals(medicalEvent.getPet().getId(), foundMedicalEvent.getPet().getId());
        assertEquals(medicalEvent.getDate(), foundMedicalEvent.getDate());
        assertEquals(medicalEvent.getDescription(), foundMedicalEvent.getDescription());
    }

    /**
     * Test for getMedicalEventById method with non-existing ID
     */
    @Test
    void testGetMedicalEventByIdWithNonExistingId() {
        MedicalEventEntity foundMedicalEvent = medicalEventService.getMedicalEventById(999L);
        assertNull(foundMedicalEvent);
    }

    /**
     * Test for updateMedicalEvent method
     */
    @Test
    void testUpdateMedicalEvent() {
        MedicalEventEntity medicalEvent = medicalEventList.get(0);
        MedicalEventEntity updatedMedicalEvent = factory.manufacturePojo(MedicalEventEntity.class);
        updatedMedicalEvent.setVeterinarian(veterinarianList.get(1));
        updatedMedicalEvent.setPet(petList.get(1));
        updatedMedicalEvent.setDate(dateList.get(1));
        updatedMedicalEvent.setDescription(descriptionList.get(1));

        MedicalEventEntity result = medicalEventService.updateMedicalEvent(medicalEvent.getId(), updatedMedicalEvent);
        assertNotNull(result);
        assertEquals(updatedMedicalEvent.getVeterinarian().getId(), result.getVeterinarian().getId());
        assertEquals(updatedMedicalEvent.getPet().getId(), result.getPet().getId());
        assertEquals(updatedMedicalEvent.getDate(), result.getDate());
        assertEquals(updatedMedicalEvent.getDescription(), result.getDescription());
    }

    /**
     * Test for updateMedicalEvent method with non-existing ID
     */
    @Test
    void testUpdateMedicalEventWithNonExistingId() {
        MedicalEventEntity medicalEvent = factory.manufacturePojo(MedicalEventEntity.class);
        medicalEvent.setVeterinarian(veterinarianList.get(0));
        medicalEvent.setPet(petList.get(0));
        medicalEvent.setDate(dateList.get(0));
        medicalEvent.setDescription(descriptionList.get(0));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            medicalEventService.updateMedicalEvent(999L, medicalEvent);
        });
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("does not exist"));
    }
}
