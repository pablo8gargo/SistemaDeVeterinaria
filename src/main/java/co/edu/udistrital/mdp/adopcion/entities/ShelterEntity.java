package co.edu.udistrital.mdp.adopcion.entities;

import java.util.ArrayList;
import java.util.List;

import co.edu.udistrital.mdp.adopcion.entities.events.ShelterArrivalEntity;
import co.edu.udistrital.mdp.adopcion.entities.events.ShelterEventEntity;
import co.edu.udistrital.mdp.adopcion.entities.multimedia.MultimediaEntity;
import co.edu.udistrital.mdp.adopcion.entities.pet.PetEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class ShelterEntity extends BaseEntity {
    private String name;
    private String address;
    private String phone;
    private String email;

    @PodamExclude
    @OneToMany(mappedBy = "shelter")
    private List<ShelterEventEntity> shelterEvents;

    @PodamExclude
    @OneToMany(mappedBy = "shelter")
    private List<PetEntity> pets = new ArrayList<>();

    @PodamExclude
    @OneToMany(mappedBy = "shelter")
    private List<ShelterArrivalEntity> shelterArrivals = new ArrayList<>();

    @PodamExclude
    @OneToMany(mappedBy = "shelter")
    private List<MultimediaEntity> multimedia = new ArrayList<>();
}
