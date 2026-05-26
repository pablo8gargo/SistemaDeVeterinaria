package co.edu.udistrital.mdp.adopcion.services.events;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import co.edu.udistrital.mdp.adopcion.entities.events.ShelterEventEntity;
import co.edu.udistrital.mdp.adopcion.entities.ShelterEntity;

@DataJpaTest
@Transactional
@Import(ShelterEventService.class)
public class ShelterEventServiceTest {
    @Autowired
    private ShelterEventService shelterEventService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    private List<ShelterEventEntity> shelterEventList = new ArrayList<>();
    private List<ShelterEntity> shelterList = new ArrayList<>();
    private List<String> nameList = new ArrayList<>();
    private List<Date> eventDateList = new ArrayList<>();
    private List<String> descriptionList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from ShelterEventEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ShelterEntity").executeUpdate();
    }

    private void insertData() {
        int n = 5;
        for (int i = 0; i < n; i++) {
            ShelterEntity shelter = factory.manufacturePojo(ShelterEntity.class);
            entityManager.persist(shelter);
            shelterList.add(shelter);

            String name = factory.manufacturePojo(String.class);
            nameList.add(name);

            Date eventDate = factory.manufacturePojo(Date.class);
            eventDateList.add(eventDate);

            String description = factory.manufacturePojo(String.class);
            descriptionList.add(description);
        }

        for (int i = 0; i < n; i++) {
            ShelterEventEntity shelterEvent = factory.manufacturePojo(ShelterEventEntity.class);
            shelterEvent.setShelter(shelterList.get(i));
            shelterEvent.setName(nameList.get(i));
            shelterEvent.setDate(eventDateList.get(i));
            shelterEvent.setDescription(descriptionList.get(i));
            entityManager.persist(shelterEvent);
            shelterEventList.add(shelterEvent);
        }
    }

    @Test
    public void testCreateShelterEvent() {
        ShelterEventEntity newShelterEvent = factory.manufacturePojo(ShelterEventEntity.class);
        newShelterEvent.setShelter(shelterList.get(0));
        newShelterEvent.setName(nameList.get(0));
        newShelterEvent.setDate(eventDateList.get(0));
        newShelterEvent.setDescription(descriptionList.get(0));

        ShelterEventEntity createdShelterEvent = shelterEventService.createShelterEvent(newShelterEvent);

        assertNotNull(createdShelterEvent);
        assertEquals(newShelterEvent.getName(), createdShelterEvent.getName());
        assertEquals(newShelterEvent.getShelter().getId(), createdShelterEvent.getShelter().getId());
    }

    @Test
    public void testGetAllShelterEvents() {
        List<ShelterEventEntity> shelterEvents = shelterEventService.getAllShelterEvents();
        assertEquals(shelterEventList.size(), shelterEvents.size());
    }

    @Test
    public void testGetShelterEventById() {
        ShelterEventEntity shelterEvent = shelterEventList.get(0);
        ShelterEventEntity foundShelterEvent = shelterEventService.getShelterEventById(shelterEvent.getId());
        assertNotNull(foundShelterEvent);
        assertEquals(shelterEvent.getId(), foundShelterEvent.getId());
    }

    @Test
    public void testUpdateShelterEvent() {
        ShelterEventEntity shelterEvent = shelterEventList.get(0);
        ShelterEventEntity updatedShelterEvent = factory.manufacturePojo(ShelterEventEntity.class);
        updatedShelterEvent.setId(shelterEvent.getId());
        updatedShelterEvent.setShelter(shelterList.get(0));
        updatedShelterEvent.setName("Updated Shelter Event Name");
        updatedShelterEvent.setDate(new Date());
        updatedShelterEvent.setDescription("Updated description");


        ShelterEventEntity result = shelterEventService.updateShelterEvent(shelterEvent.getId(), updatedShelterEvent);
        
        assertNotNull(result);
        assertEquals(updatedShelterEvent.getName(), result.getName());
        assertEquals(updatedShelterEvent.getShelter().getId(), result.getShelter().getId());
        assertEquals(updatedShelterEvent.getDescription(), result.getDescription());
    }

    @Test
    public void testDeleteShelterEvent() {
        ShelterEventEntity shelterEvent = shelterEventList.get(0);
        shelterEventService.deleteShelterEvent(shelterEvent.getId());
        ShelterEventEntity deletedShelterEvent = entityManager.find(ShelterEventEntity.class, shelterEvent.getId());
        assertNull(deletedShelterEvent);
    }

    @Test
    public void testDeleteNonExistentShelterEvent() {
        assertThrows(IllegalArgumentException.class, () -> {
            shelterEventService.deleteShelterEvent(Long.MAX_VALUE);
        });
    }

    @Test
    public void testCreateShelterEventWithNullShelter() {
        ShelterEventEntity shelterEvent = factory.manufacturePojo(ShelterEventEntity.class);
        shelterEvent.setShelter(null);
        shelterEvent.setName(nameList.get(0));
        shelterEvent.setDate(eventDateList.get(0));
        shelterEvent.setDescription(descriptionList.get(0));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            shelterEventService.createShelterEvent(shelterEvent);
        });
        assertNotNull(exception.getMessage());
    }

    
    @Test
    public void testCreateShelterEventWithNullName() {
        ShelterEventEntity shelterEvent = factory.manufacturePojo(ShelterEventEntity.class);
        shelterEvent.setShelter(shelterList.get(0));
        shelterEvent.setName(null);
        shelterEvent.setDate(eventDateList.get(0));
        shelterEvent.setDescription(descriptionList.get(0));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            shelterEventService.createShelterEvent(shelterEvent);
        });
        assertNotNull(exception.getMessage());
    }
}
