package co.edu.udistrital.mdp.adopcion.entities.person;


import java.util.ArrayList;
import java.util.List;

import co.edu.udistrital.mdp.adopcion.entities.adoption.AdoptionApplicationEntity;
import co.edu.udistrital.mdp.adopcion.entities.adoption.AdoptionEntity;
import co.edu.udistrital.mdp.adopcion.entities.adoption.AdoptionTestEntity;
import co.edu.udistrital.mdp.adopcion.entities.pet.PetEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;


@Data
@Entity

public class OwnerEntity extends PersonEntity {
    private HouseTypeEnum houseType;
    private String address;

    @PodamExclude
    @OneToMany(mappedBy="owner")
	private List<AdoptionEntity> adoptions = new ArrayList<>();

    @PodamExclude
    @OneToMany(mappedBy="owner")
	private List<AdoptionTestEntity> adoptionTests = new ArrayList<>();

    @PodamExclude
    @OneToMany(mappedBy="owner")
    private List<AdoptionApplicationEntity> adoptionApplications = new ArrayList<>();

    @PodamExclude
    @ManyToMany
    private List<PetEntity> pets = new ArrayList<>(); // Lista de mascotas del dueño
}
