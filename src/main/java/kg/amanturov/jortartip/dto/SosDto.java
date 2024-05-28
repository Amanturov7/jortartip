package kg.amanturov.jortartip.dto;

import lombok.Data;

import java.math.BigInteger;
import java.sql.Timestamp;

@Data
public class SosDto {

    private Long id;
    private String title;
    private String description;
    private Float lat;
    private Float lon;
    private String address;
    private Timestamp created;
    private Timestamp updated;
    private Long userId;
    private Long typeSosId;
    private String typeSosName;
    private BigInteger phone;

}

