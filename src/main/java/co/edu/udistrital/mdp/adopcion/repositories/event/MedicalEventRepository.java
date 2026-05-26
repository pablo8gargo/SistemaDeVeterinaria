package co.edu.udistrital.mdp.adopcion.repositories.event;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.adopcion.entities.events.MedicalEventEntity;

public interface MedicalEventRepository extends JpaRepository<MedicalEventEntity, Long>{

}
