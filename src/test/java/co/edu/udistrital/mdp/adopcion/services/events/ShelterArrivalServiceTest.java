package co.edu.udistrital.mdp.adopcion.services.events;

import java.util.ArrayList;
import java.util.Date;
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

import co.edu.udistrital.mdp.adopcion.entities.ShelterEntity;
import co.edu.udistrital.mdp.adopcion.entities.events.ShelterArrivalEntity;
import co.edu.udistrital.mdp.adopcion.entities.person.VeterinarianEntity;
import co.edu.udistrital.mdp.adopcion.entities.pet.PetConditionEnum;
import co.edu.udistrital.mdp.adopcion.entities.pet.PetEntity;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(ShelterArrivalService.class)
public class ShelterArrivalServiceTest {
    @Autowired
    private ShelterArrivalService shelterArrivalService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    private List<ShelterArrivalEntity> shelterArrivalList = new ArrayList<>();
    private List<PetConditionEnum> petConditionList = new ArrayList<>();
    private List<Date> dateList = new ArrayList<>();
    private List<String> arrivalDetailsList = new ArrayList<>();
    private List<ShelterEntity> shelterList = new ArrayList<>();
    private List<VeterinarianEntity> veterinarianList = new ArrayList<>();
    private List<PetEntity> petList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from ShelterArrivalEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from PetEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ShelterEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from VeterinarianEntity").executeUpdate();
    }

    private void insertData() {
        int n = 5;
        for (int i = 0; i < n; i++) {
            PetConditionEnum petCondition = factory.manufacturePojo(PetConditionEnum.class);
            petConditionList.add(petCondition);

            Date date = factory.manufacturePojo(Date.class);
            dateList.add(date);

            String arrivalDetails = factory.manufacturePojo(String.class);
            arrivalDetailsList.add(arrivalDetails);


            PetEntity pet = factory.manufacturePojo(PetEntity.class);
            entityManager.persist(pet);
            petList.add(pet);

            VeterinarianEntity veterinarian = factory.manufacturePojo(VeterinarianEntity.class);
            entityManager.persist(veterinarian);
            veterinarianList.add(veterinarian);
            
            ShelterEntity shelter = factory.manufacturePojo(ShelterEntity.class);
            entityManager.persist(shelter);
            shelterList.add(shelter);
        }
        for (int i = 0; i < n; i++) {

            ShelterArrivalEntity shelterArrival = factory.manufacturePojo(ShelterArrivalEntity.class);
            shelterArrival.setPet(petList.get(i));
            shelterArrival.setShelter(shelterList.get(i));
            shelterArrival.setVeterinarian(veterinarianList.get(i));
            shelterArrival.setDescription(arrivalDetailsList.get(i));
            shelterArrival.setDate(dateList.get(i));
            shelterArrival.setPetCondition(petConditionList.get(i));
            entityManager.persist(shelterArrival);
            shelterArrivalList.add(shelterArrival);
        }
    }

    @Test
    public void testCreateShelterArrival() {
        // Busca un Pet que no tenga ShelterArrival asociado
        PetEntity petSinLlegada = factory.manufacturePojo(PetEntity.class);
        entityManager.persist(petSinLlegada);

        ShelterArrivalEntity newShelterArrival = factory.manufacturePojo(ShelterArrivalEntity.class);
        newShelterArrival.setPet(petSinLlegada);
        newShelterArrival.setDate(dateList.get(0));
        newShelterArrival.setShelter(shelterList.get(0));
        newShelterArrival.setVeterinarian(veterinarianList.get(0));
        newShelterArrival.setDescription(arrivalDetailsList.get(0));
        newShelterArrival.setPetCondition(petConditionList.get(0));

        ShelterArrivalEntity createdShelterArrival = shelterArrivalService.createShelterArrival(newShelterArrival);

        assertNotNull(createdShelterArrival);
        assertEquals(newShelterArrival.getPet(), createdShelterArrival.getPet());
        assertEquals(newShelterArrival.getShelter(), createdShelterArrival.getShelter());
        assertEquals(newShelterArrival.getVeterinarian(), createdShelterArrival.getVeterinarian());
        assertEquals(newShelterArrival.getDescription(), createdShelterArrival.getDescription());
        assertEquals(newShelterArrival.getPetCondition(), createdShelterArrival.getPetCondition());
    }

    @Test
    public void testCreateShelterArrivalWithNullVeterinarian() {
        ShelterArrivalEntity newShelterArrival = factory.manufacturePojo(ShelterArrivalEntity.class);
        newShelterArrival.setVeterinarian(null);
        newShelterArrival.setPet(petList.get(0));
        newShelterArrival.setShelter(shelterList.get(0));
        newShelterArrival.setDescription(arrivalDetailsList.get(0));
        newShelterArrival.setDate(dateList.get(0));
        newShelterArrival.setPetCondition(petConditionList.get(0));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            shelterArrivalService.createShelterArrival(newShelterArrival);
        });
        assertNotNull(exception.getMessage());
    }

    @Test
    public void testGetAllShelterArrivals() {
        List<ShelterArrivalEntity> shelterArrivals = shelterArrivalService.getAllShelterArrivals();
        assertEquals(shelterArrivalList.size(), shelterArrivals.size());
    }

    @Test
    public void testGetShelterArrivalById() {
        ShelterArrivalEntity shelterArrival = shelterArrivalList.get(0);
        ShelterArrivalEntity foundShelterArrival = shelterArrivalService.getShelterArrivalById(shelterArrival.getId());
        assertNotNull(foundShelterArrival);
        assertEquals(shelterArrival.getId(), foundShelterArrival.getId());
    }

    @Test
    public void testUpdateShelterArrival() {
        ShelterArrivalEntity shelterArrival = shelterArrivalList.get(0);
        ShelterArrivalEntity updatedShelterArrival = factory.manufacturePojo(ShelterArrivalEntity.class);
        updatedShelterArrival.setId(shelterArrival.getId());
        updatedShelterArrival.setPet(petList.get(0));
        updatedShelterArrival.setShelter(shelterList.get(0));
        updatedShelterArrival.setVeterinarian(veterinarianList.get(0));
        updatedShelterArrival.setDescription(arrivalDetailsList.get(0));
        updatedShelterArrival.setPetCondition(petConditionList.get(0));

        ShelterArrivalEntity result = shelterArrivalService.updateShelterArrival(shelterArrival.getId(), updatedShelterArrival);

        assertNotNull(result);
        assertEquals(updatedShelterArrival.getPet(), result.getPet());
        assertEquals(updatedShelterArrival.getShelter(), result.getShelter());
        assertEquals(updatedShelterArrival.getVeterinarian(), result.getVeterinarian());
        assertEquals(updatedShelterArrival.getPetCondition(), result.getPetCondition());
    }

    @Test
    public void testDeleteShelterArrival() {
        ShelterArrivalEntity shelterArrival = shelterArrivalList.get(0);
        shelterArrivalService.deleteShelterArrival(shelterArrival.getId());
        ShelterArrivalEntity deletedShelterArrival = entityManager.find(ShelterArrivalEntity.class, shelterArrival.getId());
        assertNull(deletedShelterArrival);
    }
}
