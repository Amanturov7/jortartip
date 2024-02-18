package kg.amanturov.jortartip.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
public class ReviewDto {
    private Long id;
    private Float lat;
    private Float lon;
    private String locationAddress;
    private Timestamp createdDate;
    private Timestamp updatedDate;
    private String description;
    private Long userId;
    private Long roadId;
    private Long statusId;
    private String statusName;
    private Long lightId;
    private Long roadSignId;
    private Long ecologicFactorsId;
}
