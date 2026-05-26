package co.edu.udistrital.mdp.adopcion.repositories.pet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.adopcion.entities.pet.PetEntity;
@Repository

public interface PetRepository extends JpaRepository <PetEntity, Long> {
}
