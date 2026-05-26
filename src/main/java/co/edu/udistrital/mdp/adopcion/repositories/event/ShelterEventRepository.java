package co.edu.udistrital.mdp.adopcion.repositories.event;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.adopcion.entities.events.ShelterEventEntity;

public interface ShelterEventRepository extends JpaRepository<ShelterEventEntity, Long> {

}
