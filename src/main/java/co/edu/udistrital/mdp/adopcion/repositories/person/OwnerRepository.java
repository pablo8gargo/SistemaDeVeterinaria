package co.edu.udistrital.mdp.adopcion.repositories.person;

import co.edu.udistrital.mdp.adopcion.entities.person.OwnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface OwnerRepository extends JpaRepository<OwnerEntity, Long> {

}
