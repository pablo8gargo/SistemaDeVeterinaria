package co.edu.udistrital.mdp.adopcion.repositories.event.medical;
import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.adopcion.entities.events.medical.VaccineEntity;

public interface VaccineRepository extends JpaRepository<VaccineEntity, Long> {
    // Custom query methods can be defined here if needed

}
