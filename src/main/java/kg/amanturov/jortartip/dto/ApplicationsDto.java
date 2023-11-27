package kg.amanturov.jortartip.dto;

import lombok.Data;

@Data
public class ApplicationsDto {
    private Long id;
    private String title;
    private String description;
    private String place;
    private float lon;
    private float lat;
    private String status;
    private Long typeViolationsId;
    private Long userId;
}
