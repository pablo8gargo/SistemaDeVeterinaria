package co.edu.udistrital.mdp.adopcion.dto.events;

import java.util.Date;

import lombok.Data;

@Data
public abstract class EventDTO {
    private Long id;
    private String description;
    private Date date;
}
