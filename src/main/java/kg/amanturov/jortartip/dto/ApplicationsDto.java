package kg.amanturov.jortartip.dto;

import lombok.Data;

import java.security.Timestamp;

@Data
public class ApplicationsDto {
    private Long id;
    private String title;
    private String description;
    private String place;
    private float lon;
    private float lat;
    private Long status;
    private Long regionId;
    private Long districtId;
    private Long typeViolationsId;
    private Long userId;
    private Timestamp createdDate;
    private Timestamp updateDate;

}
