package kg.amanturov.jortartip.dto;


import lombok.Data;

import java.security.Timestamp;
@Data
public class EventDto {

    private Long id;
    private String title;
    private String description;
    private Timestamp startDate;
    private Timestamp endDate;

}
