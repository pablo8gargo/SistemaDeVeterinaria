package co.edu.udistrital.mdp.adopcion.entities.pet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import co.edu.udistrital.mdp.adopcion.entities.BaseEntity;
import co.edu.udistrital.mdp.adopcion.entities.ShelterEntity;
import co.edu.udistrital.mdp.adopcion.entities.adoption.AdoptionApplicationEntity;
import co.edu.udistrital.mdp.adopcion.entities.adoption.AdoptionEntity;
import co.edu.udistrital.mdp.adopcion.entities.events.MedicalEventEntity;
import co.edu.udistrital.mdp.adopcion.entities.events.ShelterArrivalEntity;
import co.edu.udistrital.mdp.adopcion.entities.events.medical.VaccineCardEntity;
import co.edu.udistrital.mdp.adopcion.entities.multimedia.MultimediaEntity;
import co.edu.udistrital.mdp.adopcion.entities.person.OwnerEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Entity
@Data
public class PetEntity extends BaseEntity {

    private String name;
    private Date birthDate;
    private String breed;
    private SizeEnum size;
    private GenderEnum gender;
    private String behaviorProfile;

    @PodamExclude
    @ManyToMany(mappedBy="pets")
    private List<OwnerEntity> owners = new ArrayList<>();

    @PodamExclude
    @OneToMany(mappedBy = "pet")
    private List<AdoptionApplicationEntity> adoptionApplications = new ArrayList<>();
    
    @PodamExclude
    @OneToMany(mappedBy = "pet")
    private List<MedicalEventEntity> medicalEvents = new ArrayList<>();
    
    @PodamExclude
    @OneToMany(mappedBy = "pet")
    private List<MultimediaEntity> multimedia = new ArrayList<>();
    
    @PodamExclude
    @OneToOne(mappedBy = "pet")
    private AdoptionEntity adoption;
    
    @PodamExclude
    @OneToOne(mappedBy = "pet")
    private ShelterArrivalEntity shelterArrival;
    
    @PodamExclude
    @ManyToOne
    private ShelterEntity shelter;
    
    @PodamExclude
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "vaccine_card_id")
    private VaccineCardEntity vaccineCard;
}
