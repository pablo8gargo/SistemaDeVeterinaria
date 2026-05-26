package co.edu.udistrital.mdp.adopcion.repositories.person;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.adopcion.entities.person.VeterinarianEntity;

public interface VeterinarianRepository extends JpaRepository<VeterinarianEntity, Long> {
}
