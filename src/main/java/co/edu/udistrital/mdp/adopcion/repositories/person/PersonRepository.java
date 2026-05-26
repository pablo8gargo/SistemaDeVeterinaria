package co.edu.udistrital.mdp.adopcion.repositories.person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.edu.udistrital.mdp.adopcion.entities.person.PersonEntity;

@Repository

public interface PersonRepository extends JpaRepository<PersonEntity, Long> {


}
