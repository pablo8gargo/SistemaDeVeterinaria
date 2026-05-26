package co.edu.udistrital.mdp.adopcion.entities.person;


import co.edu.udistrital.mdp.adopcion.entities.BaseEntity;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class PersonEntity extends BaseEntity {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

}