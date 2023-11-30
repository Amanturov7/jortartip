package kg.amanturov.jortartip.dto;


import lombok.Data;

import java.sql.Timestamp;
@Data
public class EventDto {

    private Long id;
    private String title;
    private String description;
    private Timestamp startDate;
    private Timestamp endDate;

}
