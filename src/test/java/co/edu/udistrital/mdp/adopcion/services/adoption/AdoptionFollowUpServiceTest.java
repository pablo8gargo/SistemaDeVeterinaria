package co.edu.udistrital.mdp.adopcion.services.adoption;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.adopcion.entities.adoption.AdoptionFollowUpEntity;
import co.edu.udistrital.mdp.adopcion.entities.person.VeterinarianEntity;
import co.edu.udistrital.mdp.adopcion.entities.pet.PetConditionEnum;
import co.edu.udistrital.mdp.adopcion.entities.adoption.FollowUpStatusEnum;
import co.edu.udistrital.mdp.adopcion.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.adopcion.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(AdoptionFollowUpService.class)
public class AdoptionFollowUpServiceTest {

    @Autowired
    private AdoptionFollowUpService adoptionFollowUpService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    private List<AdoptionFollowUpEntity> followUpList = new ArrayList<>();
    private List<VeterinarianEntity> veterinarianList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        followUpList = new ArrayList<>();
        veterinarianList = new ArrayList<>();
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from AdoptionFollowUpEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from VeterinarianEntity").executeUpdate();
    }

    private void insertData() {
        int n = 5;
        for (int i = 0; i < n; i++) {
            VeterinarianEntity vet = factory.manufacturePojo(VeterinarianEntity.class);
            entityManager.persist(vet);
            veterinarianList.add(vet);

            AdoptionFollowUpEntity followUp = factory.manufacturePojo(AdoptionFollowUpEntity.class);
            followUp.setVeterinarian(vet);
            followUp.setFollowUpStatus(FollowUpStatusEnum.values()[i % FollowUpStatusEnum.values().length]);
            followUp.setPetCondition(PetConditionEnum.values()[i % PetConditionEnum.values().length]);
            entityManager.persist(followUp);
            followUpList.add(followUp);
        }
    }

    /**
     * Test for createFollowUp method
     */
    @Test
    void testCreateFollowUp() throws IllegalOperationException {
        AdoptionFollowUpEntity followUp = factory.manufacturePojo(AdoptionFollowUpEntity.class);
        VeterinarianEntity vet = veterinarianList.get(0);
        followUp.setVeterinarian(vet);
        followUp.setFollowUpStatus(FollowUpStatusEnum.SCHEDULED);
        followUp.setPetCondition(PetConditionEnum.HEALTHY);
        
        AdoptionFollowUpEntity created = adoptionFollowUpService.createFollowUp(followUp);
        
        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals(followUp.getVeterinarian().getId(), created.getVeterinarian().getId());
        assertEquals(followUp.getFollowUpStatus(), created.getFollowUpStatus());
        assertEquals(followUp.getPetCondition(), created.getPetCondition());
    }

    /**
     * Test for getAllFollowUps method
     */
    @Test
    void testGetAllFollowUps() {
        List<AdoptionFollowUpEntity> list = adoptionFollowUpService.getAllFollowUps();
        assertEquals(followUpList.size(), list.size());
    }

    /**
     * Test for getFollowUpById method
     */
    @Test
    void testGetFollowUpById() throws EntityNotFoundException {
        AdoptionFollowUpEntity followUp = followUpList.get(0);
        AdoptionFollowUpEntity found = adoptionFollowUpService.getFollowUpById(followUp.getId());
        assertNotNull(found);
        assertEquals(followUp.getId(), found.getId());
        assertEquals(followUp.getDescription(), found.getDescription());
    }

    /**
     * Test for updateFollowUp method
     */
    @Test
    void testUpdateFollowUp() throws EntityNotFoundException {
        AdoptionFollowUpEntity followUp = followUpList.get(0);
        AdoptionFollowUpEntity updated = factory.manufacturePojo(AdoptionFollowUpEntity.class);
        
        updated.setId(followUp.getId());
        updated.setVeterinarian(veterinarianList.get(1));
        updated.setFollowUpStatus(FollowUpStatusEnum.COMPLETED);
        updated.setPetCondition(PetConditionEnum.INJURED);
        
        AdoptionFollowUpEntity result = adoptionFollowUpService.updateFollowUp(followUp.getId(), updated);

        assertNotNull(result);
        assertEquals(updated.getId(), result.getId());
        assertEquals(updated.getVeterinarian().getId(), result.getVeterinarian().getId());
        assertEquals(updated.getFollowUpStatus(), result.getFollowUpStatus());
        assertEquals(updated.getPetCondition(), result.getPetCondition());
    }

    /**
     * Test for deleteFollowUp method
     */
    @Test
    void testDeleteFollowUp() throws EntityNotFoundException {
        AdoptionFollowUpEntity followUp = followUpList.get(0);
        adoptionFollowUpService.deleteFollowUp(followUp.getId());
        AdoptionFollowUpEntity deleted = entityManager.find(AdoptionFollowUpEntity.class, followUp.getId());
        assertNull(deleted);
    }
}

