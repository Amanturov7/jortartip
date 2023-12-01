package kg.amanturov.jortartip.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ViolationsDto {
    private Long id;
    private String title;
    private String statya;
    private Integer part;
    private String description;
    private Timestamp createdDate;
    private Timestamp updateDate;
    private Float costFiz;
    private Float costUr;
    private Long organId;
}
