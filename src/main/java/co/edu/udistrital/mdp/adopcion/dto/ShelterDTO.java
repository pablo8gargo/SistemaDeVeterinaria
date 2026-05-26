package co.edu.udistrital.mdp.adopcion.dto;

import lombok.Data;

@Data
public class ShelterDTO {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;
}