package kg.amanturov.jortartip.model;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.relational.core.sql.In;

import java.math.BigInteger;
import java.sql.Timestamp;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String role;

    private String username;

    private String password;

    private String passportSerial;

    private Timestamp signupDate;

    private BigInteger inn;

    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private CommonReference region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id")
    private CommonReference district;

}