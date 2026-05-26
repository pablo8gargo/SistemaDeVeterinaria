package co.edu.udistrital.mdp.adopcion.services.events.medical;

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

import co.edu.udistrital.mdp.adopcion.entities.events.medical.VaccineCardEntity;
import co.edu.udistrital.mdp.adopcion.entities.events.medical.VaccineEntity;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(VaccineService.class)
public class VaccineServiceTest {

    @Autowired
    private VaccineService vaccineService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    private List<VaccineEntity> vaccineList = new ArrayList<>();
    private List<VaccineCardEntity> vaccineCardList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from VaccineEntity");
        entityManager.getEntityManager().createQuery("delete from VaccineCardEntity");
    }

    private void insertData() {
        int n = 5;
        for (int i = 0; i < n; i++) {
            VaccineEntity vaccine = factory.manufacturePojo(VaccineEntity.class);
            entityManager.persist(vaccine);
            vaccineList.add(vaccine);

            VaccineCardEntity vaccineCard = factory.manufacturePojo(VaccineCardEntity.class);
            entityManager.persist(vaccineCard);
            vaccineCardList.add(vaccineCard);
        }
    }

    /**
     * Test for createVaccine method
     */
    @Test
    void testCreateVaccine() {
        VaccineEntity vaccine = factory.manufacturePojo(VaccineEntity.class);
        vaccine.setVaccineCard(vaccineCardList.get(0));
        VaccineEntity createdVaccine = vaccineService.createVaccine(vaccine);
        assertNotNull(createdVaccine);
        assertEquals(vaccine.getName(), createdVaccine.getName());
        assertEquals(vaccine.getBrandName(), createdVaccine.getBrandName());
        assertEquals(vaccine.getNextDate(), createdVaccine.getNextDate());
        assertEquals(vaccine.getDosis(), createdVaccine.getDosis());
    }
    /**
     * Test for getAllVaccines method
     */
    @Test
    void testGetAllVaccines() {
        List<VaccineEntity> vaccines = vaccineService.getAllVaccines();
        assertEquals(vaccineList.size(), vaccines.size());
    }
    /**
     * Test for getVaccineById method
     */
    @Test
    void testGetVaccineById() {
        VaccineEntity vaccine = vaccineList.get(0);
        VaccineEntity foundVaccine = vaccineService.getVaccineById(vaccine.getId());
        assertNotNull(foundVaccine);
        assertEquals(vaccine.getName(), foundVaccine.getName());
        assertEquals(vaccine.getBrandName(), foundVaccine.getBrandName());
        assertEquals(vaccine.getNextDate(), foundVaccine.getNextDate());
        assertEquals(vaccine.getDosis(), foundVaccine.getDosis());
    }
    /**
     * Test for updateVaccine method
     */
    @Test
    void testUpdateVaccine() {
        VaccineEntity vaccine = vaccineList.get(0);
        vaccine.setName("Updated Name");
        VaccineEntity updatedVaccine = vaccineService.updateVaccine(vaccine.getId(), vaccine);
        assertNotNull(updatedVaccine);
        assertEquals(vaccine.getName(), updatedVaccine.getName());
    }
    /**
     * Test for deleteVaccine method
     */
    @Test
    void testDeleteVaccine() {
        VaccineEntity vaccine = vaccineList.get(0);
        vaccineService.deleteVaccine(vaccine.getId());
        VaccineEntity deletedVaccine = vaccineService.getVaccineById(vaccine.getId());
        assertNull(deletedVaccine);
    }
    /**
     * Test for createVaccine with null name
     */
    @Test
    void testCreateVaccineWithNullName() {
        VaccineEntity vaccine = factory.manufacturePojo(VaccineEntity.class);
        vaccine.setName(null);
        assertThrows(IllegalArgumentException.class, () -> {
            vaccineService.createVaccine(vaccine);
        });
    }
    /**
     * Test for createVaccine with null brand name
     */
    @Test
    void testCreateVaccineWithNullBrandName() {
        VaccineEntity vaccine = factory.manufacturePojo(VaccineEntity.class);
        vaccine.setBrandName(null);
        assertThrows(IllegalArgumentException.class, () -> {
            vaccineService.createVaccine(vaccine);
        });
    }
}
