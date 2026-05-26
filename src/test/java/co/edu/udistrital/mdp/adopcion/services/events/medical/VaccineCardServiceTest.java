package co.edu.udistrital.mdp.adopcion.services.events.medical;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.adopcion.entities.events.medical.DewormingEntity;
import co.edu.udistrital.mdp.adopcion.entities.events.medical.VaccineCardEntity;
import co.edu.udistrital.mdp.adopcion.entities.events.medical.VaccineEntity;
import co.edu.udistrital.mdp.adopcion.entities.pet.PetEntity;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(VaccineCardService.class)
public class VaccineCardServiceTest {
    @Autowired
    private VaccineCardService vaccineCardService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    private List<VaccineCardEntity> vaccineCardList = new ArrayList<>();
    private List<VaccineEntity> vaccineList = new ArrayList<>();
    private List<DewormingEntity> dewormingList = new ArrayList<>();
    private List<PetEntity> petList= new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }
    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from VaccineCardEntity");
        entityManager.getEntityManager().createQuery("delete from PetEntity");
        entityManager.getEntityManager().createQuery("delete from VaccineEntity");
        entityManager.getEntityManager().createQuery("delete from DewormingEntity");
    }
    
    private void insertData() {
        int n = 5;
        for (int i = 0; i < n; i++) {
            PetEntity pet = factory.manufacturePojo(PetEntity.class);
            entityManager.persist(pet);
            petList.add(pet);

            VaccineCardEntity vaccineCard = factory.manufacturePojo(VaccineCardEntity.class);
            entityManager.persist(vaccineCard);
            vaccineCardList.add(vaccineCard);

            
            DewormingEntity deworming = factory.manufacturePojo(DewormingEntity.class);
            deworming.setPet(pet);
            entityManager.persist(deworming);
            dewormingList.add(deworming);
            
            VaccineEntity vaccine = factory.manufacturePojo(VaccineEntity.class);
            vaccineCard.setPet(pet);
            vaccineCard.setVaccines(vaccineList);
            vaccineCard.setDewormings(dewormingList);
            entityManager.persist(vaccine);
            vaccineList.add(vaccine);
        }
    }

    /**
     * Test for createVaccineCard method
     */
    @Test
    void testCreateVaccineCard() {
        VaccineCardEntity newVaccineCard = factory.manufacturePojo(VaccineCardEntity.class);
        newVaccineCard.setPet(petList.get(0));
        newVaccineCard.setVaccines(vaccineList);
        newVaccineCard.setDewormings(dewormingList);
        VaccineCardEntity createdVaccineCard = vaccineCardService.createVaccineCard(newVaccineCard);
        assert createdVaccineCard != null;
        assert createdVaccineCard.getId() != null;
    }
    /**
     * Test for getAllVaccineCards method
     */
    @Test
    void testGetAllVaccineCards() {
        List<VaccineCardEntity> vaccineCards = vaccineCardService.getAllVaccineCards();
        assert vaccineCards.size() == vaccineCardList.size();
    }
    /**
     * Test for getVaccineCardById method
     */
    @Test
    void testGetVaccineCardById() {
        VaccineCardEntity vaccineCard = vaccineCardService.getVaccineCardById(vaccineCardList.get(0).getId());
        assert vaccineCard != null;
        assert vaccineCard.getId().equals(vaccineCardList.get(0).getId());
    }
    /**
     * Test for updateVaccineCard method
     */
    @Test
    void testUpdateVaccineCard() {
        VaccineCardEntity vaccineCard = vaccineCardList.get(0);
        vaccineCard.setLastVacineDate(factory.manufacturePojo(Date.class));
        vaccineCard.setLastDewormingDate(factory.manufacturePojo(Date.class));
        VaccineCardEntity updatedVaccineCard = vaccineCardService.updateVaccineCard(vaccineCard.getId(), vaccineCard);
        assert updatedVaccineCard != null;
        assert updatedVaccineCard.getId().equals(vaccineCard.getId());
        assert updatedVaccineCard.getLastVacineDate().equals(vaccineCard.getLastVacineDate());
        assert updatedVaccineCard.getLastDewormingDate().equals(vaccineCard.getLastDewormingDate());
    }
    /**
     * Test for deleteVaccineCard method
     */
    @Test
    void testDeleteVaccineCard() {
        VaccineCardEntity vaccineCard = vaccineCardList.get(0);
        vaccineCardService.deleteVaccineCard(vaccineCard.getId());
        VaccineCardEntity deletedVaccineCard = vaccineCardService.getVaccineCardById(vaccineCard.getId());
        assert deletedVaccineCard == null;
    }
}
