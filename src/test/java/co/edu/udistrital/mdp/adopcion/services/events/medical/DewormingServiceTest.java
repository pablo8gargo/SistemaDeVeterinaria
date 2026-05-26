package co.edu.udistrital.mdp.adopcion.services.events.medical;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.adopcion.entities.events.medical.DewormingEntity;
import co.edu.udistrital.mdp.adopcion.entities.events.medical.VaccineCardEntity;
import co.edu.udistrital.mdp.adopcion.entities.pet.PetEntity;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(DewormingService.class)
public class DewormingServiceTest {
    @Autowired
    private DewormingService dewormingService;

    @Autowired
    private TestEntityManager entityManager;


    private PodamFactory factory = new PodamFactoryImpl();
    private List<PetEntity> petList = new ArrayList<>();
    private List<DewormingEntity> dewormingList = new ArrayList<>();
    private List<VaccineCardEntity> vaccineCardList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from DewormingEntity");
        entityManager.getEntityManager().createQuery("delete from PetEntity");
        entityManager.getEntityManager().createQuery("delete from VaccineCardEntity");
    }

    private void insertData() {
        int n = 5;
        for (int i = 0; i < n; i++) {
            PetEntity pet = factory.manufacturePojo(PetEntity.class);
            entityManager.persist(pet);
            petList.add(pet);

            DewormingEntity deworming = factory.manufacturePojo(DewormingEntity.class);
            deworming.setPet(pet);
            entityManager.persist(deworming);
            dewormingList.add(deworming);

            VaccineCardEntity vaccineCard = factory.manufacturePojo(VaccineCardEntity.class);
            entityManager.persist(vaccineCard);
            vaccineCardList.add(vaccineCard);
        }
    }
    /**
     * Test for createDeworming method
     */
    @Test
    public void testCreateDeworming() {
        DewormingEntity deworming = factory.manufacturePojo(DewormingEntity.class);

        VaccineCardEntity vaccineCard = vaccineCardList.get(0);
        deworming.setVaccineCard(vaccineCard);
        DewormingEntity result = dewormingService.createDeworming(deworming);
        assertNotNull(result);
        assertEquals(deworming.getId(), result.getId());
        assertEquals(deworming.getDate(), result.getDate());
        assertEquals(deworming.getVaccineCard(), result.getVaccineCard());
    }
    /**
     * Test for updateDeworming method
     */
    @Test
    public void testUpdateDeworming() {
        DewormingEntity deworming = dewormingList.get(0);
        DewormingEntity newDeworming = factory.manufacturePojo(DewormingEntity.class);
        newDeworming.setId(deworming.getId());
        // Pass the ID and the updated entity to the service method
        dewormingService.updateDeworming(deworming.getId(), newDeworming);
        DewormingEntity result = entityManager.find(DewormingEntity.class, deworming.getId());
        assertEquals(newDeworming.getDate(), result.getDate());
        assertEquals(newDeworming.getVaccineCard(), result.getVaccineCard());
        assertEquals(newDeworming.getId(), result.getId());
    }
    /**
     * Test for deleteDeworming method
     */
    @Test
    public void testDeleteDeworming() {
        DewormingEntity deworming = dewormingList.get(0);
        dewormingService.deleteDeworming(deworming.getId());
        DewormingEntity result = entityManager.find(DewormingEntity.class, deworming.getId());
        assertNull(result);
    }
    /**
     * Test for getAllDewormings method
     */
    @Test
    public void testGetAllDewormings() {
        List<DewormingEntity> result = dewormingService.getAllDewormings();
        assertEquals(dewormingList.size(), result.size());
    }
    /**
     * Test for getDewormingById method
     */
    @Test
    public void testGetDewormingById() {
        DewormingEntity deworming = dewormingList.get(0);
        DewormingEntity result = dewormingService.getDewormingById(deworming.getId());
        assertNotNull(result);
        assertEquals(deworming.getId(), result.getId());
        assertEquals(deworming.getDate(), result.getDate());
        assertEquals(deworming.getVaccineCard(), result.getVaccineCard());
    }
}
