package co.edu.udistrital.mdp.adopcion.services.multimedia;

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

import co.edu.udistrital.mdp.adopcion.entities.multimedia.MultimediaEntity;
import co.edu.udistrital.mdp.adopcion.entities.pet.PetEntity;
import co.edu.udistrital.mdp.adopcion.exceptions.EntityNotFoundException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(MultimediaService.class)
public class MultimediaServiceTest {

    @Autowired
    private MultimediaService multimediaService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    private List<MultimediaEntity> multimediaList = new ArrayList<>();
    private List<PetEntity> petList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        multimediaList = new ArrayList<>();
        petList = new ArrayList<>();
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from MultimediaEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from PetEntity").executeUpdate();
    }

    private void insertData() {
        int n = 5;
        for (int i = 0; i < n; i++) {
            PetEntity pet = factory.manufacturePojo(PetEntity.class);
            entityManager.persist(pet);
            petList.add(pet);

            MultimediaEntity multimedia = factory.manufacturePojo(MultimediaEntity.class);
            multimedia.setPet(pet);
            entityManager.persist(multimedia);
            multimediaList.add(multimedia);
        }
    }

    @Test
    void testCreateMultimedia() {
        MultimediaEntity multimedia = factory.manufacturePojo(MultimediaEntity.class);
        multimedia.setPet(petList.get(0));
        MultimediaEntity created = multimediaService.createMultimedia(multimedia);
        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals(multimedia.getPet().getId(), created.getPet().getId());
    }

    @Test
    void testGetAllMultimedia() {
        List<MultimediaEntity> list = multimediaService.getAllMultimedia();
        assertEquals(multimediaList.size(), list.size());
    }

    @Test
    void testGetMultimediaById() throws EntityNotFoundException {
        MultimediaEntity multimedia = multimediaList.get(0);
        MultimediaEntity found = multimediaService.getMultimediaById(multimedia.getId());
        assertNotNull(found);
        assertEquals(multimedia.getId(), found.getId());
    }

    @Test
    void testUpdateMultimedia() throws EntityNotFoundException {
        MultimediaEntity multimedia = multimediaList.get(0);
        MultimediaEntity updated = factory.manufacturePojo(MultimediaEntity.class);
        updated.setId(multimedia.getId());
        updated.setPet(petList.get(1));
        MultimediaEntity result = multimediaService.updateMultimedia(multimedia.getId(), updated);
        assertNotNull(result);
        assertEquals(updated.getUrl(), result.getUrl());
        assertEquals(updated.getMultimediaType(), result.getMultimediaType());
        assertEquals(updated.getPet().getId(), result.getPet().getId());
    }

    @Test
    void testDeleteMultimedia() throws EntityNotFoundException {
        MultimediaEntity multimedia = multimediaList.get(0);
        multimediaService.deleteMultimedia(multimedia.getId());
        MultimediaEntity deleted = entityManager.find(MultimediaEntity.class, multimedia.getId());
        assertNull(deleted);
    }
}

