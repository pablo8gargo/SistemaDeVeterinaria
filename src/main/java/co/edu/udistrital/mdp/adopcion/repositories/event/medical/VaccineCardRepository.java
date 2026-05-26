package co.edu.udistrital.mdp.adopcion.repositories.event.medical;
import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.adopcion.entities.events.medical.VaccineCardEntity;


public interface VaccineCardRepository extends JpaRepository<VaccineCardEntity, Long>{

}
