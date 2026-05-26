package co.edu.udistrital.mdp.adopcion.repositories.event;

import co.edu.udistrital.mdp.adopcion.entities.events.ShelterArrivalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  ShelterArrivalRepository extends JpaRepository<ShelterArrivalEntity, Long> {

}
