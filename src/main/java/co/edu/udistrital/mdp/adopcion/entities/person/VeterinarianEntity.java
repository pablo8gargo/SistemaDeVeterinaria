package co.edu.udistrital.mdp.adopcion.entities.person;

import java.util.ArrayList;
import java.util.List;

import co.edu.udistrital.mdp.adopcion.entities.adoption.AdoptionApplicationEntity;
import co.edu.udistrital.mdp.adopcion.entities.adoption.AdoptionEntity;
import co.edu.udistrital.mdp.adopcion.entities.adoption.AdoptionFollowUpEntity;
import co.edu.udistrital.mdp.adopcion.entities.adoption.AdoptionTestEntity;
import co.edu.udistrital.mdp.adopcion.entities.events.MedicalEventEntity;
import co.edu.udistrital.mdp.adopcion.entities.events.ShelterArrivalEntity;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class VeterinarianEntity extends PersonEntity {
    private String licenseNumber;
    private Speciality speciality;

    @PodamExclude
    @OneToMany(mappedBy = "veterinarian")
    private List<MedicalEventEntity> medicalEvents;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<DisponibilityEnum> disponibilities;
    
    @PodamExclude
    @OneToMany(mappedBy="veterinarian")
    private List<AdoptionApplicationEntity> adoptionApplications = new ArrayList<>();

    @OneToMany(mappedBy = "veterinarian")
    private List<AdoptionFollowUpEntity> followUps = new ArrayList<>();

    @PodamExclude
    @OneToMany(mappedBy="veterinarian")
    private List<AdoptionEntity> adoption = new ArrayList<>();

    @PodamExclude
    @OneToMany(mappedBy="veterinarian")
    private List<AdoptionTestEntity> adoptionTests = new ArrayList<>();

    @PodamExclude
    @OneToMany(mappedBy="veterinarian")
    private List<ShelterArrivalEntity> shelterArrivals = new ArrayList<>();
}
