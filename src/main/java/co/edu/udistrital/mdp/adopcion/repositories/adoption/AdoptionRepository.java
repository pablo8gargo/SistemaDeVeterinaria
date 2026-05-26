package co.edu.udistrital.mdp.adopcion.repositories.adoption;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.adopcion.entities.adoption.AdoptionEntity;

public interface AdoptionRepository extends JpaRepository<AdoptionEntity, Long> {

}
