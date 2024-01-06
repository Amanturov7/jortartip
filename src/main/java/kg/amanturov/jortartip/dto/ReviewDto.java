package kg.amanturov.jortartip.dto;

import lombok.Data;

@Data
public class ReviewDto {
    private Long id;
    private Float lat;
    private Float lon;
    private String locationAddress;
    private String description;
    private Long userId;
    private Long roadId;
    private Long lightId;
    private Long roadSignId;
    private Long ecologicFactorsId;
}
